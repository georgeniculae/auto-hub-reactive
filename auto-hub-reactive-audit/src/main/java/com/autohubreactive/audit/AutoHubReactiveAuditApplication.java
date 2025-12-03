package com.autohubreactive.audit;

import com.autohubreactive.lib.annotation.AutoHubReactiveMicroservice;
import org.springframework.boot.SpringApplication;

@AutoHubReactiveMicroservice
public class AutoHubReactiveAuditApplication {

    static void main(String[] args) {
        SpringApplication.run(AutoHubReactiveAuditApplication.class, args);
    }

}
