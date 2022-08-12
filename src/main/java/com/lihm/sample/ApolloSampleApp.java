package com.lihm.sample;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.lihm.sample.config.SampleProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableConfigurationProperties(value = {SampleProperties.class})
@EnableApolloConfig
public class ApolloSampleApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ApolloSampleApp.class, args);
        applicationContext.start();
    }
}
