package org.asura.batch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JobLauncherController {

    private static final Logger logger = LoggerFactory.getLogger(JobLauncherController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Map<String, Job> jobs;

    @GetMapping("/launch/{jobName}")
    public Map<String, Object> launchJob(@PathVariable String jobName) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        if (!jobs.containsKey(jobName)) {
            result.put("status", "error");
            result.put("message", "Job not found: " + jobName);
            return result;
        }

        Job job = jobs.get(jobName);
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
            result.put("status", "success");
            result.put("message", "Job " + jobName + " launched successfully");
            logger.info("Job {} launched successfully", jobName);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Failed to launch job " + jobName + ": " + e.getMessage());
            logger.error("Failed to launch job {}", jobName, e);
        }

        return result;
    }

    @GetMapping("/jobs")
    public Map<String, Object> listJobs() {
        Map<String, Object> result = new HashMap<>();
        result.put("jobs", jobs.keySet());
        return result;
    }
}