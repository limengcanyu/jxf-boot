package org.asura.custom.starter.autoconfigure;

import org.asura.custom.starter.service.AcmeService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/01/22 15:42
 */
@EnableConfigurationProperties(AcmeProperties.class)
@Configuration
public class AcmeAutoConfiguration {

    private final AcmeProperties properties;

    public AcmeAutoConfiguration(AcmeProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public AcmeService exampleService() {
        return new AcmeService(properties.isCheckLocation(), properties.getLoginTimeout());
    }
}
