package com.flightlogic.co.ke.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(exclude = GsonAutoConfiguration.class)
@ComponentScan(basePackages = "com.flightlogic.co.ke.main")
//@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable method-level security
public class FlightLogicApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FlightLogicApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FlightLogicApplication.class, args);
    }

}
