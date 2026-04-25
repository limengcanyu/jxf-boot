package org.asura.flaw.service;

import org.asura.flaw.dao.entity.ExampleTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rock.jiang
 * @since 2020-12-09
 */
public interface ExampleTableService extends IService<ExampleTable> {

    List<ExampleTable> getListByUserId(String userId);

}
