package com.spring.boot.flowable;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * 对于像Flowable这样的BPM引擎来说，核心是需要人类用户操作的任务。所有任务相关的东西都组织在TaskService中，例如：
 *
 * 查询分派给用户或组的任务
 *
 * 创建独立运行(standalone)任务。这是一种没有关联到流程实例的任务。
 *
 * 决定任务的执行用户(assignee)，或者将用户通过某种方式与任务关联。
 *
 * 认领(claim)与完成(complete)任务。认领是指某人决定成为任务的执行用户，也即他将会完成这个任务。完成任务是指“做这个任务要求的工作”，通常是填写某个表单。
 *
 * @author rock.jxf
 * @date 2020/8/15 17:23
 */
@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void createTaskQuery() {
        System.out.println("all-------------------------------------");
        List<Task> taskList = taskService.createTaskQuery().list();
        for (Task task : taskList) {
            System.out.println(task);
        }

        System.out.println("suspended-------------------------------------");
        taskList = taskService.createTaskQuery().suspended().list();
        for (Task task : taskList) {
            System.out.println(task);
        }

        System.out.println("active-------------------------------------");
        taskList = taskService.createTaskQuery().active().list();
        for (Task task : taskList) {
            System.out.println(task);
        }

        System.out.println("taskUnassigned-------------------------------------");
        taskList = taskService.createTaskQuery().taskUnassigned().list();
        for (Task task : taskList) {
            System.out.println(task);
        }
    }
}
