package org.asura.ai.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.agent.entity.DocumentChunk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文档块Mapper接口
 * 提供文档块数据的持久化操作
 */
@Mapper
public interface DocumentChunkMapper extends BaseMapper<DocumentChunk> {

    /**
     * 根据文档ID查询所有文档块
     * @param documentId 文档ID
     * @return 文档块列表
     */
    List<DocumentChunk> selectByDocumentId(String documentId);

    /**
     * 根据文档ID删除所有文档块
     * @param documentId 文档ID
     */
    void deleteByDocumentId(String documentId);
}