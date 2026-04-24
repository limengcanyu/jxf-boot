package com.spring.boot.httpcomponents.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *
 * </p>
 *
 * @author rock.jxf
 * @date 2020/11/22 14:20
 */
@Slf4j
@Configuration
public class ApplicationConfig {

    @Bean
    RestTemplate httpRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    RestTemplate httpsRestTemplate() {
        try {
            return new RestTemplateBuilder().requestFactory(this::clientHttpRequestFactory).build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("配置RestTemplate失败！", e);
            return null;
        }
    }

    /**
     * 配置支持SSL
     *
     * @return ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;

        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

        assert sslContext != null;
        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(5000);
        factory.setHttpClient(httpClient);
        return factory;
    }
}
