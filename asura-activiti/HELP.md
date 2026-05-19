测试覆盖场景
序号	测试场景	HTTP 方法	接口路径
1	获取流程定义列表	GET	/api/process/definitions
2	启动流程实例	POST	/api/process/instances
3	查询流程实例详情	GET	/api/process/instances/{id}
4	查询待办任务列表	GET	/api/process/tasks/assignee/{userId}
5	获取任务详情	GET	/api/process/tasks/{id}
6	完成任务（审批通过）	POST	/api/process/tasks/{id}/complete
7	查询 HR 待办任务	GET	/api/process/tasks/assignee/{userId}
8	完成 HR 任务	POST	/api/process/tasks/{id}/complete
9	验证流程已结束	GET	/api/process/instances/{id}
10	启动流程并拒绝	POST/GET	完整流程
11	查询不存在的流程实例	GET	/api/process/instances/nonexistent-id
12	查询不存在的任务	GET	/api/process/tasks/nonexistent-id
13	获取流程实例列表	GET	/api/process/instances
测试特点