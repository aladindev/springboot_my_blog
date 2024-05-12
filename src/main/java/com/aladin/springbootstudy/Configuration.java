package com.aladin.springbootstudy;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@org.springframework.context.annotation.Configuration

public class Configuration {
    @Bean(name = "oauth")
    public YamlPropertiesFactoryBean kakaoPropertiesFactoryBean() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("oauth.yml"));
        return yaml;
    }

    @Bean(name = "encrypt")
    public YamlPropertiesFactoryBean encryptPropertiesFactoryBean() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("encrypt.yml"));
        return yaml;
    }
}
