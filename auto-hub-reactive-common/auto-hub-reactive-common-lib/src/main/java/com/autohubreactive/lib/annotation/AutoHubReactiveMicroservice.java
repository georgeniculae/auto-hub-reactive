package com.autohubreactive.lib.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.security.autoconfigure.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication(exclude = {
        UserDetailsServiceAutoConfiguration.class,
        ReactiveUserDetailsServiceAutoConfiguration.class
})
@ComponentScan(basePackages = "com.autohubreactive")
@EntityScan("com.autohubreactive")
@EnableAutoConfiguration
@EnableReactiveMongoRepositories(basePackages = "com.autohubreactive")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableScheduling
@EnableConfigurationProperties
public @interface AutoHubReactiveMicroservice {
}
