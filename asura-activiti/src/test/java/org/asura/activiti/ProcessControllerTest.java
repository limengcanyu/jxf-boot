package org.asura.activiti;

import org.asura.activiti.config.ActivitiConfig;
import org.asura.activiti.entity.ProcessInstanceDTO;
import org.asura.activiti.entity.TaskDTO;
import org.asura.activiti.service.ProcessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(ProcessControllerTest.TestConfig.class)
public class ProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProcessService processService;

    public static class TestConfig {
        @org.springframework.context.annotation.Bean
        public ProcessService processService() {
            return org.mockito.Mockito.mock(ProcessService.class);
        }
        
        @org.springframework.context.annotation.Bean
        public ActivitiConfig activitiConfig() {
            return org.mockito.Mockito.mock(ActivitiConfig.class);
        }
    }

    @Test
    @DisplayName("测试获取流程定义列表 API")
    public void testGetProcessDefinitions() throws Exception {
        mockMvc.perform(get("/api/process/definitions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("测试启动流程实例 API")
    public void testStartProcess() throws Exception {
        when(processService.startProcess(eq("leaveRequest"), eq("TEST-001"), any(Map.class)))
                .thenReturn("process-123");

        String requestBody = """
                {
                    "processDefinitionKey": "leaveRequest",
                    "businessKey": "TEST-001",
                    "variables": {
                        "manager": "manager1",
                        "hr": "hr1"
                    }
                }
                """;

        mockMvc.perform(post("/api/process/instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.processInstanceId").value("process-123"));
    }

    @Test
    @DisplayName("测试获取流程实例 API - 找到")
    public void testGetProcessInstanceFound() throws Exception {
        ProcessInstanceDTO instanceDTO = new ProcessInstanceDTO();
        instanceDTO.setProcessInstanceId("test-process-id");
        instanceDTO.setStatus("RUNNING");

        when(processService.getProcessInstance("test-process-id")).thenReturn(instanceDTO);

        mockMvc.perform(get("/api/process/instances/test-process-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.processInstanceId").value("test-process-id"));
    }

    @Test
    @DisplayName("测试获取流程实例 API - 未找到")
    public void testGetProcessInstanceNotFound() throws Exception {
        when(processService.getProcessInstance("test-process-id")).thenReturn(null);

        mockMvc.perform(get("/api/process/instances/test-process-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("测试删除流程实例 API")
    public void testDeleteProcessInstance() throws Exception {
        mockMvc.perform(delete("/api/process/instances/test-process-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("测试查询待办任务 API")
    public void testGetTasksByAssignee() throws Exception {
        mockMvc.perform(get("/api/process/tasks/assignee/manager1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("测试获取单个任务 API - 找到")
    public void testGetTaskFound() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId("test-task-id");
        taskDTO.setName("Test Task");

        when(processService.getTask("test-task-id")).thenReturn(taskDTO);

        mockMvc.perform(get("/api/process/tasks/test-task-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value("test-task-id"))
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    @DisplayName("测试获取单个任务 API - 未找到")
    public void testGetTaskNotFound() throws Exception {
        when(processService.getTask("test-task-id")).thenReturn(null);

        mockMvc.perform(get("/api/process/tasks/test-task-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("测试完成任务 API")
    public void testCompleteTask() throws Exception {
        String requestBody = """
                {
                    "approved": true,
                    "comment": "Approved"
                }
                """;

        mockMvc.perform(post("/api/process/tasks/test-task-id/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("测试领取任务 API")
    public void testClaimTask() throws Exception {
        mockMvc.perform(post("/api/process/tasks/test-task-id/claim")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("测试委托任务 API")
    public void testDelegateTask() throws Exception {
        mockMvc.perform(post("/api/process/tasks/test-task-id/delegate")
                        .param("userId", "delegateUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("测试获取流程实例列表 API")
    public void testGetProcessInstances() throws Exception {
        mockMvc.perform(get("/api/process/instances")
                        .param("processDefinitionKey", "leaveRequest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}