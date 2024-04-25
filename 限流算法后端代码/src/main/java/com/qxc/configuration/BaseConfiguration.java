package com.qxc.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2023 2023/7/13 19:07
 * @Version 1.0
 * @PACKAGE com.qxc.configuration
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@ConfigurationProperties(prefix = "base-configuration")
@Data
public class BaseConfiguration {
    @Value("${basePackage:com.qxc}")
    String basePackage = "com.qxc";
}
