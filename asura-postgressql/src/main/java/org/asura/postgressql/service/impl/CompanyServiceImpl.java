package org.asura.postgressql.service.impl;

import org.asura.postgressql.dao.entity.Company;
import org.asura.postgressql.dao.mapper.CompanyMapper;
import org.asura.postgressql.service.ICompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rock.jiang
 * @since 2020-05-23
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

}
