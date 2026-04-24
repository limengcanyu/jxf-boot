package com.spring.boot.custom.starter.autoconfigure;

import com.spring.boot.custom.starter.service.AcmeService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/01/22 15:42
 */
// classpath下发现该类时自动配置。
//@ConditionalOnClass(AcmeService.class)

// acme.enabled 为 true 时自动配置。
@ConditionalOnProperty(prefix = "acme", name = "enabled", havingValue = "true", matchIfMissing = false)

@EnableConfigurationProperties(AcmeProperties.class)
@Configuration
public class AcmeAutoConfiguration {

    private final AcmeProperties properties;

    public AcmeAutoConfiguration(AcmeProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public AcmeService exampleService (){
        return  new AcmeService(properties.isCheckLocation(), properties.getLoginTimeout());
    }
}
