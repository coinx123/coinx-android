package com.coin.exchange.utils;

import com.coin.exchange.config.okEx.ServerTimeStampHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/11/5
 * @description 时间格式类
 * 支持格式：
 * <p>
 * long -> String :{@link #_getDateStringViaTimeStamp}
 * long -> Date{@link Date} :{@link #getDateViaTimeStamp}
 * String -> long :{@link #_getTimeStampViaString}
 * String -> Date{@link Date} :{@link #_getDateViaString}
 * Date{@link Date} -> String :{@link #_getDateStringViaDate}
 * Date{@link Date} -> long :{@link #getTimeStampViaDate}
 */

public class DateUtils {

    private static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String YY_MM_DD = "yyMMdd";
    private static final String MM_DD_HH_MM = "MM-dd HH:mm";
    private static final SimpleDateFormat format = new SimpleDateFormat(ISO_8601);
    private static final Map<String, SimpleDateFormat> dateFormatMap = new HashMap<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取 格式化的 format
     *
     * @param pattern 格式，可参见{@link #ISO_8601}
     * @return
     */
    private static SimpleDateFormat _getDateFormat(String pattern) {
        SimpleDateFormat dateFormat = dateFormatMap.get(pattern);
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(pattern);
            dateFormatMap.put(pattern, dateFormat);
        }
        return dateFormat;
    }

    //=============================Date -> String start===================================

    /**
     * 按照 dataFormat 格式，获取 date 的时间
     * Date -> String
     *
     * @param date       时间
     * @param dateFormat 格式，可以参考{@link #ISO_8601}
     * @return
     */
    private static String _getDateStringViaDate(Date date, String dateFormat) {
        return _getDateFormat(dateFormat).format(date);
    }

    /**
     * 按照 dateFormat 格式，获取 当前date 的时间
     * Date(当前时间) -> String
     *
     * @param dateFormat 格式，可以参考{@link #ISO_8601}
     * @return
     */
    private static String _getCurrentDateStringViaDate(String dateFormat) {
        return _getDateStringViaDate(new Date(), dateFormat);
    }

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     * Date -> String（ISO_8601格式）
     *
     * @param date 时间date
     * @return
     */
    public static String getISO8601Timestamp(Date date) {
        return _getDateStringViaDate(date, ISO_8601);
    }

    /**
     * 获取当前 ISO8601标准时间
     * Date（当前时间） -> String（ISO_8601格式）
     *
     * @return
     */
    public static String getCurrentISO8601Timestamp() {
        return _getCurrentDateStringViaDate(ISO_8601);
    }

    //=============================Date -> String end===================================

    //=============================long -> String start===================================

    /**
     * 获取 格式化时间 通过 时间戳
     * long -> String(指定的dateFormat格式)
     *
     * @param dateFormat 格式，可以参考{@link #ISO_8601}
     * @param timeStamp  13位的时间戳（含毫秒）
     * @return
     */
    private static String _getDateStringViaTimeStamp(String dateFormat, long timeStamp) {
        return _getDateFormat(dateFormat).format(timeStamp);
    }

    /**
     * 获取 格式化时间 通过 当前时间戳
     * long(当前时间) -> String(指定的dateFormat格式)
     *
     * @param dateFormat 格式，可以参考{@link #ISO_8601}
     * @return
     */
    private static String _getDateStringViaCurrentTimeStamp(String dateFormat) {
        return _getDateStringViaTimeStamp(dateFormat, System.currentTimeMillis());
    }

    /**
     * 获取 ISO8601格式化时间 通过 时间戳
     * long -> String(ISO8601格式)
     *
     * @param timeStamp 13位的时间戳（含毫秒）
     * @return
     */
    public static String getISO8601StringViaTimeStamp(long timeStamp) {
        return _getDateStringViaTimeStamp(ISO_8601, timeStamp);
    }

    /**
     * 获取 ISO8601格式化时间 通过 当前时间戳
     * long(当前时间戳) -> String(ISO8601格式)
     *
     * @return
     */
    public static String getISO8601StringViaCurrentTimeStamp() {
        return _getDateStringViaCurrentTimeStamp(ISO_8601);
    }
    //=============================long -> String end===================================

    //=============================String -> long start===================================

    /**
     * 获取 13位时间戳 通过 时间字符串
     * String -> long
     *
     * @param dateFormat 格式，可以参考{@link #ISO_8601}
     * @param dateString 时间字符串，格式有 dateFormat 决定
     * @return 正确解析，返回13位的时间戳；错误返回-1
     */
    private static long _getTimeStampViaString(String dateFormat, String dateString) {
        try {
            return _getDateFormat(dateFormat).parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取 13位时间戳 通过 时间字符串
     * String(ISO8601格式) -> long
     *
     * @param dateString ISO8601格式 时间字符串
     * @return 正确解析，返回13位的时间戳；错误返回-1
     */
    public static long getTimeStampViaISO8601String(String dateString) {
        return _getTimeStampViaString(ISO_8601, dateString);
    }

    //对bitmex时间的转换
    public static String getBitmexTimeStampViaISO8601String(String dateString) {
        try {
            Date time = format.parse(dateString);
            long uinx = time.getTime();
            long time_teal = uinx + 8 * 60 * 60 * 1000; //多增加8个小时
            String date = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(time_teal));
            return date;
        } catch (ParseException e) {
            return "";
        }
    }
    //=============================String -> long end===================================

    //=============================Date -> long start===================================

    /**
     * 获取 时间戳 通过 Date
     * Date -> long
     *
     * @param date
     * @return
     */
    public static long getTimeStampViaDate(Date date) {
        return date.getTime();
    }
    //=============================Date -> long end===================================

    //=============================long -> Date start===================================

    /**
     * 获取 Date 通过 时间戳
     *
     * @param timeStamp 13位的时间戳（含毫秒）
     * @return
     */
    public static Date getDateViaTimeStamp(long timeStamp) {
        return new Date(timeStamp);
    }
    //=============================long -> Date end===================================

    //=============================String -> Date start===================================

    /**
     * 获取 Date 通过 字符串
     * Date -> String
     *
     * @param dateFormat 格式，可以参考{@link #ISO_8601}
     * @param dateString ISO8601格式 时间字符串
     * @return 成功的话，返回 dateString 对应的 Date; 失败，返回当前的null
     */
    private static Date _getDateViaString(String dateFormat, String dateString) {
        SimpleDateFormat simpleDateFormat = _getDateFormat(dateFormat);
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 Date 通过 字符串
     * Date -> String(ISO8601 格式)
     *
     * @param dateString ISO8601格式 时间字符串
     * @return 成功的话，返回 dateString 对应的 Date; 失败，返回当前的null
     */
    public static Date getDateViaISO8601String(String dateString) {
        return _getDateViaString(ISO_8601, dateString);
    }

    //=============================String -> Date end===================================

    public static String getYYMMDDViaTimeStamp(long timeStamp) {
        return _getDateStringViaTimeStamp(YY_MM_DD, timeStamp);
    }

    public static String getMM_DD_HH_MMViaTimeStamp(long timeStamp) {
        return _getDateStringViaTimeStamp(MM_DD_HH_MM, timeStamp);
    }

    public static String getMM_DD_HH_MMViaISO8601(String timeStamp) {
        return getMM_DD_HH_MMViaTimeStamp(
                getTimeStampViaISO8601String(timeStamp) + ServerTimeStampHelper.getInstance().getDelay()
        );
    }

    public static String getMM_DD_HH_MMViaISO8601ForBitMex(String timeStamp) {
        return getMM_DD_HH_MMViaTimeStamp(
                getTimeStampViaISO8601String(timeStamp) + ServerTimeStampHelper.DEFAULT_DELAY);
    }

    //返回季度时间
    public static String forBitMexTime(String timeStamp) {
        try {
            if (timeStamp == null) {
                return "永续";
            }
            Date date1 = sdf.parse(timeStamp);
            Date date2 = sdf.parse(dateFormat.format(new Date()));
            if ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) < 92) {
                return "当季";
            } else if ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) < 184) {
                return "次季";
            } else if ((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) >= 184) {
                return "下下季";
            }

        } catch (Exception e) {
            return "永续";
        }
        return "永续";
    }

    public static void main(String[] args) throws ParseException {

        /**
         * long -> String :{@link #_getDateStringViaTimeStamp}
         * long -> Date{@link Date} :{@link #getDateViaTimeStamp}
         * String -> long :{@link #_getTimeStampViaString}
         * String -> Date{@link Date} :{@link #_getDateViaString}
         * Date{@link Date} -> String :{@link #_getDateStringViaDate}
         * Date{@link Date} -> long :{@link #getTimeStampViaDate}
         */

        long timeStamp = 1541406716848L;
        Date date = new Date();
        String isoString = "2018-11-05T16:31:56.848Z";

        System.out.println("long -> String");
        System.out.println("long: " + timeStamp + "===> String: " + getISO8601StringViaTimeStamp(timeStamp));

        System.out.println("long -> Date");
        System.out.println("long: " + timeStamp + "===> Date: " + getDateViaTimeStamp(timeStamp));

        System.out.println("String -> long");
        System.out.println("String:" + isoString + "===> long:" + getTimeStampViaISO8601String(isoString));

        System.out.println("String -> Date");
        System.out.println("String:" + isoString + "===> Date:" + getDateViaISO8601String(isoString));

        System.out.println("Date -> long");
        System.out.println("Date:" + date + "===> long:" + getTimeStampViaDate(date));

        System.out.println("Date -> String");
        System.out.println("Date:" + date + "===> String:" + getISO8601Timestamp(date));

        System.out.println("YY-MM-DD");
        System.out.println("Data:" + date + "===> String:" + _getDateStringViaTimeStamp(YY_MM_DD, timeStamp));

    }

}