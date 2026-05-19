package org.asura.activiti.service;

import org.asura.activiti.entity.ProcessDefinitionDTO;
import org.asura.activiti.entity.ProcessInstanceDTO;
import org.asura.activiti.entity.TaskDTO;

import java.util.List;
import java.util.Map;

public interface ProcessService {

    List<ProcessDefinitionDTO> getProcessDefinitions();
    
    byte[] getProcessDiagram(String processDefinitionId);
    
    String startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables);
    
    ProcessInstanceDTO getProcessInstance(String processInstanceId);
    
    List<ProcessInstanceDTO> getProcessInstances(String processDefinitionKey);
    
    void deleteProcessInstance(String processInstanceId, String deleteReason);
    
    List<TaskDTO> getTasksByAssignee(String assignee);
    
    List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId);
    
    TaskDTO getTask(String taskId);
    
    void completeTask(String taskId, Map<String, Object> variables);
    
    void claimTask(String taskId, String userId);
    
    void delegateTask(String taskId, String userId);
}