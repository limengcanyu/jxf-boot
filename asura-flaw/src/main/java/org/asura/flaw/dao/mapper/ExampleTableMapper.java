package org.asura.flaw.dao.mapper;

import org.asura.flaw.dao.entity.ExampleTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rock.jiang
 * @since 2020-12-09
 */
public interface ExampleTableMapper extends BaseMapper<ExampleTable> {

    List<ExampleTable> getListByUserId(String userId);

}
