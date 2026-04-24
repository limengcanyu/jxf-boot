package com.spring.boot.data.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

@NoRepositoryBean
public interface CommonMongoRepository<T, ID extends Serializable> extends MongoRepository<T, ID> {

    Page<T> findPage(Query query, Pageable pageable);

    List<T> findList(Query query);

    long count(Query query);

    T findOne(Query query);

    boolean exists(Query query);

    <U> List<U> findDistinct(Query query, String field, Class<U> resultClass);

    void foreach(Query query, Consumer<T> callback);

    void remove(Query query);

    <S extends T> S save(S entity);

}
