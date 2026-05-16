package org.asura.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.asura.activiti.entity.ProcessDefinitionDTO;
import org.asura.activiti.entity.ProcessInstanceDTO;
import org.asura.activiti.entity.TaskDTO;
import org.asura.activiti.service.ProcessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProcessServiceTest {

    @Autowired
    private ProcessService processService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private static final String PROCESS_KEY = "leaveRequest";
    private String deploymentId;

    @BeforeEach
    public void setUp() {
        cleanupRunningProcesses();
        deployProcessDefinition();
    }

    @AfterEach
    public void tearDown() {
        cleanupRunningProcesses();
        if (deploymentId != null) {
            try {
                repositoryService.deleteDeployment(deploymentId, true);
            } catch (Exception e) {
                // Deployment may have already been deleted
            }
        }
    }

    private void deployProcessDefinition() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/leaveRequest.bpmn20.xml")
                .deploy();
        deploymentId = deployment.getId();
    }

    private void cleanupRunningProcesses() {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance instance : instances) {
            runtimeService.deleteProcessInstance(instance.getId(), "Test cleanup");
        }
    }

    @Test
    @DisplayName("测试获取流程定义列表")
    public void testGetProcessDefinitions() {
        List<ProcessDefinitionDTO> definitions = processService.getProcessDefinitions();
        assertNotNull(definitions);
        assertFalse(definitions.isEmpty(), "Should have at least one process definition");

        ProcessDefinitionDTO leaveRequest = definitions.stream()
                .filter(d -> PROCESS_KEY.equals(d.getKey()))
                .findFirst()
                .orElse(null);

        assertNotNull(leaveRequest, "Leave request process should be deployed");
        assertEquals(PROCESS_KEY, leaveRequest.getKey());
    }

    @Test
    @DisplayName("测试启动流程实例")
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");
        variables.put("employeeName", "John Doe");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-001", variables);

        assertNotNull(processInstanceId);
        assertFalse(processInstanceId.isEmpty());

        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNotNull(instance);
        assertEquals("LEAVE-TEST-001", instance.getBusinessKey());
        assertEquals("manager1", runtimeService.getVariable(processInstanceId, "manager"));
    }

    @Test
    @DisplayName("测试获取流程实例")
    public void testGetProcessInstance() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-002", variables);

        ProcessInstanceDTO instanceDTO = processService.getProcessInstance(processInstanceId);

        assertNotNull(instanceDTO);
        assertEquals(processInstanceId, instanceDTO.getProcessInstanceId());
        assertEquals("RUNNING", instanceDTO.getStatus());
        assertEquals("manager1", instanceDTO.getVariables().get("manager"));
    }

    @Test
    @DisplayName("测试查询待办任务")
    public void testGetTasksByAssignee() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "testManager");
        variables.put("hr", "testHr");

        processService.startProcess(PROCESS_KEY, "LEAVE-TEST-003", variables);

        List<TaskDTO> tasks = processService.getTasksByAssignee("testManager");

        assertFalse(tasks.isEmpty());
        TaskDTO task = tasks.getFirst();
        assertEquals("Manager Approval", task.getName());
        assertEquals("testManager", task.getAssignee());
    }

    @Test
    @DisplayName("测试领取任务")
    public void testClaimTask() {
        Task standaloneTask = taskService.newTask();
        standaloneTask.setName("Standalone Task");
        taskService.saveTask(standaloneTask);

        String taskId = standaloneTask.getId();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        assertNotNull(task);
        assertNull(task.getAssignee());

        processService.claimTask(taskId, "newAssignee");

        Task claimedTask = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        assertEquals("newAssignee", claimedTask.getAssignee());

        taskService.deleteTask(taskId, true);
    }

    @Test
    @DisplayName("测试完成任务")
    public void testCompleteTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-005", variables);

        Task managerTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee("manager1")
                .singleResult();

        assertNotNull(managerTask);

        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("approved", true);
        taskVariables.put("comment", "Approved by manager");

        processService.completeTask(managerTask.getId(), taskVariables);

        Task hrTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee("hr1")
                .singleResult();

        assertNotNull(hrTask);
        assertEquals("HR Approval", hrTask.getName());
    }

    @Test
    @DisplayName("测试流程变量设置与获取")
    public void testProcessVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");
        variables.put("employeeName", "Alice");
        variables.put("days", 5);

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-006", variables);

        ProcessInstanceDTO instanceDTO = processService.getProcessInstance(processInstanceId);

        assertNotNull(instanceDTO.getVariables());
        assertEquals("Alice", instanceDTO.getVariables().get("employeeName"));
        assertEquals(5, instanceDTO.getVariables().get("days"));
    }

    @Test
    @DisplayName("测试删除流程实例")
    public void testDeleteProcessInstance() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-007", variables);

        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNotNull(instance);

        processService.deleteProcessInstance(processInstanceId, "Test deletion");

        instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNull(instance);
    }

    @Test
    @DisplayName("测试委托任务")
    public void testDelegateTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-008", variables);

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNotNull(task);

        processService.delegateTask(task.getId(), "delegateUser");

        Task delegatedTask = taskService.createTaskQuery()
                .taskId(task.getId())
                .singleResult();

        assertEquals("delegateUser", delegatedTask.getAssignee());
    }

    @Test
    @DisplayName("测试完整流程执行 - 审批通过")
    public void testFullProcessApproval() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");
        variables.put("employeeName", "Test User");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-FULL-001", variables);

        Task managerTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee("manager1")
                .singleResult();

        assertNotNull(managerTask);
        assertEquals("Manager Approval", managerTask.getName());

        processService.completeTask(managerTask.getId(), Map.of("approved", true));

        Task hrTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee("hr1")
                .singleResult();

        assertNotNull(hrTask);
        assertEquals("HR Approval", hrTask.getName());

        processService.completeTask(hrTask.getId(), Map.of("approved", true));

        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNull(instance);
    }

    @Test
    @DisplayName("测试完整流程执行 - 审批拒绝")
    public void testFullProcessRejection() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-FULL-002", variables);

        Task managerTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee("manager1")
                .singleResult();

        assertNotNull(managerTask);

        processService.completeTask(managerTask.getId(), Map.of("approved", false));

        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNull(instance);
    }

    @Test
    @DisplayName("测试查询流程实例列表")
    public void testGetProcessInstances() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        processService.startProcess(PROCESS_KEY, "LEAVE-TEST-LIST-001", variables);
        processService.startProcess(PROCESS_KEY, "LEAVE-TEST-LIST-002", variables);

        List<ProcessInstanceDTO> instances = processService.getProcessInstances(PROCESS_KEY);

        assertEquals(2, instances.size());
    }

    @Test
    @DisplayName("测试获取单个任务")
    public void testGetTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("manager", "manager1");
        variables.put("hr", "hr1");

        String processInstanceId = processService.startProcess(PROCESS_KEY, "LEAVE-TEST-TASK-001", variables);

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        assertNotNull(task);

        TaskDTO taskDTO = processService.getTask(task.getId());

        assertNotNull(taskDTO);
        assertEquals(task.getId(), taskDTO.getTaskId());
        assertEquals(task.getName(), taskDTO.getName());
    }
}