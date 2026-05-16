package org.asura.batch.tasklet;

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
        if (!dir.exists()) {
            logger.warn("Directory {} does not exist, skipping deletion", dir.getPath());
            return RepeatStatus.FINISHED;
        }

        Assert.state(dir.isDirectory(), "Resource must be a directory");

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean deleted = file.delete();
                if (!deleted) {
                    throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
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