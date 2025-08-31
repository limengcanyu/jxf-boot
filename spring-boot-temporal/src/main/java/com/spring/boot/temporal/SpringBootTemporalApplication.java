package com.spring.boot.temporal;

import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@MapperScan("com.spring.boot.temporal")  // 替换为你的包名
@SpringBootApplication
public class SpringBootTemporalApplication {

	@Autowired
	private WorkflowClient workflowClient;

	@Autowired
	private OrderActivityImpl orderActivity;

	public static void main(String[] args) {
//		SpringApplication.run(SpringBootTemporalApplication.class, args);

		SpringApplication app = new SpringApplication(SpringBootTemporalApplication.class);
		ConfigurableApplicationContext context = app.run(args);

		startTemporalWorker(context);
	}

	private static void startTemporalWorker(ConfigurableApplicationContext context) {
		WorkflowClient client = context.getBean(WorkflowClient.class);
		OrderActivityImpl activity = context.getBean(OrderActivityImpl.class);

		WorkerFactory factory = WorkerFactory.newInstance(client);
		Worker worker = factory.newWorker("order-task-queue");

		worker.registerWorkflowImplementationTypes(OrderWorkflowImpl.class);
		worker.registerActivitiesImplementations(activity);

		factory.start();
		System.out.println("✅ Temporal Worker 已启动，监听任务队列: order-task-queue");
	}

}
