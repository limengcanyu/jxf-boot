
package org.asura.ai.alibaba.service;

import org.asura.ai.alibaba.dto.request.ChatRequest;
import org.asura.ai.alibaba.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {

    ChatResponse chat(ChatRequest request);

    ChatResponse chatWithHistory(ChatRequest request, List<?> history);
}