package com.spring.boot.flowable.controller;

import com.spring.boot.flowable.service.MyService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author rock.jxf
 * @date 2020/6/2 23:07
 */
@RestController
public class MyRestController {

    @Autowired
    private MyService myService;

    /**
     * http://localhost:8090/process/test-process
     */
    @GetMapping(value="/process/{processName}")
    public void startProcessInstance(@PathVariable String processName) {
        myService.startProcess(processName);
    }

    /**
     * http://localhost:8090/tasks?assignee=kermit
     *
     * @param assignee
     * @return
     */
    @RequestMapping(value="/tasks", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = myService.getTasks(assignee);
        List<TaskRepresentation> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
        }
        return dtos;
    }

    static class TaskRepresentation {

        private String id;
        private String name;

        public TaskRepresentation(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

    }

}
