package com.apica.backend.integrationservice;

import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.apica.backend.integrationservice")
public class IntegrationServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(IntegrationServiceApplication.class, args);
  }
  
  @PostConstruct
  public void init() {}
}
