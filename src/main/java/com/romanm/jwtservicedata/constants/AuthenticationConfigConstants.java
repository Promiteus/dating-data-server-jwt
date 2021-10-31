package com.romanm.jwtservicedata.constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationConfigConstants {
    public static final String SECRET = "Java_to_Dev_Secret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CACHE_USERS_KEY = "users";

    public static final String MSG_PREFIX = ">> ";

    public static final String MSG_USER_NOT_FOUND = "User '%s' not found!";
    public static final String MSG_USER_BLOCKED = "User '%s' was blocked!";

    public static String prefixMsg(String msg) {
        return MSG_PREFIX+msg;
    }

    /**
     *
     * @param username
     * @return
     */
    public static String getUserNotFoundMsg(String username) {
        String msg = String.format(MSG_USER_NOT_FOUND, username);
        log.info(prefixMsg(msg));
        return msg;
    }

    /**
     *
     * @param username
     */
    public static void userBlocked(String username) {
        String msg = String.format(MSG_USER_BLOCKED, username);
        log.info(prefixMsg(msg));
    }

    /**
     *
     * @param message
     * @return
     */
    public static String invalidToken(String message) {
        String msg = String.format("Invalid token: '%s' !", message);
        log.info(prefixMsg(msg));
        return msg;
    }

    /**
     *
     * @param username
     * @param url
     * @param method
     */
    public static void getDecodedUserMsg(String username, String url, String method) {
        String msg = String.format("User '%s' requested data: %s %s", username, method, url);
        log.info(prefixMsg(msg));
    }
}
