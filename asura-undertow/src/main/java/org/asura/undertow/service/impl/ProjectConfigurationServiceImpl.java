package org.asura.undertow.service.impl;

import org.asura.undertow.service.ProjectConfigurationService;
import org.springframework.beans.factory.annotation.Value;
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
