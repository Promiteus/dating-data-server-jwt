package com.romanm.jwtservicedata.components.auth;

import com.romanm.jwtservicedata.constants.Api;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OpenUrlChecker {
    /**
     * Проверить наличие url в списке незакрытых токеном маршрутов
     * @param url String
     * @return boolean
     */
    public boolean check(String url) {
        return !url.isEmpty() && Arrays.stream(Api.openedUrlPaths).filter(url::contains).count() > 0;
    }
}
