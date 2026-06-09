package org.asura.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.entity.ConversationMemory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 对话记忆Mapper接口
 * 提供对话历史数据的持久化操作
 */
@Mapper
public interface ConversationMemoryMapper extends BaseMapper<ConversationMemory> {

    /**
     * 根据对话ID查询对话历史
     * @param conversationId 对话ID
     * @return 对话历史列表
     */
    List<ConversationMemory> selectByConversationId(String conversationId);

    /**
     * 根据对话ID删除对话历史
     * @param conversationId 对话ID
     */
    void deleteByConversationId(String conversationId);
}