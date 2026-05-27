package org.asura.ai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 对话记忆实体类
 * 存储用户与AI的对话历史，用于上下文感知的问答
 */
@TableName("conversation_memory")
public class ConversationMemory {

    /** 主键ID，UUID自动生成 */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /** 对话ID，用于标识同一会话 */
    private String conversationId;

    /** 角色：user（用户）或 assistant（助手） */
    private String role;

    /** 对话内容 */
    private String content;

    /** 创建时间 */
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}