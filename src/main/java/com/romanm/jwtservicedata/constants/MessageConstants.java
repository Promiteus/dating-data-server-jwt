package com.romanm.jwtservicedata.constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageConstants {
    public static final String SECRET = "Java_to_Dev_Secret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String MSG_PREFIX = ">> ";
    public static final String ERROR_MSG_PREFIX = " **** : ";

    public static final String MSG_USER_NOT_FOUND = "User '%s' not found!";
    public static final String MSG_USER_BLOCKED = "User '%s' was blocked!";
    public static final String MSG_SAVED_USER = "Saved user: %s";
    public static final String MSG_CANT_SAVE_USER = "Can't save users: %s";
    public static final String MSG_USER_PROFILE_COLLECTION_FILLED = "UserProfile collection is already filled!";
    public static final String MSG_CHAT_MESSAGE_FROM_USER = "Message from '%s' number '%s'.";

    public static final String MSG_FILE_NOT_FOUND = "File '%s' not found! Can't create thumb icon!";
    public static final String MSG_FILE_SAVED_SUCCESSFUL = "File '%s' was saved successfully!";
    public static final String MSG_THUMB_FILE_SAVED_SUCCESSFUL = "Thumb file '%s' was saved successfully!";
    public static final String MSG_ERR_FILE_SAVING = "Can't save file '%s'! Error: %s";
    public static final String MSG_CANT_DELETE_FILE = "Can't delete file '%s' or it was deleted before!";
    public static final String MSG_CANT_DELETE_FILES = "Can't delete files of directory '%s' or its were deleted before!";
    public static final String MSG_DELETED_FILE_SUCCESSFUL = "File '%s' was deleted successfully!";
    public static final String MSG_DELETED_FILES_SUCCESSFUL = "Files in directory '%s' were deleted successfully and directory was too!";
    public static final String MSG_FILES_COUNT = "Files in directory '%s' count: %d";
    public static final String MSG_FORMAT_FILE_INVALID = "Format file '%s' is invalid! Valid formats are: %s";
    public static final String MSG_MAX_FILES_COUNT = "Files amount for saving permitted are: %d";
    public static final String MSG_DELETE_THUMB_SUCCESSFULLY = "Thumb was deleted successfully!";
    public static final String MSG_DELETE_THUMB_FAILED = "Can't delete thumb!";

    public static final String MSG_NOT_ALL_HTTP_PARAMS = "Not all params have been passed!";
    public static final String MSG_UNKNOWN_MEDIA_TYPE = "It's unknown media type!";

    public static final String UNKNOWN_USER = "Unknown";


    public static String prefixMsg(String msg) {
        return MSG_PREFIX+msg;
    }

    public static String errorPrefixMsg(String msg) {
        return ERROR_MSG_PREFIX+msg;
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
