package org.asura.sse;

import org.asura.sse.model.SseMessageRequest;
import org.asura.sse.repository.SseSessionRepository;
import org.asura.sse.service.SseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SseServiceTest {

    @Autowired
    private SseService sseService;

    @Autowired
    private SseSessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.clear();
    }

    @Test
    void testCreateConnection() {
        SseEmitter emitter = sseService.createConnection("test-client", "test-channel");
        
        assertNotNull(emitter);
        assertEquals(1, sessionRepository.count());
    }

    @Test
    void testBroadcast() throws InterruptedException {
        SseEmitter emitter = sseService.createConnection("test-client", "test-channel");
        CountDownLatch latch = new CountDownLatch(1);
        
        emitter.onCompletion(latch::countDown);

        SseMessageRequest request = new SseMessageRequest();
        request.setData("Test message");
        request.setChannel("test-channel");
        
        sseService.broadcast(request);
        
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    void testGetActiveConnectionsCount() {
        assertEquals(0, sseService.getActiveConnectionsCount());
        
        sseService.createConnection("client1", null);
        sseService.createConnection("client2", "channel1");
        
        assertEquals(2, sseService.getActiveConnectionsCount());
    }

    @Test
    void testSubscribeAndUnsubscribeChannel() {
        SseEmitter emitter = sseService.createConnection("test-client", null);
        
        String sessionId = sessionRepository.findAll().iterator().next().getSessionId();
        
        assertEquals(0, sessionRepository.findById(sessionId).getChannels().size());
        
        sseService.subscribeChannel(sessionId, "test-channel");
        assertEquals(1, sessionRepository.findById(sessionId).getChannels().size());
        
        sseService.unsubscribeChannel(sessionId, "test-channel");
        assertEquals(0, sessionRepository.findById(sessionId).getChannels().size());
    }
}