package com.spring.boot.kafka.service.impl;

import com.spring.boot.kafka.service.KafkaListenerSampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: KafkaListener Sample Service Impl</p>
 *
 * @author rock.jiang
 * date 2019/06/21
 */
//@Service
public class KafkaListenerSampleServiceImpl implements KafkaListenerSampleService {
    private static Logger logger = LoggerFactory.getLogger(KafkaListenerSampleServiceImpl.class);

//    @KafkaListener(topics = "myTopic")
//    @SendTo("sinkTopic") // 方法返回结果转发到指定topic
//    public String listen(ConsumerRecord<String, String> cr) throws Exception {
//        logger.info("received message: " + cr.toString());
//        return cr.toString();
//    }

//    @KafkaListener(topics = "stringTopic")
//    public String listenString(ConsumerRecord<String, String> cr) throws Exception {
//        logger.info("received message: " + cr.toString());
//        return cr.toString();
//    }

//    /**
//     * 直接接收负载对象
//     *
//     * @param s
//     * @return
//     * @throws Exception
//     */
//    @KafkaListener(topics = "stringTopic")
//    public String listenString(String s) throws Exception {
//        logger.info("received message: " + s);
//        return s;
//    }

//    /**
//     * 接收ConsumerRecord，其中含有负载对象
//     *
//     * @param cr
//     * @return
//     * @throws Exception
//     */
//    @KafkaListener(topics = "userTopic")
//    public String listenUser(ConsumerRecord<String, User> cr) throws Exception {
//        logger.info("received message: " + cr.toString());
//        return cr.toString();
//    }

//    /**
//     * 直接接收负载对象
//     *
//     * @param user
//     * @throws Exception
//     */
//    @KafkaListener(topics = "userTopic")
//    public void listenUser(User user) throws Exception {
//        logger.info("received message: " + user);
//        logger.info("");
//    }

}
