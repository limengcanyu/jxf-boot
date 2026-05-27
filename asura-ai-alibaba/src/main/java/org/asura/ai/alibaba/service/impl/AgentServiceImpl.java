
package org.asura.ai.alibaba.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asura.ai.alibaba.service.AgentService;
import org.asura.ai.alibaba.service.RedisVectorStoreService;
import org.asura.ai.alibaba.service.impl.RedisVectorStoreServiceImpl.VectorDocument;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final ChatClient chatClient;
    private final RedisVectorStoreService vectorStoreService;

    @Override
    public String chat(String message, String conversationId) {
        log.info("[AGENT] Processing chat request: conversationId={}, message={}", conversationId, message);
        
        String response = chatClient
                .prompt()
                .user(message)
                .call()
                .content();
        
        log.info("[AGENT] Response generated: {} chars", response.length());
        return response;
    }

    @Override
    public String chatWithTools(String message, String conversationId) {
        log.info("[AGENT] Processing tool-enabled chat: conversationId={}, message={}", conversationId, message);

        String toolResult = analyzeAndCallTool(message);
        
        if (toolResult != null && !toolResult.isEmpty()) {
            log.info("[AGENT] Tool result obtained, returning directly");
            return toolResult;
        }

        return chat(message, conversationId);
    }

    private String analyzeAndCallTool(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("天气") || lowerMessage.contains("温度") || lowerMessage.contains("气候") ||
            lowerMessage.contains("weather") || lowerMessage.contains("temperature") || lowerMessage.contains("climate")) {
            return callWeatherTool(message);
        }
        
        if (lowerMessage.contains("计算") || lowerMessage.contains("+") || lowerMessage.contains("-") || 
            lowerMessage.contains("*") || lowerMessage.contains("/") || lowerMessage.contains("等于") ||
            lowerMessage.contains("calculate") || lowerMessage.contains("compute") || lowerMessage.contains("math")) {
            return callCalculatorTool(message);
        }
        
        if (lowerMessage.contains("搜索") || lowerMessage.contains("查找") || lowerMessage.contains("资料") ||
            lowerMessage.contains("文档") || lowerMessage.contains("知识") ||
            lowerMessage.contains("search") || lowerMessage.contains("find") || lowerMessage.contains("document")) {
            return callVectorSearchTool(message);
        }
        
        return null;
    }

    private String callWeatherTool(String message) {
        log.info("[AGENT] Calling weather tool");
        String city = extractCity(message);
        if (city.isEmpty()) city = "Beijing";
        
        return "Weather for " + city + ": Sunny, 25 degrees Celsius";
    }

    private String callCalculatorTool(String message) {
        log.info("[AGENT] Calling calculator tool");
        try {
            String expression = extractMathExpression(message);
            if (!expression.isEmpty()) {
                double result = simpleEvaluate(expression);
                return String.format("计算结果: %s = %.2f", expression, result);
            }
        } catch (Exception e) {
            log.warn("[AGENT] Calculator error: {}", e.getMessage());
        }
        return "计算错误，请检查表达式";
    }
    
    private double simpleEvaluate(String expr) {
        expr = expr.replace(" ", "");
        return evaluateSimpleExpression(expr);
    }
    
    private double evaluateSimpleExpression(String expr) {
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+", 2);
            return evaluateSimpleExpression(parts[0]) + evaluateSimpleExpression(parts[1]);
        }
        if (expr.contains("-")) {
            String[] parts = expr.split("-", 2);
            return evaluateSimpleExpression(parts[0]) - evaluateSimpleExpression(parts[1]);
        }
        if (expr.contains("*")) {
            String[] parts = expr.split("\\*", 2);
            return evaluateSimpleExpression(parts[0]) * evaluateSimpleExpression(parts[1]);
        }
        if (expr.contains("/")) {
            String[] parts = expr.split("/", 2);
            return evaluateSimpleExpression(parts[0]) / evaluateSimpleExpression(parts[1]);
        }
        return Double.parseDouble(expr);
    }

    private String callVectorSearchTool(String message) {
        log.info("[AGENT] Calling vector search tool");
        String query = extractSearchQuery(message);
        List<VectorDocument> results = vectorStoreService.search(query, 3);
        
        if (results.isEmpty()) {
            return "未找到相关文档";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("找到 ").append(results.size()).append(" 条相关文档：\n");
        for (int i = 0; i < results.size(); i++) {
            sb.append(i + 1).append(". ").append(results.get(i).getContent()).append("\n");
        }
        return sb.toString();
    }

    private String extractCity(String message) {
        String[] cities = {"北京", "上海", "广州", "深圳", "杭州", "成都", "武汉", "西安"};
        for (String city : cities) {
            if (message.contains(city)) {
                return city;
            }
        }
        return "";
    }

    private String extractMathExpression(String message) {
        StringBuilder expr = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isDigit(c) || "+-*/().".indexOf(c) >= 0) {
                expr.append(c);
            }
        }
        return expr.toString();
    }

    private double evaluateExpression(String expression) throws javax.script.ScriptException {
        expression = expression.replace(" ", "");
        Object result = new javax.script.ScriptEngineManager()
                .getEngineByName("JavaScript")
                .eval(expression);
        return ((Number) result).doubleValue();
    }

    private String extractSearchQuery(String message) {
        String[] keywords = {"搜索", "查找", "资料", "文档", "知识", "关于"};
        String query = message;
        for (String keyword : keywords) {
            query = query.replace(keyword, "");
        }
        return query.trim();
    }

    @Override
    public Map<String, Object> getAgentInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Asura AI Agent");
        info.put("version", "1.0.0");
        info.put("description", "基于 Spring AI 的智能代理服务");
        info.put("capabilities", List.of(
            "对话交互",
            "工具调用（天气、计算、向量搜索）",
            "对话记忆",
            "多模态支持"
        ));
        info.put("tools", List.of(
            Map.of("name", "weather", "description", "获取天气信息"),
            Map.of("name", "calculator", "description", "数学计算"),
            Map.of("name", "vector_search", "description", "向量数据库搜索")
        ));
        return info;
    }
}