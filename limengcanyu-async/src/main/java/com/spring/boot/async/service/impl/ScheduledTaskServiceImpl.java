package com.spring.boot.async.service.impl;

import com.spring.boot.async.service.ScheduledTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {
    @Scheduled(fixedRate=1000)
    @Override
    public void task1() {
        System.out.println("当前时间: " + LocalDateTime.now());
    }
}
