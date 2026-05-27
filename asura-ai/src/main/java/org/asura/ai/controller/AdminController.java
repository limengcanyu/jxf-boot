package org.asura.ai.controller;

import org.asura.ai.entity.OperationLog;
import org.asura.ai.entity.User;
import org.asura.ai.mapper.UserMapper;
import org.asura.ai.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<User> users = userMapper.selectList(null);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userMapper.selectById(id) != null) {
            userMapper.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/logs")
    public ResponseEntity<List<OperationLog>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<OperationLog> logs = operationLogService.getAllLogs(page, size);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<List<OperationLog>> getLogsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(operationLogService.getLogsByUserId(userId));
    }
}
