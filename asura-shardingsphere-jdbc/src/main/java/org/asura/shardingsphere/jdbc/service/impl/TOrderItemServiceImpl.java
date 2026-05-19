package org.asura.shardingsphere.jdbc.service.impl;

import org.asura.shardingsphere.jdbc.dao.entity.TOrderItem;
import org.asura.shardingsphere.jdbc.dao.mapper.TOrderItemMapper;
import org.asura.shardingsphere.jdbc.service.ITOrderItemService;
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
