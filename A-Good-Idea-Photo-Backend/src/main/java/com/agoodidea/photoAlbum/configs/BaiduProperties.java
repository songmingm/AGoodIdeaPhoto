package com.agoodidea.photoAlbum.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 百度api参数
 * Create by mmsong on 2022/12/23 21:33
 */
@Configuration
@PropertySource("classpath:baidu.properties")
public class BaiduProperties {

    @Value("${baidu.serverAk}")
    private String ak;

    @Value("${baidu.serverUrl}")
    private String gpsServerUrl;
}
