package com.remitxpress.co.ke.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(exclude = GsonAutoConfiguration.class)
@ComponentScan(basePackages = "com.remitxpress.co.ke.main")
public class RemitXpressApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RemitXpressApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RemitXpressApplication.class, args);
    }

}
