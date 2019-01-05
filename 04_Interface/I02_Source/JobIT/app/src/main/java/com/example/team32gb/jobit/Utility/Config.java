package com.example.team32gb.jobit.Utility;

public class Config {
    //Lưu trạng thái
    public static final String IS_LOGGED = "IS_LOGGED";

    //Lưu đường dẫn
    public static final String REF_JOBSEEKERS_NODE = "jobseekers";
    public static final String REF_RECRUITERS_NODE = "recruiters";
    public static final String REF_ADMINS_NODE = "admins";
    public static final String REF_INFO_COMPANY = "companys";
    public static final String REF_FCM_TOKEN = "fcm_tokens";
    public static final String REF_AWAITING_APPROVAL_RECRUITERS = "awaiting_approval_recruiters";
    public static final String REF_AWAITING_APPROVAL = "awaiting_approval_recruiters";
    public static final String REF_INFO_COMPANY_WAITING_APPROVAL = "companysWaitingAdminApproval";
    public static final String REF_REPORT_WAITING_ADMIN_APPROVAL ="reportWaitingAdminApproval";
    public static final String REF_REPORT ="reports";
    public static final String REF_RATING = "rating";
    public static final String REF_LIST_RATING = "listRating";
    public static final String REF_COMMENT_IN_RATING = "comment";
    //Dữ liệu offline
    public static final String SHARED_PREFERENCES_NAME = "team32gb";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String MAY_GET_LOCAL = "MAY_GET_LOCAL";
    public static final String SIGN_UP_WITH_EMAIL = "SIGN_UP_WITH_EMAIL";
    public static final String NAME_USER = "NAME_USER";
    public static final String EMAIL_USER = "EMAIL_USER";
    public static final String PASSWORD_USER = "PASSWORD_USER";
    public static final String UID_USER = "UID_USER";
    public static final String IS_ACTIVITY_APPLY = "IS_ACTIVITY_APPLY" ;
    public static final String REGESTERED_INFO = "IS_ACTIVITY_APPLY" ;

    public static final String REF_CVS_NODE = "cvs";

    public static final String REF_FOLDER_AVATAR = "images/avatars";
    public static final String REF_FOLDER_LOGO = "images/logos";
    public static final String REF_FOLDER_IMAGE = "avatars";
    public static final int IS_JOB_SEEKER = 1;
    public static final int IS_RECRUITER = 2;
    public static final int IS_ADMIN = 3;

    public static final long NOT_APPROVAL = 0;
    public static final long APPROVAL_SUCCESSFUL = 1;
    public static final long APPROVAL_FAIL = 2;

    public static final String ID_REPORT_KEY = "idReport";
    public static final String DATE_SEND_KEY = "date";
    public static final String ID_ACCUSED_KEY = "idAccused";
    public static final String ID_COMPANY_KEY ="idCompany";

    public static final String IS_ACTIVE= "isActive";
    public static final String UN_ACTIVE_USER="unActiveUsers";
    public static final String FEED_BACKS_NODE = "feedbacks";

    public static long MILISECONDOFDAY = 86400000;
}
