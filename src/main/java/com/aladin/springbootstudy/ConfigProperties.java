package com.aladin.springbootstudy;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ConfigProperties {
    @Bean(name="kakao")
    public PropertiesFactoryBean kakaoPropertiesFactoryBean() throws Exception {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("kakao.properties");
        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }
    @Bean(name="encrypt")
    public PropertiesFactoryBean encryptPropertiesFactoryBean() throws Exception {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("encrypt.properties");
        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

    @Bean(name="crypto")
    public PropertiesFactoryBean cryptoPropertiesFactoryBean() throws Exception {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("crypto.properties");
        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }
}
