package com.spring.boot.data.neo4j;

import com.alibaba.fastjson2.JSONObject;
import com.spring.boot.data.neo4j.entity.*;
import com.spring.boot.data.neo4j.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootDataNeo4jApplicationTests {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private Node1Repository node1Repository;

    @Autowired
    private Node2Repository node2Repository;

    @Autowired
    private Node3Repository node3Repository;

    @Test
    void contextLoads() {
    }


    @Test
    void saveMovie() {
        movieRepository.deleteAll();
        cityRepository.deleteAll();
        actorRepository.deleteAll();

        Movie m1 = new Movie("无问西东", "2018");
        Movie m2 = new Movie("罗曼蒂克消亡史", "2016");
        movieRepository.save(m1);
        movieRepository.save(m2);

        City c1 = new City("香港", "1");
        City c2 = new City("上海", "1");
        cityRepository.save(c1);
        cityRepository.save(c2);

        Actor p1 = new Actor("章子怡", "1979");
        Actor p2 = new Actor("李芳芳", "1976");
        Actor p3 = new Actor("程耳", "1970");

        p1.addCity(c1);
        p1.addActor(m1);
        p3.addDirector(m1);

        p2.addCity(c2);
        p1.addActor(m2);
        p2.addDirector(m2);

        actorRepository.save(p1);
        actorRepository.save(p2);
        actorRepository.save(p3);

        Movie movie = movieRepository.findByTitle("罗曼蒂克消亡史");
        System.out.println("根据电影标题查找电影: " + JSONObject.toJSONString(movie));

        Actor person = actorRepository.findByName("章子怡");
        System.out.println("根据演员姓名查找演员: " + JSONObject.toJSONString(person));

    }

    @Test
    void saveCity() {
        City city;

//        city = new City("hangzhou", "5");
//        city = cityRepository.save(city);
//        System.out.println(city);

        System.out.println("--------------------------------------");
        Iterable<City> cityIterable = cityRepository.findAll();
        cityIterable.forEach(System.out::println);

        System.out.println("--------------------------------------");
        city = cityRepository.findById(25L).orElse(null);
        System.out.println(city);

        city = cityRepository.findOneByNameAndState("shanghai", "1");
        System.out.println(city);

        System.out.println("==============================================");
        city = cityRepository.findOneByNameAndState("nanyang", "2");
        System.out.println(city);
    }

    @Test
    void saveNode1() {
        node1Repository.deleteAll();
        node2Repository.deleteAll();
        node3Repository.deleteAll();

        Node1 node1Instance1 = new Node1("node1Instance1");

        Node2 node2Instance1 = new Node2("node2Instance1");
        Node2 node2Instance2 = new Node2("node2Instance2");
        Node2 node2Instance3 = new Node2("node2Instance3");

        Node3 node3Instance1 = new Node3("node3Instance1");
        Node3 node3Instance2 = new Node3("node3Instance2");
        Node3 node3Instance3 = new Node3("node3Instance3");

        // node1 instance1 -> node2 instance1 -> node3 instance1
        node2Instance1.addNode3(node3Instance1);
        node1Instance1.addNode2(node2Instance1);

        // node1 instance1 -> node2 instance3 -> node3 instance1
        node2Instance3.addNode3(node3Instance1);
        node1Instance1.addNode2(node2Instance3);

        // node1 instance1 -> node2 instance3 -> node3 instance3
        node2Instance3.addNode3(node3Instance3);
        node1Instance1.addNode2(node2Instance3);

        node1Repository.save(node1Instance1);
    }

    @Test
    void findNode1() {
        Node1 node1 = node1Repository.findByName("node1Instance1").orElse(null);
        System.out.println("根据名称查找节点1: \n" + JSONObject.toJSONString(node1));

        Iterable<Node1> node1Iterable = node1Repository.findAll();
        System.out.println("查找节点1全部: ");
        for (Node1 node11 : node1Iterable) {
            System.out.println(JSONObject.toJSONString(node11));
        }

        Node2 node2 = node2Repository.findByName("node2Instance1");
        System.out.println("根据名称查找节点2: " + node2);

        Node3 node3 = node3Repository.findByName("node3Instance1");
        System.out.println("根据名称查找节点3: " + node3);
    }

    /**
     * 删除node1Instance1
     */
    @Test
    void deleteNode1() {
        // delete node1Instance1
        node1Repository.deleteById(7L);
    }

    /**
     * 创建新建的node1Instance1和node2Instance1之间的关联
     */
    @Test
    void relateNode2Instance1() {
        Node2 node2Instance1 = node2Repository.findByName("node2Instance1");
        System.out.println("根据名称查找节点2: " + node2Instance1);

        Node1 node1Instance1 = new Node1("node1Instance1");

        // node1 instance1 -> node2 instance1
        node1Instance1.addNode2(node2Instance1);

        node1Repository.save(node1Instance1);
    }

    /**
     * 创建已存在的node1Instance1和node2Instance3之间的关联
     */
    @Test
    void relateNode2Instance3() {
        Node1 node1Instance1 = node1Repository.findByName("node1Instance1").orElse(null);
        System.out.println("根据名称查找节点1: " + node1Instance1);

        Node2 node2Instance3 = node2Repository.findByName("node2Instance3");
        System.out.println("根据名称查找节点2: " + node2Instance3);

        // node1 instance1 -> node2 instance3
        assert node1Instance1 != null;
        node1Instance1.addNode2(node2Instance3);

        node1Repository.save(node1Instance1);
    }

    /**
     * 更新node1Instance1
     */
    @Test
    void updateNode1Instance1() {
        // 根据名称查找，不存在返回null
        Node1 node1Instance1 = node1Repository.findByName("新建实例").orElse(null);
        System.out.println("根据名称查找节点1: " + node1Instance1);

        // 更新节点字段
        assert node1Instance1 != null;
        node1Instance1.setName("node1Instance1");

        // 重新保存节点
        node1Repository.save(node1Instance1);

        node1Instance1 = node1Repository.findByName("node1Instance1").orElse(null);
        System.out.println("根据名称查找节点1: " + node1Instance1);

        node1Instance1 = node1Repository.findByName("新建实例").orElse(null);
        System.out.println("根据名称查找节点1: " + node1Instance1);
    }

}
