package org.asura.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.asura.ai.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    List<OperationLog> selectByUserId(String userId);
    List<OperationLog> selectByOperation(String operation);
}
