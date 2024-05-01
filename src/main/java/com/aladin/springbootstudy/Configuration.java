package com.aladin.springbootstudy;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@org.springframework.context.annotation.Configuration

public class Configuration {
    @Bean(name="oauth")
    public PropertiesFactoryBean kakaoPropertiesFactoryBean() throws Exception {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("oauth.yml");
        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }

    @Bean(name="encrypt")
    public PropertiesFactoryBean encryptPropertiesFactoryBean() throws Exception {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ClassPathResource classPathResource = new ClassPathResource("encrypt.yml");
        propertiesFactoryBean.setLocation(classPathResource);

        return propertiesFactoryBean;
    }
}
