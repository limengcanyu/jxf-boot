package org.asura.batch.config;

import org.asura.batch.entity.Person;
import org.asura.batch.listener.JobCompletionNotificationListener;
import org.asura.batch.processor.PersonItemProcessor;
import org.asura.batch.tasklet.FileDeletingTasklet;
import org.asura.batch.tasklet.TaskletStep1;
import org.asura.batch.tasklet.TaskletStep2;
import org.asura.batch.tasklet.TaskletStep3;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public FlatFileItemReader<Person> reader() {
        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);

        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(fieldSetMapper)
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

    @Bean
    public FileDeletingTasklet fileDeletingTasklet() {
        FileDeletingTasklet tasklet = new FileDeletingTasklet();
        tasklet.setDirectoryResource(new FileSystemResource("./target/test-outputs/test-dir"));
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

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1, JobRepository jobRepository) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(step1)
                .listener(listener)
                .build();
    }

    @Bean
    public Job taskletJob(JobRepository jobRepository, Step deleteFilesInDir) {
        return new JobBuilder("taskletJob", jobRepository)
                .start(deleteFilesInDir)
                .build();
    }

    @Bean
    public Job taskletStepJob(JobRepository jobRepository, Step taskletStep1Step, Step taskletStep2Step, Step taskletStep3Step) {
        return new JobBuilder("taskletStepJob", jobRepository)
                .start(taskletStep1Step)
                .next(taskletStep2Step)
                .next(taskletStep3Step)
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Step deleteFilesInDir(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteFilesInDir", jobRepository)
                .tasklet(fileDeletingTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step taskletStep1Step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("taskletStep1", jobRepository)
                .tasklet(taskletStep1(), transactionManager)
                .build();
    }

    @Bean
    public Step taskletStep2Step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("taskletStep2", jobRepository)
                .tasklet(taskletStep2(), transactionManager)
                .build();
    }

    @Bean
    public Step taskletStep3Step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("taskletStep3", jobRepository)
                .tasklet(taskletStep3(), transactionManager)
                .build();
    }
}