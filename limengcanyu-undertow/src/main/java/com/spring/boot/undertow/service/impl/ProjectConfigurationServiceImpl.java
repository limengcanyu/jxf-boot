package com.spring.boot.undertow.service.impl;

import com.spring.boot.undertow.service.ProjectConfigurationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
public class ProjectConfigurationServiceImpl implements ProjectConfigurationService {

    @Value("${current.company.name}")
    private String currentCompanyName;

    @Override
    public String getCurrentCompanyName() {
        return currentCompanyName;
    }
}
