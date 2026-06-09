package org.asura.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.entity.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
    List<Document> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 分页查询文档
     * @param params 包含offset, size, categoryId
     * @return 文档列表
     */
    List<Document> selectByPage(Map<String, Object> params);

    /**
     * 根据分类统计文档数量
     * @param categoryId 分类ID
     * @return 文档数量
     */
    long countByCategory(@Param("categoryId") String categoryId);

    /**
     * 分页搜索文档
     * @param params 包含keyword, offset, size
     * @return 匹配的文档列表
     */
    List<Document> searchByKeywordPage(Map<String, Object> params);

    /**
     * 根据关键词统计文档数量
     * @param keyword 关键词
     * @return 文档数量
     */
    long countByKeyword(@Param("keyword") String keyword);
}