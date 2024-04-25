package com.qxc.scaleconvertiontools.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author qxc
 * @Date 2024 2024/3/10 19:08
 * @Version 1.0
 * @PACKAGE com.qxc.scaleconvertiontools.Config
 */
@Configuration
@PropertySource("classpath:SPB-plugInUnit.properties")
@Data
public class ScaleConversionToolsConfiguration {
    @Value("${saveDir}")
    String saveDir = "";

    @Value("${visPic}")
    String visPic = "vis_pic";

    @Value("${nirPic}")
    String nirPic = "nir_pic";

    @Value("${visLoc}")
    String visLoc = "vis_loc";

    @Value("${nirLoc}")
    String nirLoc = "nir_loc";

    @Value("${command}")
    String py = "python D:\\tmp\\test.py";
}
