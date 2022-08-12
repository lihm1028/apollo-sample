package com.lihm.sample.api;

import com.lihm.sample.config.SampleProperties;
import com.lihm.sample.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {


    @Autowired
    private SampleProperties properties;

    @Value("${sample.api-url:}")
    private String apiUrl;

    @Value("${sample.api-host:}")
    private String apiHost;


    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private BookService bookService;

    @GetMapping("/config")
    public Map getConfig() {
        Map map = new HashMap();
        map.put("apiUrl", apiUrl);
        map.put("apiHost", apiHost);
        return map;
    }

    @GetMapping("/config2")
    public Map getConfig2() {
        Map map = new HashMap();
        map.put("apiUrl", properties.getApiUrl());
        map.put("apiHost", properties.getApiHost());
        map.put("appKey", properties.getAppKey());
        map.put("appSecret", properties.getAppSecret());
        return map;
    }

    @GetMapping("/config3")
    public Map getConfig3() {
        Map map = new HashMap();
        SampleProperties contextBean = applicationContext.getBean(SampleProperties.class);

        map.put("apiUrl", contextBean.getApiUrl());
        map.put("apiHost", contextBean.getApiHost());
        map.put("appKey", contextBean.getAppKey());
        map.put("appSecret", contextBean.getAppSecret());
        return map;
    }


    @GetMapping("/config4")
    public Map getConfig4() {
        Map map = new HashMap();
        SampleProperties contextBean = bookService.getConfig();

        map.put("apiUrl", contextBean.getApiUrl());
        map.put("apiHost", contextBean.getApiHost());
        map.put("appKey", contextBean.getAppKey());
        map.put("appSecret", contextBean.getAppSecret());
        return map;
    }


}
