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
        HETERO, HOMO, BI
    }

    public static enum MeetPreferences {
        MAN, WOMAN, ALL
    }

    public static final String MONGO_DB = "chat";
    public static final String COMMENT_COLLECTION = "comments";
    public static final String VISITOR_COLLECTION = "visitors";
    public static final String CHAT_MESSAGE_COLLECTION = "chat_messages";
    public static final String USER_PROFILE_COLLECTION = "user_profiles";
    public static final String LIKES_COLLECTION = "likes";
    public static final String MULTIMEDIA_DEST_DIR = "%s/%s";
}
