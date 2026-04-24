package com.spring.boot.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2019/2/25 0025
 */
public class TaskletStep3 implements Tasklet {
    private static final Logger logger = LoggerFactory.getLogger(TaskletStep3.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.debug("==========TaskletStep3 execute==========");
        return RepeatStatus.FINISHED;
    }
}
