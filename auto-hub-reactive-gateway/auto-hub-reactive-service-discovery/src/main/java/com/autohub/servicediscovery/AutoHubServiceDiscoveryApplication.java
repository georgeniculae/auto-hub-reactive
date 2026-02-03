package com.autohub.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AutoHubServiceDiscoveryApplication {

    static void main(String[] args) {
        SpringApplication.run(AutoHubServiceDiscoveryApplication.class, args);
    }

}
