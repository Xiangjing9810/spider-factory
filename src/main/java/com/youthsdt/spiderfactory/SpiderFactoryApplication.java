package com.youthsdt.spiderfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpiderFactoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiderFactoryApplication.class, args);
    }

}
