package com.vdegree.february.im.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/25 11:09
 */
@SpringBootApplication(scanBasePackages = {"com.vdegree.february.im.ws","com.vdegree.february.im.common","com.vdegree.february.im.api.ws"})
public class WSProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(WSProxyApplication.class, args);
    }
}
