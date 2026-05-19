package org.asura.liteflow;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsuraLiteflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsuraLiteflowApplication.class, args);
    }

    @Resource
    private FlowExecutor flowExecutor;

    @PostConstruct
    public void testConfig() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
    }
}
