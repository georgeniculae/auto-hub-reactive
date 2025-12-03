package com.autohubreactive.lib.config.objectmapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import tools.jackson.core.json.JsonWriteFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class ObjectMapperConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
//        JavaTimeModule javaTimeModule = new JavaTimeModule()
//                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
//                .addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);

//        return JsonMapper.builder()
//                .addModule(javaTimeModule)

                // Jackson 3: ALLOW_COERCION_OF_SCALARS was removed; default is "allowed",
                // so you can usually just drop it. This line is here only if you're still on Jackson 2.
                //.configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, true)

//                .configure(JsonWriteFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
//                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
//                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
//                .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
//                .build();

        return new ObjectMapper();
    }

}
