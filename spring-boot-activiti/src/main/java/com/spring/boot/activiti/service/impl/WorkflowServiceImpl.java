package com.spring.boot.activiti.service.impl;

import com.spring.boot.activiti.service.IWorkflowService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 工作流管理服务实现类
 * 提供流程定义、流程实例的管理功能
 * 
 * @author Auto Generated
 * @version 1.0
 */
@Service
public class WorkflowServiceImpl implements IWorkflowService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 部署流程定义
     * 
     * @param processName 流程名称
     * @param fileName 文件名
     * @param inputStream 流程文件输入流
     * @return 部署ID
     */
    public String deployProcess(String processName, String fileName, InputStream inputStream) {
        Deployment deployment = repositoryService.createDeployment()
                .name(processName)
                .addInputStream(fileName, inputStream)
                .deploy();
        return deployment.getId();
    }

    /**
     * 部署流程定义 (简化版本)
     * 
     * @param processName 流程文件名
     * @return 部署ID
     */
    public String deployProcess(String processName) {
        // 从 classpath 中部署流程文件
        String resourcePath = "processes/" + processName;
        return deployProcessFromClasspath(processName, resourcePath);
    }

    /**
     * 根据资源路径部署流程
     * 
     * @param processName 流程名称
     * @param resourcePath 资源路径
     * @return 部署ID
     */
    public String deployProcessFromClasspath(String processName, String resourcePath) {
        Deployment deployment = repositoryService.createDeployment()
                .name(processName)
                .addClasspathResource(resourcePath)
                .deploy();
        return deployment.getId();
    }

    /**
     * 启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    public String startProcess(String processDefinitionKey, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        return processInstance.getId();
    }

    /**
     * 根据业务Key启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    public String startProcessWithBusinessKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        return processInstance.getId();
    }

    /**
     * 获取所有流程定义
     * 
     * @return 流程定义列表
     */
    public List<ProcessDefinition> getAllProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery().list();
    }

    /**
     * 根据Key获取最新版本的流程定义
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程定义
     */
    public ProcessDefinition getLatestProcessDefinition(String processDefinitionKey) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();
    }

    /**
     * 获取正在运行的流程实例
     * 
     * @return 流程实例列表
     */
    public List<ProcessInstance> getActiveProcessInstances() {
        return runtimeService.createProcessInstanceQuery().active().list();
    }

    /**
     * 根据流程定义Key获取运行中的流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程实例列表
     */
    public List<ProcessInstance> getActiveProcessInstancesByKey(String processDefinitionKey) {
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .active()
                .list();
    }

    /**
     * 根据业务Key获取流程实例
     * 
     * @param businessKey 业务Key
     * @return 流程实例
     */
    public ProcessInstance getProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
    }

    /**
     * 挂起流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 激活流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 删除流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    /**
     * 删除流程定义
     * 
     * @param deploymentId 部署ID
     * @param cascade 是否级联删除
     */
    public void deleteProcessDefinition(String deploymentId, boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    /**
     * 获取流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程变量Map
     */
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }

    /**
     * 设置流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @param variables 变量Map
     */
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }
}