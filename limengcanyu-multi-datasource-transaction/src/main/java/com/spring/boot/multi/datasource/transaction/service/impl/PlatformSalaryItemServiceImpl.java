package com.spring.boot.multi.datasource.transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.boot.multi.datasource.transaction.service.IPlatformSalaryItemService;
import com.spring.boot.multi.datasource.transaction.dao.entity.PlatformSalaryItem;
import com.spring.boot.multi.datasource.transaction.dao.mapper.PlatformSalaryItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 薪资项定义表 服务实现类
 * </p>
 */
@Service
public class PlatformSalaryItemServiceImpl extends ServiceImpl<PlatformSalaryItemMapper, PlatformSalaryItem> implements IPlatformSalaryItemService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addItem() throws Exception {

        try {
            PlatformSalaryItem item = new PlatformSalaryItem();
            item.setItemCode("item_134");
            item.setItemName("item_134");
            save(item);

            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

//    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateItem() throws Exception {
        PlatformSalaryItem update1 = new PlatformSalaryItem();
        update1.setId(155);
        update1.setItemName("实发补偿金111");
        updateById(update1);

        throw new Exception();

//        PlatformSalaryItem update2 = new PlatformSalaryItem();
//        update1.setId(156);
//        update1.setItemName("报盘工资111");
//        updateById(update1);
//
//        return false;
    }
}
