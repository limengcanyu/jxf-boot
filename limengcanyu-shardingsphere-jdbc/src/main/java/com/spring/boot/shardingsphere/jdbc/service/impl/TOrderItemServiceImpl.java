package com.spring.boot.shardingsphere.jdbc.service.impl;

import com.spring.boot.shardingsphere.jdbc.dao.entity.TOrderItem;
import com.spring.boot.shardingsphere.jdbc.dao.mapper.TOrderItemMapper;
import com.spring.boot.shardingsphere.jdbc.service.ITOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock.jiang
 * @since 2020-09-02
 */
@Service
public class TOrderItemServiceImpl extends ServiceImpl<TOrderItemMapper, TOrderItem> implements ITOrderItemService {

}
