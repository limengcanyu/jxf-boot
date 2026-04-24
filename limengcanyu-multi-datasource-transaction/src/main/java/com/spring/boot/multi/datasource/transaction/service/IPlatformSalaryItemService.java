package com.spring.boot.multi.datasource.transaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.boot.multi.datasource.transaction.dao.entity.PlatformSalaryItem;

/**
 * <p>
 * 薪资项定义表 服务类
 * </p>
 */
public interface IPlatformSalaryItemService extends IService<PlatformSalaryItem> {

    boolean addItem() throws Exception;

    boolean updateItem() throws Exception;
}
