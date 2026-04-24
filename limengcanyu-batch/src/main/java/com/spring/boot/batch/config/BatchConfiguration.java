package com.spring.boot.batch.config;

import com.spring.boot.batch.entity.Person;
import com.spring.boot.batch.listener.JobCompletionNotificationListener;
import com.spring.boot.batch.processor.PersonItemProcessor;
import com.spring.boot.batch.tasklet.FileDeletingTasklet;
import com.spring.boot.batch.tasklet.TaskletStep1;
import com.spring.boot.batch.tasklet.TaskletStep2;
import com.spring.boot.batch.tasklet.TaskletStep3;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2019/2/22 0022
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[] Step的reade，rwriter，processor设置
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::Tasklet
    @Bean
    public FileDeletingTasklet fileDeletingTasklet() {
        FileDeletingTasklet tasklet = new FileDeletingTasklet();

        tasklet.setDirectoryResource(new FileSystemResource("spring-boot-batch/target/test-outputs/test-dir"));

        return tasklet;
    }

    @Bean
    public TaskletStep1 taskletStep1() {
        return new TaskletStep1();
    }

    @Bean
    public TaskletStep2 taskletStep2() {
        return new TaskletStep2();
    }

    @Bean
    public TaskletStep3 taskletStep3() {
        return new TaskletStep3();
    }
    // end::Tasklet

    // tag::jobstep[] job设置
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Job taskletJob() {
        return this.jobBuilderFactory.get("taskletJob")
                .start(deleteFilesInDir())
                .build();
    }

    /**
     * Sequential Flow
     *
     * @return
     */
    @Bean
    public Job taskletStepJob() {
        return this.jobBuilderFactory.get("taskletStepJob")
                .start(taskletStep1Step())
                .next(taskletStep2Step())
                .next(taskletStep3Step())
                .build();
    }

    /**
     * Chunk-oriented Processing
     *
     * @param writer
     * @return
     */
    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    /**
     * TaskletStep
     *
     * @return
     */
    @Bean
    public Step deleteFilesInDir() {
        return this.stepBuilderFactory.get("deleteFilesInDir")
                .tasklet(fileDeletingTasklet())
                .build();
    }

    @Bean
    public Step taskletStep1Step(){
        return this.stepBuilderFactory.get("taskletStep1")
                .tasklet(taskletStep1())
                .build();
    }

    @Bean
    public Step taskletStep2Step(){
        return this.stepBuilderFactory.get("taskletStep2")
                .tasklet(taskletStep2())
                .build();
    }

    @Bean
    public Step taskletStep3Step(){
        return this.stepBuilderFactory.get("taskletStep3")
                .tasklet(taskletStep3())
                .build();
    }
    // end::jobstep[]
}
