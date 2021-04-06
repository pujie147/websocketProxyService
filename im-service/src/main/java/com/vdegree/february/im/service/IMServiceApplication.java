package com.vdegree.february.im.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/25 11:09
 */
@SpringBootApplication(scanBasePackages = {"com.vdegree.february.im.service","com.vdegree.february.im.common","com.vdpub.crontab","com.vdpub.common.util"})
public class IMServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IMServiceApplication.class, args);
    }
}
