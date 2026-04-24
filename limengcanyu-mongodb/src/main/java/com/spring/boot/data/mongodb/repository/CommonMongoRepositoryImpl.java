package com.spring.boot.data.mongodb.repository;

import com.spring.boot.data.mongodb.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class CommonMongoRepositoryImpl<T, ID extends Serializable> extends SimpleMongoRepository<T, ID> implements CommonMongoRepository<T, ID> {

    private final MongoOperations mongoOperations;
    private final Class<T> entityClass;

    public CommonMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.entityClass = metadata.getJavaType();
    }

    @Override
    public Page<T> findPage(Query query, Pageable pageable) {
        query.with(pageable);
        query.with(pageable.getSort());
        query.collation(Collation.of("zh")); // 支持中文排序
        List<T> list = mongoOperations.find(query, entityClass);
        long count = mongoOperations.count(query.limit(-1).skip(-1L), entityClass);
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<T> findList(Query query) {
        query.collation(Collation.of("zh"));
        return mongoOperations.find(query, entityClass);
    }

    @Override
    public long count(Query query) {
        return mongoOperations.count(query, entityClass);
    }

    @Override
    public T findOne(Query query) {
        return mongoOperations.findOne(query, entityClass);
    }

    @Override
    public boolean exists(Query query) {
        return mongoOperations.exists(query, entityClass);
    }

    @Override
    public <U> List<U> findDistinct(Query query, String field, Class<U> resultClass) {
        return mongoOperations.findDistinct(query, field, entityClass, resultClass);
    }

    @Override
    public void foreach(Query query, Consumer<T> callback) {
        mongoOperations.stream(query, entityClass).forEach(callback);
    }

    @Override
    public void remove(Query query) {
        mongoOperations.remove(query);
    }

    @Override
    public <S extends T> S save(S entity) {
        if (entity instanceof BaseEntity base) {
            base.setUpdateTime(new Date());
        }
        return super.save(entity);
    }
}
