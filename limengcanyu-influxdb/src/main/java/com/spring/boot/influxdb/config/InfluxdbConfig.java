package com.spring.boot.influxdb.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.influx.InfluxDbOkHttpClientBuilderProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxdbConfig {

    /**
     * InfluxDB relies on OkHttp. If you need to tune the http client InfluxDB uses behind the scenes,
     * you can register an InfluxDbOkHttpClientBuilderProvider bean.
     *
     * @return
     */
    @Bean
    InfluxDbOkHttpClientBuilderProvider builder() {
        return OkHttpClient.Builder::new;
    }
}
