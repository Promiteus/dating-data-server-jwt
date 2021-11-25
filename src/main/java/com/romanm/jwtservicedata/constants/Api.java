package com.romanm.jwtservicedata.constants;

public class Api {
    public final static String API_PREFIX = "/api";
    public final static String PARAM_PAGE = "page";
    public final static String PARAM_PAGE_SIZE = "size";
    public final static String PARAM_USER_ID = "user_id";
    public final static String PARAM_FROM_USER_ID = "from_user_id";

    public final static String BASE_URL = "http://localhost:8090";

    public final static String API_CHAT_MESSAGES = "/chat_messages";
    public final static String API_USER_PROFILE = "/user_profile";
    public final static String API_USER_PROFILE_USER_ID = API_USER_PROFILE+"/{"+Api.PARAM_USER_ID+"}";
}
