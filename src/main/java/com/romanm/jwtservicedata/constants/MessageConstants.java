package com.romanm.jwtservicedata.constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageConstants {
    public static final String SECRET = "Java_to_Dev_Secret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CACHE_USERS_KEY = "users";

    public static final String MSG_PREFIX = ">> ";

    public static final String MSG_USER_NOT_FOUND = "User '%s' not found!";
    public static final String MSG_USER_BLOCKED = "User '%s' was blocked!";
    public static final String MSG_SAVED_USER = "Saved user: %s";
    public static final String MSG_CANT_SAVE_USER = "Can't save users: %s";
    public static final String MSG_USER_PROFILE_COLLECTION_FILLED = "UserProfile collection is already filled!";
    public static final String MSG_CHAT_MESSAGE_FROM_USER = "Сообщение от %s номер %s.";

    public static final String UNKNOWN_USER = "Uncknown";


    public static String prefixMsg(String msg) {
        return MSG_PREFIX+msg;
    }

    /**
     *
     * @param username String
     * @return String
     */
    public static String getUserNotFoundMsg(String username) {
        String msg = String.format(MSG_USER_NOT_FOUND, username);
        log.info(prefixMsg(msg));
        return msg;
    }

    /**
     *
     * @param username String
     */
    public static void userBlocked(String username) {
        String msg = String.format(MSG_USER_BLOCKED, username);
        log.info(prefixMsg(msg));
    }

    /**
     *
     * @param message String
     * @return String
     */
    public static String invalidToken(String message) {
        String msg = String.format("Invalid token: '%s' !", message);
        log.info(prefixMsg(msg));
        return msg;
    }

    /**
     *
     * @param username String
     * @param url String
     * @param method String
     */
    public static void getDecodedUserMsg(String username, String url, String method) {
        String msg = String.format("User '%s' requested data: %s %s", (username == null) ? UNKNOWN_USER : username, method, url);
        log.info(prefixMsg(msg));
    }
}
