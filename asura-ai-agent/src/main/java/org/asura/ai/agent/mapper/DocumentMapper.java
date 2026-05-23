package org.asura.ai.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.agent.entity.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文档Mapper接口
 * 提供文档数据的持久化操作
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {

    /**
     * 根据关键词搜索文档
     * @param keyword 关键词
     * @return 匹配的文档列表
     */
    @Select("SELECT * FROM documents WHERE content LIKE CONCAT('%', #{keyword}, '%') OR original_filename LIKE CONCAT('%', #{keyword}, '%')")
    List<Document> searchByKeyword(@Param("keyword") String keyword);
}