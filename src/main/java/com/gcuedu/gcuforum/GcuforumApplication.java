package com.gcuedu.gcuforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "com.gcuedu.gcuforum.mapper")
@ServletComponentScan
public class GcuforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(GcuforumApplication.class);
    }
}
