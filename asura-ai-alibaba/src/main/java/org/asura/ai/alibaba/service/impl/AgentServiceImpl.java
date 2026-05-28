
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

    /**
     * 基础对话实现
     * 直接调用AI模型生成响应
     * 
     * @param message 用户消息
     * @param conversationId 会话ID
     * @return AI响应内容
     */
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

    /**
     * 带工具调用的对话实现
     * 先分析用户意图，决定是否调用工具，如不调用则回退到基础对话
     * 
     * @param message 用户消息
     * @param conversationId 会话ID
     * @return 工具调用结果或AI响应内容
     */
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

    /**
     * 分析用户意图并调用相应工具
     * 
     * @param message 用户消息
     * @return 工具执行结果，如不匹配任何工具则返回null
     */
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

    /**
     * 调用天气工具
     * 
     * @param message 用户消息
     * @return 天气信息字符串
     */
    private String callWeatherTool(String message) {
        log.info("[AGENT] Calling weather tool");
        String city = extractCity(message);
        if (city.isEmpty()) city = "Beijing";
        
        return "Weather for " + city + ": Sunny, 25 degrees Celsius";
    }

    /**
     * 调用计算器工具
     * 
     * @param message 用户消息
     * @return 计算结果字符串
     */
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
    
    /**
     * 简单表达式求值入口
     * 
     * @param expr 数学表达式
     * @return 计算结果
     */
    private double simpleEvaluate(String expr) {
        expr = expr.replace(" ", "");
        return evaluateSimpleExpression(expr);
    }
    
    /**
     * 递归解析并计算简单数学表达式
     * 
     * @param expr 数学表达式
     * @return 计算结果
     */
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

    /**
     * 调用向量搜索工具
     * 
     * @param message 用户消息
     * @return 搜索结果字符串
     */
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

    /**
     * 从消息中提取城市名称
     * 
     * @param message 用户消息
     * @return 城市名称，未找到则返回空字符串
     */
    private String extractCity(String message) {
        String[] cities = {"北京", "上海", "广州", "深圳", "杭州", "成都", "武汉", "西安"};
        for (String city : cities) {
            if (message.contains(city)) {
                return city;
            }
        }
        return "";
    }

    /**
     * 从消息中提取数学表达式
     * 
     * @param message 用户消息
     * @return 提取的数学表达式
     */
    private String extractMathExpression(String message) {
        StringBuilder expr = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isDigit(c) || "+-*/().".indexOf(c) >= 0) {
                expr.append(c);
            }
        }
        return expr.toString();
    }

    /**
     * 使用JavaScript引擎计算表达式（备用方法）
     * 
     * @param expression 数学表达式
     * @return 计算结果
     * @throws javax.script.ScriptException 脚本执行异常
     */
    private double evaluateExpression(String expression) throws javax.script.ScriptException {
        expression = expression.replace(" ", "");
        Object result = new javax.script.ScriptEngineManager()
                .getEngineByName("JavaScript")
                .eval(expression);
        return ((Number) result).doubleValue();
    }

    /**
     * 从消息中提取搜索关键词
     * 
     * @param message 用户消息
     * @return 提取的搜索关键词
     */
    private String extractSearchQuery(String message) {
        String[] keywords = {"搜索", "查找", "资料", "文档", "知识", "关于"};
        String query = message;
        for (String keyword : keywords) {
            query = query.replace(keyword, "");
        }
        return query.trim();
    }

    /**
     * 获取代理服务信息
     * 
     * @return 代理信息Map
     */
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