package com.spring.boot.postgres.service.impl;

import com.spring.boot.postgres.dao.entity.Company;
import com.spring.boot.postgres.dao.mapper.CompanyMapper;
import com.spring.boot.postgres.service.ICompanyService;
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
