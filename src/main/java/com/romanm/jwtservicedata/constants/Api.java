package com.romanm.jwtservicedata.constants;

public class Api {
    public final static String API_PREFIX = "/api";

    public final static String API_CHAT_MESSAGES = "/chat_messages";
    public final static String API_USER_PRIFILE = "/user_profile";
    public final static String API_USER_PRIFILE_USER_ID = API_USER_PRIFILE+"/{"+PARAM_USER_ID+"}";

    public final static String PARAM_PAGE = "page";
    public final static String PARAM_PAGE_SIZE = "size";
    public final static String PARAM_USER_ID = "user_id";


}
