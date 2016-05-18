package com.e.common.constant;

/**
 * @author <a href="mailto:evan0502@qq.com">Evan</a>
 * @version 1.0
 * @{# Constants.java Create on 2014年11月30日 上午10:56:55
 * @description
 */
public class Constants {

    public static final String PLATFORM = "Android";
    public static final String PREF_DOC_ID = "id";
    public static final String PREF_DOC_JSON = "doctorJson_";//为了兼容2.5.1老版本，2.6以后版本不会再存这个

    public static final long ONE_MINUTES_MILLS = 1 * 60 * 1000;//1分钟调用1次,接口测试使用

    public static final long ONE_DAY_MILLS = 24 * 60 * ONE_MINUTES_MILLS;//1天

    public static final long ONE_WEEK_MILLS = 7 * ONE_DAY_MILLS;//7天

    public static final long TEN_MINUTES_MILLS = 10 * ONE_MINUTES_MILLS;//10分钟

    public static final long TWENTY_MINUTES_MILLS = 2 * TEN_MINUTES_MILLS;//20分钟
//    public static final long TWENTY_MINUTES_MILLS = ONE_MINUTES_MILLS;//测试的时候先给1分钟

    public static final String SHARE_PREF = "SHARE_PREF_CUSTOM";
    public static final String SHARE_PREF_DOCTOR = "doctor_preference";
    public static final String PREF_APP_SETTING_NAME = "setting_preference", PREF_KNOWLEDGE_NEW = "PREF_KNOWLEDGE_NEW",PREF_SHOW_GUIDE = "PREF_SHOW_GUIDE",PREF_SHOW_GUIDE_CHART = "PREF_SHOW_GUIDE_CHART";


    public static final String PREF_IS_OPEN_SPEAKER = "isOpenSpeaker";
    public static final String PREF_IS_OPEN_BEE = "isOpenBee";
    public static final String PREF_WEBVIEW_TEXT_SIZE = "webViewTextSize_";
    public static final String PREF_REFRESH_ADMISSIONS_MILLS = "PREF_REFRESH_ADMISSIONS_MILLS";//刷新接诊列表的时间戳


    //是否显示文章的推送，true显示，false关闭，默认打开文章推送
    public static final String PREF_IS_SHOW_ARTICLE_NOTIFICATION = "is_show_article_notification";

    public static String HEAD_CONTENT_TYPE = "Content-Type", HEAD_ACCEPT = "Accept";

    public static final class IDENTITY {
        public static final String IDENTITY_KEY = "identity_key";
        public static final String IDENTITY_INDEX = "identity_index";
        public static final String IDENTITY_FILEPATH = "identity_filepath";
        public static final String IDENTITY_ADDRESS = "identity_address";
        public static final String IDENTITY_CITY = "identity_city";
        public static final String IDENTITY_PROVINCE_ID = "identity_province_id";
        public static final String IDENTITY_CITY_ID = "identity_city_id";
        public static final String IDENTITY_CAP = "identity_cap";
        public static final int IDENTITY_RESULT_CODE_CONTENT_INPUT = 0x121212;

        public static final int ACTIVITY_CHOOSEFILE_CODE = 0x101;
    }

    public static final int NET_RESPONSE_SUCCESS_CODE = 0;
    public static final int NET_RESPONSE_FAIL_CODE = 1;
    public static final int NET_RESPONSE_ACCOUNT_EXPIRED = 401003;
    public static final int PANEL_SELECT_REQUEST_CODE = 0;
    public static boolean IS_CONTINUE_SHOW_ACCOUNT_EXPIRED_DIALOG = false;//是否需要继续显示账户过期的dialog,一个fragment/activity多个网络请求
    public static final String NET_RESPONSE_STATUS = "error_code";
    public static final String NET_RESPONSE_STATUS_NEW_API = "status";
    public static final String NET_RESPONSE_MSG = "msg";
    public static final String NET_RESPONSE_MSG_NEW_API = "messageToUser";
    public static final String NET_RESPONSE_MSG_NEW_API_ERROR_MSG = "error_msg";
    public static final String NET_RESPONSE_DATA = "data";

    public static boolean isWifi = false;
}
