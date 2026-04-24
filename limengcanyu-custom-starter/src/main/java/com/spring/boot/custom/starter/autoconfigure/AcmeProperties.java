package com.spring.boot.custom.starter.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>Description: </p>
 *
 * @author rock.jiang
 * Date 2020/01/22 15:33
 */
@ConfigurationProperties(prefix = "acme")
@Data
public class AcmeProperties {
    /**
     * Whether to check the location of acme resources.
     */
    private boolean checkLocation = true;

    /**
     * Timeout for establishing a connection to the acme server.
     */
    private Duration loginTimeout = Duration.ofSeconds(3);

}
