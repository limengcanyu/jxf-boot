package org.asura.activiti.service;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 工作流管理服务接口
 * 提供流程定义、流程实例的管理功能
 * 
 * @author Auto Generated
 * @version 1.0
 */
public interface IWorkflowService {

    /**
     * 部署流程定义
     * 
     * @param processName 流程名称
     * @param fileName 文件名
     * @param inputStream 流程文件输入流
     * @return 部署ID
     */
    String deployProcess(String processName, String fileName, InputStream inputStream);

    /**
     * 部署流程定义 (简化版本)
     * 
     * @param processName 流程文件名
     * @return 部署ID
     */
    String deployProcess(String processName);

    /**
     * 根据资源路径部署流程
     * 
     * @param processName 流程名称
     * @param resourcePath 资源路径
     * @return 部署ID
     */
    String deployProcessFromClasspath(String processName, String resourcePath);

    /**
     * 启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    String startProcess(String processDefinitionKey, Map<String, Object> variables);

    /**
     * 根据业务Key启动流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @param businessKey 业务Key
     * @param variables 流程变量
     * @return 流程实例ID
     */
    String startProcessWithBusinessKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * 获取所有流程定义
     * 
     * @return 流程定义列表
     */
    List<ProcessDefinition> getAllProcessDefinitions();

    /**
     * 根据Key获取最新版本的流程定义
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程定义
     */
    ProcessDefinition getLatestProcessDefinition(String processDefinitionKey);

    /**
     * 获取正在运行的流程实例
     * 
     * @return 流程实例列表
     */
    List<ProcessInstance> getActiveProcessInstances();

    /**
     * 根据流程定义Key获取运行中的流程实例
     * 
     * @param processDefinitionKey 流程定义Key
     * @return 流程实例列表
     */
    List<ProcessInstance> getActiveProcessInstancesByKey(String processDefinitionKey);

    /**
     * 根据业务Key获取流程实例
     * 
     * @param businessKey 业务Key
     * @return 流程实例
     */
    ProcessInstance getProcessInstanceByBusinessKey(String businessKey);

    /**
     * 挂起流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    void suspendProcessInstance(String processInstanceId);

    /**
     * 激活流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    void activateProcessInstance(String processInstanceId);

    /**
     * 删除流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 删除流程定义
     * 
     * @param deploymentId 部署ID
     * @param cascade 是否级联删除
     */
    void deleteProcessDefinition(String deploymentId, boolean cascade);

    /**
     * 获取流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程变量Map
     */
    Map<String, Object> getProcessVariables(String processInstanceId);

    /**
     * 设置流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @param variables 变量Map
     */
    void setProcessVariables(String processInstanceId, Map<String, Object> variables);
}