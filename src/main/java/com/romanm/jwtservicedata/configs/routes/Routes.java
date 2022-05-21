package com.romanm.jwtservicedata.configs.routes;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

public class Routes {

    /**
     * Получить query параметр из объекта ServerRequest
     * @param paramName String
     * @param serverRequest ServerRequest
     * @return String
     */
    public static String getQueryParam(String paramName, ServerRequest serverRequest) {
        String value = "0";
        Optional<String> optValue = serverRequest.queryParam(paramName);
        if (optValue.isPresent()) {
            value = optValue.get();
        }
        return value;
    }
}
