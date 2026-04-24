# spring-boot-elasticsearch

## elasticsearch

$ docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.6.2

$ docker exec -it elasticsearch cat /etc/hosts

http://192.168.31.149:9200

http://localhost:9200

## kibana

$ docker run -d --name kibana --link elasticsearch:elasticsearch -p 5601:5601 kibana:7.6.2

$ docker exec -it kibana cat /etc/hosts

http://192.168.31.149:5601

http://localhost:5601
