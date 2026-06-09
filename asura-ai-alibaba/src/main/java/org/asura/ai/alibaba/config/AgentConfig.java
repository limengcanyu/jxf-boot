
package org.asura.ai.alibaba.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AgentConfig {
    
    /**
     * 创建ChatClient Bean
     * 
     * @param chatModel 聊天模型
     * @return ChatClient实例
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
    
    /**
     * 代理工具记录类
     * 
     * @param name 工具名称
     * @param description 工具描述
     */
    public record AgentTool(String name, String description) {}
    
    /**
     * 创建天气工具定义
     * 
     * @return 天气工具实例
     */
    public AgentTool weatherTool() {
        return new AgentTool("weather", "获取指定城市的天气信息");
    }
    
    /**
     * 创建计算器工具定义
     * 
     * @return 计算器工具实例
     */
    public AgentTool calculatorTool() {
        return new AgentTool("calculator", "执行数学计算");
    }
    
    /**
     * 创建向量存储搜索工具定义
     * 
     * @return 向量搜索工具实例
     */
    public AgentTool vectorStoreTool() {
        return new AgentTool("vector_search", "从向量数据库中搜索相关文档");
    }
}