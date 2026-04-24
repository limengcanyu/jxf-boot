package com.spring.boot.multi.datasource.transaction.service;

/**
 * <p>Description: </p>
 *
 * @author rock.jxf
 * @date 2020/12/5 0:47
 */
public interface AggregateService {
    String aggregate() throws Exception;

    String saveItemData() throws Exception;
}
