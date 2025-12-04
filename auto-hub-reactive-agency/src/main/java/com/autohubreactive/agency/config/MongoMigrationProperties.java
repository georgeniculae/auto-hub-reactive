package com.autohubreactive.agency.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Getter
public class MongoMigrationProperties {

    @Value("${migration.package-scan}")
    private String packageScan;

    @Value("${spring.mongodb.database}")
    private String databaseName;

}
