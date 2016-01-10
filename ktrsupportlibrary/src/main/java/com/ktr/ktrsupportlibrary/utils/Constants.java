package com.ktr.ktrsupportlibrary.utils;

/**
 * 系统常用量
 * Constants
 * 
 * @author zhoubo
 * @version 1.0
 */
public class Constants {
    
    /**
     * 构造函数
     */
    public Constants(){
        
    }
    
    /** 系统名 */
    public static final String SYSTEM_NAME = "KTR_PROJECT";
    
    /** 日志输出对象名 */
    public static final String LOGGER_NAME = "KTR_PROJECT_LOG";
    
    
    // ///////////////////////////////////////////////
    // ///////////日期时间格式定义/////////////////////
    // ///////////////////////////////////////////////
    
    /** 日期格式 yyyy-MM-dd HH:mm:ss */
    public static final String DT_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /** 日期格式 yyyy-MM-dd */
    public static final String DT_FORMAT_YYYYDDMM = "yyyy-MM-dd";

    /** 日期格式 yyyyMMddHHmmss */
    public static final String DT_FORMAT_NO_MILLISECOND = "yyyyMMddHHmmss";

    /** 日期格式 yyyyMMddHHmmssS */
    public static final String DT_FORMAT_MILLISECOND = "yyyyMMddHHmmssS";
    
    
    // ///////////////////////////////////////////////
    // ///////////正则表达式定义/////////////////////
    // ///////////////////////////////////////////////
    
    /** 日期格式(yyy-MM-dd)正则表达式 */
    public static final String REGEX_DATE_YYYY_MM_DD = "^(?:(?!0000)[0-9]{4}-?(?:(?:0[1-9]|1[0-2])"
            + "-?(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-?(?:29|30)|(?:0[13578]|1[02])"
            + "-?31)|(?:[0-9]{2}-?(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)02-?29)$";
    
    /** Email正则表达式 */
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@"
            + "((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|" + "(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,3})(\\]?)$";

    /** 数值正则表达式 */
    public static final String REGEX_NUMBER = "^[0-9]*$";
    
    public static final String REGEX_NUMBER_DOUBLE = "^[0-9]+\\.{0,1}[0-9]{0,2}$";

    /** 手机号码正则表达式 */
    public static final String REGEX_PHONE_NO = "^[\\d\\-\\*\\#]+$";

    /** 经纬度正则表达式 */
    public static final String REGEX_LAT_LNG = "(||-)\\d{1,3}.\\d{6,17}";
}
