package com.agoodidea.photoAlbum.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "ignore")
@PropertySource("classpath:ignore-url.properties")
public class IgnoreUrlConfig {

    private List<String> url;
}
