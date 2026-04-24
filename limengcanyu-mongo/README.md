# spring-boot-mongo

## 开启 mongo 事务支持

1. 添加 @EnableTransactionManagement 注解
2. 配置 MongoTransactionManager

``````
    @Bean
    MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
``````

## Special behavior inside transactions

### Connection Settings

The MongoDB drivers offer a dedicated replica set name configuration option turing the driver into auto detection mode. 
This option helps identifying replica set master nodes and command routing during a transaction.

Make sure to add replicaSet to the MongoDB URI. Please refer to connection string options for further details.

### Collection Operations

`MongoDB does not support collection operations, such as collection creation, within a transaction. `
This also affects the on the fly collection creation that happens on first usage. 
Therefore make sure to have all required structures in place.

### Transient Errors

MongoDB can add special labels to errors raised during transactional execution. 
Those may indicate transient failures that might vanish by merely retrying the operation. 
We highly recommend Spring Retry for those purposes. 
Nevertheless one may override MongoTransactionManager#doCommit(MongoTransactionObject) to 
implement a Retry Commit Operation behavior as outlined in the MongoDB reference manual.

### Count
   
MongoDB count operates upon collection statistics which may not reflect the actual situation within a transaction. 
The server responds with error 50851 when issuing a count command inside of a multi-document transaction. 
Once MongoTemplate detects an active transaction, all exposed count() methods are converted and 
delegated to the aggregation framework using $match and $count operators, preserving Query settings, such as collation.
   
Restrictions apply when using geo commands inside of the aggregation count helper. 
The following operators cannot be used and must be replaced with a different operator:
   
* $where → $expr
   
* $near → $geoWithin with $center
   
* $nearSphere → $geoWithin with $centerSphere
   
Queries using Criteria.near(…) and Criteria.nearSphere(…) must be rewritten to Criteria.within(…) respective Criteria.withinSphere(…). 
Same applies for the near query keyword in repository query methods that must be changed to within. 
See also MongoDB JIRA ticket DRIVERS-518 for further reference.
   
The following snippet shows count usage inside the session-bound closure:

``````
   session.startTransaction();
   
   template.withSession(session)
       .execute(action -> {
           action.count(query(where("state").is("active")), Step.class)
           ...
``````

The snippet above materializes in the following command:

``````
   db.collection.aggregate(
      [
         { $match: { state: "active" } },
         { $count: "totalEntityCount" }
      ]
   )
``````

instead of:

``````
    db.collection.find( { state: "active" } ).count()
``````

## mongo shell

db.getCollection("group_test").find({"tenant_id":"tenant_000001", "employee_id":"employee_000001"})

db.group_test.insertOne({ "_id" : 1, "tenant_id" : "tenant_000001", "employee_id": "employee_000001" })



