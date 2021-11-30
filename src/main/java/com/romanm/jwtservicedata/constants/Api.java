package com.romanm.jwtservicedata.constants;

public class Api {
    public final static String API_PREFIX = "/api";
    public final static String PARAM_PAGE = "page";
    public final static String PARAM_PAGE_SIZE = "size";
    public final static String PARAM_USER_ID = "user_id";
    public final static String PARAM_FILE_ID = "file_id";
    public final static String PARAM_FROM_USER_ID = "from_user_id";
    public final static String PARAM_FILE = "file";
    public final static String PARAM_FILES = "files";

    public final static String BASE_URL = "http://localhost:8090";

    public final static String API_CHAT_MESSAGES = "/chat_messages";
    public final static String API_USER_PROFILE = "/user_profile";
    public final static String API_USER_IMAGES = "/user_images";
    public final static String API_USER_IMAGES_ALL = "/user_images/all";
    public final static String API_USER_IMAGES_SOME = "/user_images/some";
    public final static String API_USER_IMAGES_SOME_USER_ID = API_USER_IMAGES_SOME+"/{"+Api.PARAM_USER_ID+"}";
    public final static String API_USER_IMAGES_USER_ID = API_USER_IMAGES+"/{"+Api.PARAM_USER_ID+"}";
    public final static String API_USER_PROFILE_USER_ID = API_USER_PROFILE+"/{"+Api.PARAM_USER_ID+"}";
}
