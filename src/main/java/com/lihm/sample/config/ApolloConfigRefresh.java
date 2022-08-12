package com.lihm.sample.config;

import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
public class ApolloConfigRefresh implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @ApolloConfigChangeListener
    public void onApolloChange(ConfigChangeEvent changeEvent) {

//        https://blog.csdn.net/godloveleo9527/article/details/121331074
        log.info("apollo配置发生改变");
        changeEvent.changedKeys().stream()
                .forEach(key -> {
                    log.info("apollo change {}={}", key, changeEvent.getChange(key).getNewValue());
                });
//        applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));

        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();

        Map<String, Object> configBeanMap = applicationContext.getBeansWithAnnotation(ConfigurationProperties.class);
        if (ObjectUtils.isEmpty(configBeanMap)) {
            return;
        }

        Set<String> changeConfigurationPropertiesBeanNames = new HashSet<>();

        Set<String> keys = new HashSet<>();
        configBeanMap.forEach((className, configClass) -> {
            if (StringUtils.isBlank(className) || ObjectUtils.isEmpty(configClass)) {
                return;
            }
            ConfigurationProperties configurationProperties = configClass.getClass().getAnnotation(ConfigurationProperties.class);
            if (configurationProperties == null) {
                return;
            }


            String prefix = configurationProperties.prefix();
            if (StringUtils.isBlank(prefix.trim())) {
                return;
            }


            for (String changeKey : changeEvent.changedKeys()) {
                if (changeKey.startsWith(prefix)) {
                    log.info("apollo key = {} refresh", changeKey);
                    keys.add(changeKey);
                    changeConfigurationPropertiesBeanNames.add(className);
                    /**
                     * 获取字段
                     *
                     * sample.api-url
                     * sample.api-host
                     * sample.api-host
                     */
                    String propertiesKey = lowercaseHumpNaming(changeKey);

                    for (Field field : configClass.getClass().getDeclaredFields()) {

                        try {
                            if (StringUtils.equals(prefix.concat(".").concat(field.getName()), propertiesKey)) {
                                field.setAccessible(true);
                                field.set(configClass, changeEvent.getChange(changeKey).getNewValue());
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            log.info("刷新配置出错:{}.{},{}", className, field.getName(), changeKey);
                        } finally {
                        }


                    }

                }
            }
        });


    }

    /**
     * 将key映射为驼峰命名
     */
    public String lowercaseHumpNaming(String param) {
        //为空则返回
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        //全部转换为小写
        param = param.toLowerCase();
        //返回值
        StringBuilder sb = new StringBuilder("");
        //不包含直接返回
        if (!param.contains("-")) {
            return param;
        }
        String arrays[] = param.split("");
        //循环处理
        for (int i = 0; i < arrays.length; i++) {
            //读取当前的字符
            String s = arrays[i];
            //如果当前字符为中划线，则替换下一个字符为大写
            if (s.equals("-")) {
                int nextStep = i + 1;
                if (nextStep <= arrays.length) {
                    String next = arrays[nextStep];
                    next = next.toUpperCase();
                    arrays[nextStep] = next;
                }
            } else {
                sb.append(s);
            }
        }
        return sb.toString();


    }


}
