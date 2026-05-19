package org.asura.activiti;

import org.activiti.engine.RepositoryService;
import org.asura.activiti.entity.TaskDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProcessControllerHttpTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RepositoryService repositoryService;

    private static String processInstanceId;
    private static String taskId;

    @BeforeEach
    void setup() {
        if (repositoryService.createProcessDefinitionQuery().processDefinitionKey("leaveRequest").count() == 0) {
            repositoryService.createDeployment()
                    .addClasspathResource("processes/leaveRequest.bpmn20.xml")
                    .deploy();
        }
    }

    @Test
    @Order(1)
    @DisplayName("HTTP测试 - 获取流程定义列表")
    public void testGetProcessDefinitions() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/process/definitions", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("leaveRequest"));
    }

    @Test
    @Order(2)
    @DisplayName("HTTP测试 - 启动流程实例")
    public void testStartProcess() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("processDefinitionKey", "leaveRequest");
        requestBody.put("businessKey", "HTTP-TEST-001");
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");
        requestBody.put("variables", variables);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/instances",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().get("status"));
        
        processInstanceId = (String) response.getBody().get("processInstanceId");
        assertNotNull(processInstanceId);
    }

    @Test
    @Order(3)
    @DisplayName("HTTP测试 - 查询流程实例")
    public void testGetProcessInstance() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/instances/" + processInstanceId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(processInstanceId, response.getBody().get("processInstanceId"));
    }

    @Test
    @Order(4)
    @DisplayName("HTTP测试 - 查询待办任务列表")
    public void testGetTasks() {
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "/api/process/tasks/assignee/manager1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        
        Map<String, Object> task = response.getBody().getFirst();
        taskId = (String) task.get("taskId");
        assertNotNull(taskId);
    }

    @Test
    @Order(5)
    @DisplayName("HTTP测试 - 获取任务详情")
    public void testGetTask() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/tasks/" + taskId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(taskId, response.getBody().get("taskId"));
    }

    @Test
    @Order(6)
    @DisplayName("HTTP测试 - 完成任务（审批通过）")
    public void testCompleteTask() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("approved", true);
        requestBody.put("comment", "Approved by manager");

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/tasks/" + taskId + "/complete",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().get("status"));
    }

    @Test
    @Order(7)
    @DisplayName("HTTP测试 - 查询HR待办任务")
    public void testGetHrTasks() {
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "/api/process/tasks/assignee/hr1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        
        Map<String, Object> hrTask = response.getBody().getFirst();
        taskId = (String) hrTask.get("taskId");
        assertNotNull(taskId);
    }

    @Test
    @Order(8)
    @DisplayName("HTTP测试 - 完成HR任务")
    public void testCompleteHrTask() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("approved", true);
        requestBody.put("comment", "HR approved");

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/tasks/" + taskId + "/complete",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().get("status"));
    }

    @Test
    @Order(9)
    @DisplayName("HTTP测试 - 验证流程已结束")
    public void testProcessEnded() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/instances/" + processInstanceId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("COMPLETED", response.getBody().get("status"));
    }

    @Test
    @Order(10)
    @DisplayName("HTTP测试 - 启动流程实例并拒绝")
    public void testStartProcessAndReject() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("processDefinitionKey", "leaveRequest");
        requestBody.put("businessKey", "HTTP-TEST-REJECT");
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager2");
        variables.put("hr", "hr2");
        requestBody.put("variables", variables);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/instances",
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        String rejectProcessInstanceId = (String) response.getBody().get("processInstanceId");

        ResponseEntity<List<Map<String, Object>>> taskResponse = restTemplate.exchange(
                "/api/process/tasks/assignee/manager2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        
        Map<String, Object> rejectTask = taskResponse.getBody().get(0);
        String rejectTaskId = (String) rejectTask.get("taskId");

        Map<String, Object> completeBody = new HashMap<>();
        completeBody.put("approved", false);
        completeBody.put("comment", "Rejected");

        restTemplate.exchange(
                "/api/process/tasks/" + rejectTaskId + "/complete",
                HttpMethod.POST,
                new HttpEntity<>(completeBody),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        ResponseEntity<Map<String, Object>> instanceResponse = restTemplate.exchange(
                "/api/process/instances/" + rejectProcessInstanceId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertNotNull(instanceResponse.getBody());
        assertEquals("COMPLETED", instanceResponse.getBody().get("status"));
    }

    @Test
    @Order(11)
    @DisplayName("HTTP测试 - 查询不存在的流程实例")
    public void testGetNonExistentProcessInstance() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/instances/nonexistent-id",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(12)
    @DisplayName("HTTP测试 - 查询不存在的任务")
    public void testGetNonExistentTask() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/api/process/tasks/nonexistent-task-id",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(13)
    @DisplayName("HTTP测试 - 获取流程实例列表")
    public void testGetProcessInstances() {
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "/api/process/instances?processDefinitionKey=leaveRequest",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}