
package org.asura.ai.alibaba.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AgentConfig {
    
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
    
    public record AgentTool(String name, String description) {}
    
    public AgentTool weatherTool() {
        return new AgentTool("weather", "获取指定城市的天气信息");
    }
    
    public AgentTool calculatorTool() {
        return new AgentTool("calculator", "执行数学计算");
    }
    
    public AgentTool vectorStoreTool() {
        return new AgentTool("vector_search", "从向量数据库中搜索相关文档");
    }
}