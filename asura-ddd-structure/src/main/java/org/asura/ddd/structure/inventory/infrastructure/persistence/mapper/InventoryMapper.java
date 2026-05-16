package org.asura.ddd.structure.inventory.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.asura.ddd.structure.inventory.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMapper extends BaseMapper<InventoryEntity> {

    IPage<InventoryEntity> selectPageByCondition(Page<InventoryEntity> page,
                                                  @Param("productId") String productId);

    InventoryEntity selectByProductId(@Param("productId") String productId);
}