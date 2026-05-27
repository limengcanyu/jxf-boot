
package org.asura.ai.alibaba.service;

import java.util.Map;

public interface AgentService {
    
    String chat(String message, String conversationId);
    
    String chatWithTools(String message, String conversationId);
    
    Map<String, Object> getAgentInfo();
}