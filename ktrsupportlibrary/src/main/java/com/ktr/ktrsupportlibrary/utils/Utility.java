package com.ktr.ktrsupportlibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 工具类。各个工具的基类
 * 提供常用的工具函数。
 * 
 * @author zhoubo
 * @version 1.0
 */
public class Utility {

    /**
     * 检查指定字符串是否为空或长度为0。
     * 
     * @param str
     *            需要检查的字符串
     * @return 字符串为空或长度为0，返回true，否则返回false。
     */
    public static boolean isEmpty(String str) {

        if (str == null || str.equals("") || str.trim().length() == 0)
            return true;
        
        return false;
    }

    /**
     * 检查指定字符串是否不为空并且长度大于0。
     * 
     * @param str
     *            需要检查的字符串
     * @return 字符串为空或长度为0，返回false，否则返回true。
     */
    public static boolean notEmpty(String str) {

        if (str == null)
            return false;

        return str.length() == 0 ? false : true;
    }

    /**
     * 检查指定的字符串是否为空，如果为空，返回空字符串，否则原样返回。
     * 
     * @param str
     *            指定的字符串
     * @return 转换后的字符串
     */
    public static String null2blank(String str) {

        try {
        	
			if (str == null)
			    str = "";

		} catch (Exception e) {
			
		}
        return str;
    }

    /**
     * 使用默认格式(yyyy-MM-dd HH:mm:ss)返回系统当前日期。
     * 
     * @return 系统日期
     */
    public static String getSysdate() {

        return getSysdate(Constants.DT_FORMAT_DEFAULT);
    }

    /**
     * 使用指定格式返回系统当前日期。
     * 
     * @param format
     *            日期格式
     * @return 系统日期
     */
	public static String getSysdate(String format) {

        if (isEmpty(format))
            format = Constants.DT_FORMAT_DEFAULT;

        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * Parses text from a string to produce a <code>Date</code>. Default format
     * is "dd/MM/yyyy".
     * 
     * @param data
     *            A <code>String</code>, part of which should be parsed.
     * @return A <code>Date</code> parsed from the string. In case of error,
     *         returns null.
     */
    public static Date parseDate(String data) {

        return parseDate(Constants.DT_FORMAT_DEFAULT, data);
    }

    /**
     * Parses text from a string to produce a <code>Date</code> with the given
     * format.
     * 
     * @param data
     *            A <code>String</code>, part of which should be parsed.
     * @param format
     *            date format
     * @return A <code>Date</code> parsed from the string. In case of error,
     *         returns null.
     */
	public static Date parseDate(String format, String data) {

        try {
            return new SimpleDateFormat(format).parse(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 半角字符检查。
     * 
     * @param str
     *            检查的字符串
     * @return 检查结果
     */
    public static final boolean checkSingleByte(String str) {

        if (isEmpty(str))
            return true;

        return str.getBytes().length == str.length();
    }

    /**
     * 转义SQL参数中的特殊字符。
     * 
     * @param param
     *            SQL参数
     * @return 转义后的SQL参数
     */
    public static String escapeSQLParam(String param) {

        if (param != null)
            param = param.trim().replaceAll("\\\\", "\\\\\\\\").replaceAll("_", "\\\\_").replaceAll("%", "\\\\%");
        else
            param = "";

        return param;
    }

    /**
     * 小数
     * 
     * @param str
     *            检查的字符串
     * @param integerDigit
     *            整数位
     * @param decimalDigit
     *            小数位
     * @return
     */
    public static final boolean checkNumberDigit(String str, int integerDigit, int decimalDigit) {

        if (isEmpty(str))
            return true;

        String[] num = str.split("\\.");
        if (!isNumber(num[0]) || num[0].length() > integerDigit) {
            return false;
        }

        if (num.length == 2) {
            if (!isNumber(num[1]) || num[1].length() > decimalDigit) {
                return false;
            }
        }

        return true;
    }

    /**
     * 数值检查
     * 
     * @param num
     *            检查的字符串
     * @return
     */
    public static boolean isNumber(String num) {

        return Pattern.matches(Constants.REGEX_NUMBER, num);
    }
    
    public static boolean isNumberDouble(String num) {

        return Pattern.matches(Constants.REGEX_NUMBER_DOUBLE, num);
    }

    /**
     * 日期的检查(YYYY-MM-DD)
     * 
     * @param str
     *            检查的字符串
     * @return
     */
    public static final boolean isDate(String str) {

        if (isEmpty(str))
            return false;

        return Pattern.matches(Constants.REGEX_DATE_YYYY_MM_DD, str);
    }

    /**
     * Email的检查
     * 
     * @param str
     *            检查的字符串
     * @return
     */
    public static final boolean isEmail(String str) {

        if (isEmpty(str))
            return false;

        return Pattern.matches(Constants.REGEX_EMAIL, str);
    }

    /**
     * 手机号码的检查
     * 
     * @param str
     *            检查的字符串
     * @return
     */
    public static final boolean isPhoneNo(String str) {

        if (isEmpty(str))
            return false;

        // 電話番号
        return Pattern.matches(Constants.REGEX_PHONE_NO, str);
    }

    /**
     * 经纬度的检查
     * 
     * @param str
     *            检查的字符串
     * @return
     */
    public static final boolean isLatLng(String str) {

        if (isEmpty(str))
            return false;

        // 经纬度的检查
        return Pattern.matches(Constants.REGEX_LAT_LNG, str);
    }
    
}