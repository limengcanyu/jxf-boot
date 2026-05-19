使用方式：

启动应用后，可以通过以下接口操作：

GET /jobs - 列出所有可用的 Job
GET /launch/{jobName} - 触发指定的 Job，例如：
GET /launch/importUserJob
GET /launch/taskletJob
GET /launch/taskletStepJob
