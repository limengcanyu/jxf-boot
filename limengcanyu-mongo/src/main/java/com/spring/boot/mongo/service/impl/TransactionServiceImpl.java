package com.spring.boot.mongo.service.impl;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.spring.boot.mongo.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/04/11 13:36
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoClient client;

    @Autowired
    private MongoTransactionManager transactionManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transactionMethod1() throws Exception {
        Document employee = new Document();
        employee.put("tenant_id", "tenant_000001");
        employee.put("company_id", "company_000001");
        employee.put("salary_month", "2019-01");
        employee.put("employee_id", "employee_000001");
        employee.put("employee_name", "王一");
        employee.put("department_id", "department_000001");
        employee.put("department_name", "部门000001");

        Document iDoc = mongoTemplate.insert(employee, "artanis");
        log.debug("iDoc: {}", iDoc);

        Criteria criteria = Criteria.where("tenant_id").is("tenant_000001").and("company_id").is("company_000001");

        // 事务中先前的 insert 记录也会 count
        long count = mongoTemplate.count(new Query(criteria), "artanis");
        log.debug("count: {}", count);

//        throw new Exception();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transactionMethod2() throws Exception {
        ClientSessionOptions sessionOptions = ClientSessionOptions.builder()
                .causallyConsistent(true)
                .build();

        ClientSession session = client.startSession(sessionOptions);

        mongoTemplate.withSession(() -> session).execute(action -> {
            Document employee = new Document();
            employee.put("tenant_id", "tenant_000001");
            employee.put("company_id", "company_000001");
            employee.put("salary_month", "2019-01");
            employee.put("employee_id", "employee_000001");
            employee.put("employee_name", "王一");
            employee.put("department_id", "department_000001");
            employee.put("department_name", "部门000001");

            Document iDoc = mongoTemplate.insert(employee, "artanis");
            log.debug("iDoc: {}", iDoc);

            Criteria criteria = Criteria.where("tenant_id").is("tenant_000001").and("company_id").is("company_000001");

            // 事务中先前的 insert 记录也会 count
            long count = mongoTemplate.count(new Query(criteria), "artanis");
            log.debug("count: {}", count);

            // 再执行一次新增，count

            employee = new Document();
            employee.put("tenant_id", "tenant_000001");
            employee.put("company_id", "company_000001");
            employee.put("salary_month", "2019-02");
            employee.put("employee_id", "employee_000002");
            employee.put("employee_name", "王一");
            employee.put("department_id", "department_000002");
            employee.put("department_name", "部门000002");

            iDoc = mongoTemplate.insert(employee, "artanis");
            log.debug("iDoc: {}", iDoc);

            // 事务中先前的 insert 记录也会 count
            count = mongoTemplate.count(new Query(criteria), "artanis");
            log.debug("count: {}", count);

            return iDoc;
        });

        throw new Exception();

//        session.close();
    }

    @Override
    public void transactionMethod3() throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // explicitly setting the transaction name is something that can be done only programmatically
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            // put your business logic here
            Document employee = new Document();
            employee.put("tenant_id", "tenant_000001");
            employee.put("company_id", "company_000001");
            employee.put("salary_month", "2019-01");
            employee.put("employee_id", "employee_000001");
            employee.put("employee_name", "王一");
            employee.put("department_id", "department_000001");
            employee.put("department_name", "部门000001");

            Document iDoc = mongoTemplate.insert(employee, "artanis");
            log.debug("iDoc: {}", iDoc);

//            throw new Exception();
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        }

        transactionManager.commit(status);
    }

    @Override
    public void select() {
        List<Document> documentList = mongoTemplate.findAll(Document.class, "artanis");
    }
}
