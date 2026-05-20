package org.asura.flowable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asura.flowable.dto.request.ProcessStartRequest;
import org.asura.flowable.dto.request.TaskCompleteRequest;
import org.asura.flowable.dto.request.TaskQueryRequest;
import org.asura.flowable.service.ProcessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProcessController HTTP请求测试类
 */
@WebMvcTest(ProcessController.class)
@Import(ProcessControllerTest.TestConfig.class)
class ProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProcessService processService;

    private String processInstanceId;
    private String taskId;
    private String processDefinitionId;

    @BeforeEach
    void setUp() {
        processInstanceId = "test-process-instance-id";
        taskId = "test-task-id";
        processDefinitionId = "test-process-definition-id";
    }

    @Test
    @DisplayName("POST /api/process/instances - 启动流程实例")
    void testStartProcess() throws Exception {
        ProcessStartRequest request = new ProcessStartRequest();
        request.setProcessDefinitionKey("leave-approval");
        request.setBusinessKey("BUSINESS-001");
        request.setInitiator("zhangsan");

        mockMvc.perform(post("/api/process/instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程启动成功"));
    }

    @Test
    @DisplayName("GET /api/process/instances/{id} - 查询流程实例详情")
    void testGetProcessInstance() throws Exception {
        mockMvc.perform(get("/api/process/instances/{processInstanceId}", processInstanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("PUT /api/process/instances/{id}/suspend - 挂起流程实例")
    void testSuspendProcess() throws Exception {
        doNothing().when(processService).suspendProcess(anyString());

        mockMvc.perform(put("/api/process/instances/{processInstanceId}/suspend", processInstanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程已挂起"));
    }

    @Test
    @DisplayName("PUT /api/process/instances/{id}/activate - 激活流程实例")
    void testActivateProcess() throws Exception {
        doNothing().when(processService).activateProcess(anyString());

        mockMvc.perform(put("/api/process/instances/{processInstanceId}/activate", processInstanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程已激活"));
    }

    @Test
    @DisplayName("DELETE /api/process/instances/{id} - 终止流程实例")
    void testTerminateProcess() throws Exception {
        doNothing().when(processService).terminateProcess(anyString(), anyString());

        mockMvc.perform(delete("/api/process/instances/{processInstanceId}", processInstanceId)
                        .param("reason", "测试终止"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程已终止"));
    }

    @Test
    @DisplayName("GET /api/process/instances/{id}/variables - 获取流程变量")
    void testGetProcessVariables() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicant", "zhangsan");
        variables.put("days", 3);
        when(processService.getProcessVariables(anyString())).thenReturn(variables);

        mockMvc.perform(get("/api/process/instances/{processInstanceId}/variables", processInstanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.applicant").value("zhangsan"))
                .andExpect(jsonPath("$.data.days").value(3));
    }

    @Test
    @DisplayName("PUT /api/process/instances/{id}/variables - 设置流程变量")
    void testSetProcessVariables() throws Exception {
        doNothing().when(processService).setProcessVariables(anyString(), anyMap());

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);

        mockMvc.perform(put("/api/process/instances/{processInstanceId}/variables", processInstanceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variables)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程变量已更新"));
    }

    @Test
    @DisplayName("POST /api/process/tasks/query - 查询待办任务列表")
    void testQueryTasks() throws Exception {
        TaskQueryRequest request = new TaskQueryRequest();
        request.setAssignee("zhangsan");

        mockMvc.perform(post("/api/process/tasks/query")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("GET /api/process/tasks/{id} - 获取任务详情")
    void testGetTask() throws Exception {
        mockMvc.perform(get("/api/process/tasks/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("GET /api/process/tasks/{id}/variables - 获取任务变量")
    void testGetTaskVariables() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("comment", "同意请假");
        when(processService.getTaskVariables(anyString())).thenReturn(variables);

        mockMvc.perform(get("/api/process/tasks/{taskId}/variables", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.comment").value("同意请假"));
    }

    @Test
    @DisplayName("PUT /api/process/tasks/{id}/claim - 签收任务")
    void testClaimTask() throws Exception {
        doNothing().when(processService).claimTask(anyString(), anyString());

        mockMvc.perform(put("/api/process/tasks/{taskId}/claim", taskId)
                        .param("assignee", "zhangsan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("任务签收成功"));
    }

    @Test
    @DisplayName("PUT /api/process/tasks/{id}/unclaim - 取消签收任务")
    void testUnclaimTask() throws Exception {
        doNothing().when(processService).unclaimTask(anyString());

        mockMvc.perform(put("/api/process/tasks/{taskId}/unclaim", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("任务已取消签收"));
    }

    @Test
    @DisplayName("POST /api/process/tasks/{id}/complete - 完成任务")
    void testCompleteTask() throws Exception {
        TaskCompleteRequest request = new TaskCompleteRequest();
        request.setAction("APPROVE");
        request.setComment("同意");

        mockMvc.perform(post("/api/process/tasks/{taskId}/complete", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("任务处理成功"));
    }

    @Test
    @DisplayName("PUT /api/process/tasks/{id}/delegate - 委派任务")
    void testDelegateTask() throws Exception {
        doNothing().when(processService).delegateTask(anyString(), anyString(), anyString());

        mockMvc.perform(put("/api/process/tasks/{taskId}/delegate", taskId)
                        .param("assignee", "lisi")
                        .param("comment", "请处理"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("任务委派成功"));
    }

    @Test
    @DisplayName("PUT /api/process/tasks/{id}/assign - 转办任务")
    void testAssignTask() throws Exception {
        doNothing().when(processService).assignTask(anyString(), anyString());

        mockMvc.perform(put("/api/process/tasks/{taskId}/assign", taskId)
                        .param("assignee", "wangwu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("任务转办成功"));
    }

    @Test
    @DisplayName("GET /api/process/definitions - 查询流程定义列表")
    void testQueryProcessDefinitions() throws Exception {
        mockMvc.perform(get("/api/process/definitions")
                        .param("processDefinitionKey", "leave-approval")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("GET /api/process/definitions/{id} - 获取流程定义详情")
    void testGetProcessDefinition() throws Exception {
        mockMvc.perform(get("/api/process/definitions/{processDefinitionId}", processDefinitionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("POST /api/process/definitions/deploy - 部署流程定义")
    void testDeployProcess() throws Exception {
        String bpmnXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL http://www.omg.org/spec/BPMN/2.0/20100501/BPMN20.xsd"
                    id="Definitions_1"
                    targetNamespace="http://flowable.org/processdef">
                    <process id="test-process" name="Test Process" isExecutable="true">
                        <startEvent id="start" />
                        <userTask id="task1" name="Task 1" />
                        <endEvent id="end" />
                        <sequenceFlow id="flow1" sourceRef="start" targetRef="task1" />
                        <sequenceFlow id="flow2" sourceRef="task1" targetRef="end" />
                    </process>
                </definitions>
                """;

        when(processService.deployProcess(anyString(), anyString())).thenReturn(processDefinitionId);

        mockMvc.perform(post("/api/process/definitions/deploy")
                        .param("bpmnXml", bpmnXml)
                        .param("processName", "Test Process"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程部署成功"));
    }

    @Test
    @DisplayName("PUT /api/process/definitions/{id}/suspend - 挂起流程定义")
    void testSuspendProcessDefinition() throws Exception {
        doNothing().when(processService).suspendProcessDefinition(anyString());

        mockMvc.perform(put("/api/process/definitions/{processDefinitionId}/suspend", processDefinitionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程定义已挂起"));
    }

    @Test
    @DisplayName("PUT /api/process/definitions/{id}/activate - 激活流程定义")
    void testActivateProcessDefinition() throws Exception {
        doNothing().when(processService).activateProcessDefinition(anyString());

        mockMvc.perform(put("/api/process/definitions/{processDefinitionId}/activate", processDefinitionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("流程定义已激活"));
    }

    @Test
    @DisplayName("GET /api/process/instances/{id}/history/tasks - 获取流程历史任务")
    void testGetProcessHistoryTasks() throws Exception {
        mockMvc.perform(get("/api/process/instances/{processInstanceId}/history/tasks", processInstanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    @DisplayName("GET /api/process/definitions/{id}/diagram - 获取流程图片")
    void testGetProcessDiagram() throws Exception {
        byte[] diagram = new byte[]{1, 2, 3};
        when(processService.getProcessDiagram(anyString())).thenReturn(diagram);

        mockMvc.perform(get("/api/process/definitions/{processDefinitionId}/diagram", processDefinitionId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"));
    }

    @Test
    @DisplayName("GET /api/process/instances/{id}/diagram - 获取流程实例状态图片")
    void testGetProcessInstanceDiagram() throws Exception {
        byte[] diagram = new byte[]{1, 2, 3};
        when(processService.getProcessInstanceDiagram(anyString())).thenReturn(diagram);

        mockMvc.perform(get("/api/process/instances/{processInstanceId}/diagram", processInstanceId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"));
    }

    /**
     * 测试配置类，提供Mock Bean
     */
    static class TestConfig {
        @Bean
        ProcessService processService() {
            return Mockito.mock(ProcessService.class);
        }
    }
}