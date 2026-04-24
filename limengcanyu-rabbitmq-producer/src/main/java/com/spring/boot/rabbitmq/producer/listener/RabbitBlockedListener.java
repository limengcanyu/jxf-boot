package com.spring.boot.rabbitmq.producer.listener;

import com.rabbitmq.client.BlockedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>Description: Rabbit Blocked Listener </p>
 *
 * @author rock.jiang
 * Date 2020/04/01 13:44
 */
@Slf4j
@Component
public class RabbitBlockedListener implements BlockedListener {
    @Override
    public void handleBlocked(String s) throws IOException {
        log.debug("====== Connection Blocked, Reason: {}", s);
    }

    @Override
    public void handleUnblocked() throws IOException {
        log.debug("====== Connection Unblocked");
    }
}
