package org.asura.ai.service.impl;

import jakarta.annotation.Resource;
import org.asura.ai.service.RAGService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * RAG（Retrieval-Augmented Generation）服务实现类
 * 基于向量检索和大语言模型实现智能问答
 */
@Service
public class RAGServiceImpl implements RAGService {

    @Resource
    private VectorStore vectorStore;

    @Resource
    private ChatClient.Builder chatClientBuilder;

    /** RAG提示词模板 */
    private static final String RAG_PROMPT_TEMPLATE = """
            请根据以下提供的知识库内容来回答用户的问题。

            知识库内容:
            {document_content}

            用户问题:
            {question}

            请仅根据提供的知识库内容进行回答。如果知识库中没有相关信息，请回答"根据知识库内容，我无法回答这个问题。"
            """;

    /**
     * 回答用户问题（无上下文）
     * 从向量存储中检索相关文档，构建提示词并调用大语言模型
     * @param question 用户问题
     * @return 回答内容
     */
    @Override
    public String ask(String question) {
        try {
            SearchRequest searchRequest = SearchRequest.builder().query(question).topK(5).build();
            List<Document> retrievedDocs = vectorStore.similaritySearch(searchRequest);

            StringBuilder documentContent = new StringBuilder();
            for (Document doc : retrievedDocs) {
                documentContent.append(doc.getText()).append("\n\n");
            }

            PromptTemplate promptTemplate = new PromptTemplate(RAG_PROMPT_TEMPLATE);
            Map<String, Object> params = Map.of(
                    "document_content", documentContent.toString(),
                    "question", question
            );
            Prompt prompt = promptTemplate.create(params);

            return chatClientBuilder.build().prompt(prompt).call().content();
        } catch (Exception e) {
            e.printStackTrace();
            return "查询失败: " + e.getMessage();
        }
    }

    /**
     * 回答用户问题（带上下文）
     * 当前实现与ask方法相同，预留扩展用于支持对话历史
     * @param question 用户问题
     * @param conversationId 对话ID
     * @return 回答内容
     */
    @Override
    public String askWithContext(String question, String conversationId) {
        return ask(question);
    }

    /**
     * 清除对话历史
     * 当前实现为空，预留扩展用于清除对话历史记录
     * @param conversationId 对话ID
     */
    @Override
    public void clearConversation(String conversationId) {
    }
}