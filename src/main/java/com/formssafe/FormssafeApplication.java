package com.formssafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FormssafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormssafeApplication.class, args);
    }

}
