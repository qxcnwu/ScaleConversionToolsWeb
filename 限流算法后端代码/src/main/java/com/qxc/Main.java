package com.qxc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author qxc
 * @Date ${YEAR} ${DATE} ${TIME}
 * @Version 1.0
 * @PACKAGE com.qxc
 */
@SpringBootApplication
@EnableConfigurationProperties
public class Main {

    @SuppressWarnings("MissingJavadoc")
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}