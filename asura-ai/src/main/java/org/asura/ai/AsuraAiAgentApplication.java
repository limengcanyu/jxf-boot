package org.asura.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Asura AI Agent 应用程序启动类
 * 基于 Spring Boot 的 AI 智能代理服务，提供文档处理、向量存储和 RAG（Retrieval-Augmented Generation）问答功能
 */
@SpringBootApplication
public class AsuraAiAgentApplication {

    /**
     * 应用程序主入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AsuraAiAgentApplication.class, args);
    }

}