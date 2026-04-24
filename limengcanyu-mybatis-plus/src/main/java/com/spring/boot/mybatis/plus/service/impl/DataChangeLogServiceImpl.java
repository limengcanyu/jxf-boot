package com.spring.boot.mybatis.plus.service.impl;

import com.spring.boot.mybatis.plus.entity.DataChangeLog;
import com.spring.boot.mybatis.plus.mapper.DataChangeLogMapper;
import com.spring.boot.mybatis.plus.service.DataChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock
 * @since 2023-02-15
 */
@Service
public class DataChangeLogServiceImpl extends ServiceImpl<DataChangeLogMapper, DataChangeLog> implements DataChangeLogService {

}
