#### 启动 spring-boot-example

#### 启动 arthas

```shell
cd E:\IdeaProjects\rock-boot\arthas\arthas-packaging-3.6.7-bin

java -jar arthas-boot.jar

```
#### 查看 dashboard

dashboard

#### 通过 thread 命令来获取到应用进程的 Main Class

thread 1会打印线程 ID 1 的栈，通常是 main 函数的线程。

thread 1 | grep 'main('

#### watch

通过watch命令来查看com.spring.boot.example.SpringBootExampleApplication#hello函数的返回值

watch com.spring.boot.example.SpringBootExampleApplication hello returnObj

#### 退出 arthas

如果只是退出当前的连接，可以用quit或者exit命令。Attach 到目标进程上的 arthas 还会继续运行，端口会保持开放，下次连接时可以直接连接上。

如果想完全退出 arthas，可以执行stop命令。
