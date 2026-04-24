# spring-boot-graphql-java-kickstart

https://www.graphql-java-kickstart.com/

## graphiql

localhost:8080/graphiql

## voyager

localhost:8080/voyager

## altair

localhost:8080/altair

不能用，可用Edge浏览器扩展组件altair

地址输入

http://localhost:8080/graphql

## Playground

localhost:8080/playground

输入地址 

http://localhost:8080/graphql

# Write your query or mutation here
{
  # 查询book
  bookById(id: "book-1") {
    id
    name
    pageCount
    author {
      firstName
      lastName
    }
  }
}

https://github.com/limengcanyu/graphql-spring-boot


