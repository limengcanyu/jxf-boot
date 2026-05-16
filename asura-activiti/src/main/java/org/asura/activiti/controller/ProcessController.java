package org.asura.activiti.controller;

import org.asura.activiti.entity.ProcessDefinitionDTO;
import org.asura.activiti.entity.ProcessInstanceDTO;
import org.asura.activiti.entity.TaskDTO;
import org.asura.activiti.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @GetMapping("/definitions")
    public ResponseEntity<List<ProcessDefinitionDTO>> getProcessDefinitions() {
        logger.info("Fetching all process definitions");
        List<ProcessDefinitionDTO> definitions = processService.getProcessDefinitions();
        return ResponseEntity.ok(definitions);
    }

    @GetMapping("/definitions/{processDefinitionId}/diagram")
    public ResponseEntity<byte[]> getProcessDiagram(@PathVariable String processDefinitionId) {
        logger.info("Fetching diagram for process definition: {}", processDefinitionId);
        byte[] diagram = processService.getProcessDiagram(processDefinitionId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(diagram.length);
        
        return new ResponseEntity<>(diagram, headers, HttpStatus.OK);
    }

    @PostMapping("/instances")
    public ResponseEntity<Map<String, Object>> startProcess(@RequestBody Map<String, Object> request) {
        String processDefinitionKey = (String) request.get("processDefinitionKey");
        String businessKey = (String) request.get("businessKey");
        @SuppressWarnings("unchecked")
        Map<String, Object> variables = (Map<String, Object>) request.getOrDefault("variables", new HashMap<>());
        
        logger.info("Starting process: {} with businessKey: {}", processDefinitionKey, businessKey);
        
        String processInstanceId = processService.startProcess(processDefinitionKey, businessKey, variables);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("processInstanceId", processInstanceId);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/instances/{processInstanceId}")
    public ResponseEntity<ProcessInstanceDTO> getProcessInstance(@PathVariable String processInstanceId) {
        logger.info("Fetching process instance: {}", processInstanceId);
        ProcessInstanceDTO instance = processService.getProcessInstance(processInstanceId);
        
        if (instance == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(instance);
    }

    @GetMapping("/instances")
    public ResponseEntity<List<ProcessInstanceDTO>> getProcessInstances(
            @RequestParam(required = false) String processDefinitionKey) {
        logger.info("Fetching process instances for key: {}", processDefinitionKey);
        List<ProcessInstanceDTO> instances = processService.getProcessInstances(processDefinitionKey);
        return ResponseEntity.ok(instances);
    }

    @DeleteMapping("/instances/{processInstanceId}")
    public ResponseEntity<Map<String, Object>> deleteProcessInstance(
            @PathVariable String processInstanceId,
            @RequestParam(required = false, defaultValue = "Deleted by user") String deleteReason) {
        logger.info("Deleting process instance: {} with reason: {}", processInstanceId, deleteReason);
        
        processService.deleteProcessInstance(processInstanceId, deleteReason);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Process instance deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks/assignee/{assignee}")
    public ResponseEntity<List<TaskDTO>> getTasksByAssignee(@PathVariable String assignee) {
        logger.info("Fetching tasks for assignee: {}", assignee);
        List<TaskDTO> tasks = processService.getTasksByAssignee(assignee);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/process/{processInstanceId}")
    public ResponseEntity<List<TaskDTO>> getTasksByProcessInstanceId(@PathVariable String processInstanceId) {
        logger.info("Fetching tasks for process instance: {}", processInstanceId);
        List<TaskDTO> tasks = processService.getTasksByProcessInstanceId(processInstanceId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable String taskId) {
        logger.info("Fetching task: {}", taskId);
        TaskDTO task = processService.getTask(taskId);
        
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        logger.info("Completing task: {}", taskId);
        
        processService.completeTask(taskId, variables);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Task completed successfully");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tasks/{taskId}/claim")
    public ResponseEntity<Map<String, Object>> claimTask(
            @PathVariable String taskId,
            @RequestParam String userId) {
        logger.info("Claiming task: {} for user: {}", taskId, userId);
        
        processService.claimTask(taskId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Task claimed successfully");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tasks/{taskId}/delegate")
    public ResponseEntity<Map<String, Object>> delegateTask(
            @PathVariable String taskId,
            @RequestParam String userId) {
        logger.info("Delegating task: {} to user: {}", taskId, userId);
        
        processService.delegateTask(taskId, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Task delegated successfully");
        
        return ResponseEntity.ok(response);
    }
}