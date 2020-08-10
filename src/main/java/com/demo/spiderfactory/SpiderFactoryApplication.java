package com.demo.spiderfactory;

import com.demo.spiderfactory.kafka.KfkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpiderFactoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpiderFactoryApplication.class, args);
    }

}
