package com.autohubreactive.apigateway.util;

import com.autohubreactive.exception.AutoHubException;
import tools.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import tools.jackson.core.JacksonException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class TestUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T getResourceAsJson(String resourceName, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(getResourceAsString(resourceName), valueType);
        } catch (JacksonException e) {
            throw new AutoHubException("Failed getting resource: " + resourceName + ", cause: " + e.getMessage());
        }
    }

    private static String getResourceAsString(String resourceName) {
        URL resource = TestUtil.class.getResource(resourceName);

        if (resource == null) {
            throw new AutoHubException("Failed getting resource: " + resourceName);
        }

        try {
            return new String(Files.readAllBytes(Path.of(resource.toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new AutoHubException("Failed getting resource: " + resourceName);
        }
    }

}
