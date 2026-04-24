package com.spring.boot.multi.datasource.transaction.service.impl;

import com.spring.boot.multi.datasource.transaction.dao.entity.PlatformSalaryItem;
import com.spring.boot.multi.datasource.transaction.service.IPlatformSalaryItemService;
import com.spring.boot.multi.datasource.transaction.service.MysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class MysqlServiceImpl implements MysqlService {

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private IPlatformSalaryItemService platformSalaryItemService;

    @Override
    public String saveMysqlRecord() throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // explicitly setting the transaction name is something that can be done only programmatically
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // put your business logic here
            PlatformSalaryItem item = new PlatformSalaryItem();
            item.setItemCode("item_134");
            item.setItemName("item_134");
            platformSalaryItemService.save(item);

//            throw new Exception();
        } catch (Exception ex) {
            dataSourceTransactionManager.rollback(status);
            throw ex;
        }

        dataSourceTransactionManager.commit(status);
        return null;
    }
}
