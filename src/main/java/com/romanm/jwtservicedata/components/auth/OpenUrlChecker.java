package com.romanm.jwtservicedata.components.auth;

import com.romanm.jwtservicedata.constants.Api;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OpenUrlChecker {
    public boolean ckeck(String url) {
        return !url.isEmpty() && Arrays.stream(Api.openedUrlPaths).filter(url::contains).count() > 0;
    }
}
