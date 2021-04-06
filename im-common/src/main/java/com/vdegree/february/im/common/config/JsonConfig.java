package com.vdegree.february.im.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * json 转换 bean
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/26 10:12
 */
@Configuration
public class JsonConfig {

    @Bean
    public Gson gson(){
        return new GsonBuilder()
                .serializeNulls()
                .setVersion(1.0)
                .create();
    }
}
