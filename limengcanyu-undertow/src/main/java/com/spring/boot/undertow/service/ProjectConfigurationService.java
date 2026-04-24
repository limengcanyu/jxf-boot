package com.spring.boot.undertow.service;

/**
 * 项目配置服务类，统一管理项目配置属性
 */
public interface ProjectConfigurationService {
    /**
     * 获取当前公司名称，定制化公司的功能
     *
     * @return
     */
    String getCurrentCompanyName();
}
