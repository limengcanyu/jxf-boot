package org.asura.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.entity.DocumentCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文档分类Mapper接口
 */
@Mapper
public interface DocumentCategoryMapper extends BaseMapper<DocumentCategory> {

    List<DocumentCategory> selectByParentId(String parentId);

    List<DocumentCategory> selectAllWithTree();
}