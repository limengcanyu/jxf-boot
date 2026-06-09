package org.asura.flaw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.asura.flaw.dao.entity.ExampleTable;
import org.asura.flaw.dao.mapper.ExampleTableMapper;
import org.asura.flaw.service.ExampleTableService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author rock.jiang
 * @since 2020-12-09
 */
@Slf4j
@Service
public class ExampleTableServiceImpl extends ServiceImpl<ExampleTableMapper, ExampleTable> implements ExampleTableService {

    @Resource
    private ExampleTableMapper exampleTableMapper;

    @Override
    public List<ExampleTable> getListByUserId(String userId) {
        return exampleTableMapper.getListByUserId(userId);
    }

}
