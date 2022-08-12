package com.lihm.sample.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sample")
public class SampleProperties {


    private String apiUrl;

    private String apiHost;


    private String appKey;
    private String appSecret;

}
