package org.asura.couchbase;

import org.asura.couchbase.entity.TravelSample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

@SpringBootTest
class AsuraCouchbaseApplicationTests {

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;

    @Test
    void contextLoads() {
        System.out.println(couchbaseTemplate.getBucketName());

        System.out.println(couchbaseTemplate.findById(TravelSample.class).one("airline_10"));

        TravelSample travelSample = new TravelSample();
        travelSample.setId("airline_101");
        travelSample.setType("airline");
        travelSample.setName("40-Mile Air");
        travelSample.setIata("Q6");
        System.out.println(couchbaseTemplate.insertById(TravelSample.class).one(travelSample));

        System.out.println(couchbaseTemplate.removeById(TravelSample.class).one("airline_101"));
    }

}
