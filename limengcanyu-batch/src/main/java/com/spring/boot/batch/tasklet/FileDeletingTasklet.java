package com.spring.boot.batch.tasklet;

import com.spring.boot.batch.listener.JobCompletionNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2019/2/25 0025
 */
public class FileDeletingTasklet implements Tasklet, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(FileDeletingTasklet.class);

    private Resource directory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.debug("==========FileDeletingTasklet execute==========");

        File dir = directory.getFile();
//        Assert.state(dir.isDirectory());

        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                boolean deleted = files[i].delete();
                if (!deleted) {
                    throw new UnexpectedJobExecutionException("Could not delete file " +
                            files[i].getPath());
                }
            }
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("execute afterPropertiesSet");
        Assert.notNull(directory, "directory must be set");
    }

    public void setDirectoryResource(Resource directory) {
        this.directory = directory;
    }
}
