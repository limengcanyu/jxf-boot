package com.spring.boot.sentinel;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/24 14:41
 */
@Slf4j
@Configuration
public class SentinelConfig {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @PostConstruct
    private void initFlowRules() {
        log.debug("依赖注入完成，执行初始化操作...");

        // QPS限流
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("sayHello"); // 指定限流资源
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(2); // Set limit QPS to 2
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

        // 线程数模式限流
//        FlowRule rule = new FlowRule();
//        rule.setResource("sayHello"); // 指定限流资源
//        rule.setGrade(RuleConstant.FLOW_GRADE_THREAD);
//        rule.setCount(2); // Set limit THREAD to 2
//        rule.setLimitApp("default");
//        rules.add(rule);
//        FlowRuleManager.loadRules(rules);

        // 降级规则，可以多个degradeRule rule
        // DegradeRuleManager.getRules()可以获取到已经设置的降级规则
        List<DegradeRule> degradeRules = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        // 设置资源名称，sentinel降级都是以资源为单位进行
        degradeRule.setResource("circuitBreaker");
        // 使用异常统计降级,分钟统计,滑动时间窗口
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        // 异常数达到的数量阈值
        degradeRule.setCount(2);
        // 秒级时间窗口,该值必须有且必须大于零，否则降级将无法生效
        degradeRule.setTimeWindow(1);
        degradeRules.add(degradeRule);
        // 重新加载限流规则，此处将覆盖原有的限流，所以如果想要不覆盖
        // 请使用DegradeRuleManager.getRules()获取到的加入到rules中
        DegradeRuleManager.loadRules(degradeRules);
    }
}
