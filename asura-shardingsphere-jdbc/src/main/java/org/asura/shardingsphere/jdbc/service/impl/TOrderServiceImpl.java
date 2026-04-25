package org.asura.shardingsphere.jdbc.service.impl;

import org.asura.shardingsphere.jdbc.dao.entity.TOrder;
import org.asura.shardingsphere.jdbc.dao.mapper.TOrderMapper;
import org.asura.shardingsphere.jdbc.service.ITOrderService;
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
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

}
