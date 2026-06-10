# docker 安装 mysql 8 版本
  
docker 中下载 mysql
  
  	sudo docker pull mysql
  
Starting a MySQL instance:
  
    docker run --name mysql-3306 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=P@ssW0rd -d mysql:latest
  	docker run --name mysql-3307 -p 3307:3307 -e MYSQL_ROOT_PASSWORD=P@ssW0rd -d mysql:latest
  	docker run --name mysql-3308 -p 3308:3308 -e MYSQL_ROOT_PASSWORD=P@ssW0rd -d mysql:latest

创建将要映射到容器中的目录以及.cnf文件，然后再创建容器

  	cd /usr/local
  	mkdir -p docker/mysql/conf
  	cd docker/mysql/conf
  	touch my.cnf
  
  	命令格式：mkdir [-p] DirName
  	说明：建立一个子目录。
  	参数：-p 确保目录名称存在，如果目录不存在的就新创建一个。
  
  	docker run --name mysql-3306 -p 3306:3306 -v /usr/local/docker/mysql/conf:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=P@ssW0rd -d mysql:latest

注：

  	--name                Assign a name to the container
  	--publish , -p        Publish a container’s port(s) to the host
  	--publish-all , -P    Publish all exposed ports to random ports
  	--env , -e            Set environment variables
  	--detach , -d         Run container in background and print container ID
  	--volume , -v         Bind mount a volume
  	--expose              Expose a port or a range of ports

进入容器

    docker exec -it mysql bash

登录mysql

    mysql -u root -p
    ALTER USER 'root'@'localhost' IDENTIFIED BY 'gh321';

添加远程登录用户

    CREATE USER 'rock'@'%' IDENTIFIED WITH mysql_native_password BY 'gh321';
    GRANT ALL PRIVILEGES ON *.* TO 'rock'@'%';
