package com.imd.mongodbproject.Config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public Info infoApi(){
        return new Info()
                .title("MongoDb Project service API REST")
                .version("V1.0.0");
    }

}