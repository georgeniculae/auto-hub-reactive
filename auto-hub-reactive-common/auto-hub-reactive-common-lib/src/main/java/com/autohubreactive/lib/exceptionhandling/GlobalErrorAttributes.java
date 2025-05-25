package com.autohubreactive.lib.exceptionhandling;

import com.autohubreactive.lib.util.Constants;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

        Throwable error = super.getError(request);
        String message = getMessage(errorAttributes, error);

        errorAttributes.put(Constants.MESSAGE, message);

        return errorAttributes;
    }

    private String getMessage(Map<String, Object> errorAttributes, Throwable error) {
        String message = error.getMessage();

        if (HttpStatus.INTERNAL_SERVER_ERROR.value() == (Integer) errorAttributes.get(Constants.STATUS) && message.length() > 1000) {
            return Constants.UNEXPECTED_ERROR;
        }

        if (error instanceof ResponseStatusException responseStatusException) {
            return responseStatusException.getReason();
        }

        return message;
    }

}
