package com.romanm.jwtservicedata.constants;

public class CommonConstants
{
    public static enum FamilyStatus {
        SINGLE, MARRIED, SKIPED
    };

    public static enum Sex {
        MAN, WOMAN
    }

    public static enum SexOrientation {
        HETERO, HOMO, SKIPED
    }

    public static final String COMMENT_COLLECTION = "comments";
    public static final String LIKE_COLLECTION = "likes";
    public static final String CHAT_MESSAGE_COLLECTION = "chat_messages";
    public static final String USER_PROFILE_COLLECTION = "user_profiles";
}
