package com.coin.exchange.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {
    /**
     * 按指定进制将字符串转换为整型数值
     *
     * @param s     要转换的字符串
     * @param d     默认返回的整型值
     * @param radix 要转换的进制数
     * @return 转换后的整数值
     */
    public static int getInt(String s, int d, int radix) {
        int v = d;
        try {
            v = Integer.parseInt(s, radix);
        } catch (Exception e) {
            v = d;
        }
        return v;
    }

    /**
     * 按指定进制将字符串转换为整型数值
     *
     * @param s     要转换的字符串
     * @param d     默认返回的整型值
     * @param radix 要转换的进制数
     * @return 转换后的整数值
     */
    public static long getLong(String s, long d, int radix) {
        long v = d;
        try {
            v = Long.parseLong(s, radix);
        } catch (Exception e) {
            v = d;
        }
        return v;
    }

    /**
     * 格式化输出浮点型数据，数字不分组
     *
     * @param d   需要格式化的浮点数据
     * @param len 小数点后保留的位数
     * @return
     */
    public static String noGroupFormat(double d, int len) {
        String newDecimal = null;
        if (d != 0) {
            StringBuffer format = new StringBuffer("0");
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    format.append(".");
                }
                format.append("0");
            }
            DecimalFormat decimalFormat = new DecimalFormat(format.toString());
            decimalFormat.setGroupingUsed(false);
            newDecimal = decimalFormat.format(d);
        } else {
            newDecimal = "0.0";
        }
        return newDecimal;
    }

    /**
     * 格式化输出浮点型数据，数字不分组
     *
     * @param d   需要格式化的浮点数据
     * @param len 小数点后保留的位数
     * @return
     */
    public static String noGroupFormat(String d, int len) {
        double dd = 0;
        try {
            dd = Double.parseDouble(d);
        } catch (Exception e) {
        }
        return noGroupFormat(dd, len);
    }

    /**
     * 将字符串转换为浮点型数值
     *
     * @param s 要转换的字符串
     * @param d 默认返回的浮点值
     * @return 转换后的浮点数值
     */
    public static double getDouble(String s, double d) {
        double v = d;
        try {
            v = Double.parseDouble(s);
        } catch (Exception e) {
            v = d;
        }
        return v;
    }

    public static BigDecimal getBigDecimal(String s, BigDecimal d) {
        BigDecimal v = d;
        try {
            v = new BigDecimal(s);
        } catch (Exception e) {
            v = d;
        }
        return v;
    }

    /**
     * 格式化输出浮点型数据
     *
     * @param d   需要格式化的浮点数据
     * @param len 小数点后保留的位数
     * @return
     */
    public static String decimalFormat(double d, int len) {
        String newDecimal = null;
        if (d != 0) {
            StringBuffer format = new StringBuffer("0");
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    format.append(".");
                }
                format.append("0");
            }
            DecimalFormat decimalFormat = new DecimalFormat(format.toString());
            decimalFormat.setGroupingUsed(true);
            //decimalFormat.setGroupingSize(3);
            newDecimal = decimalFormat.format(d);
        } else {
            if (len == 0) {
                newDecimal = "0";
            } else {
                newDecimal = "0.0";
            }
        }
        return newDecimal;
    }

    public static String getPercentString(double num) {
        return num >= 0 ? "+" + decimalFormat(num * 100, 2) + "%" : decimalFormat(num * 100, 2) + "%";
    }

    public static String getTimeFromStr(int time) {
        int hour = time / 10000;
        int minute = (time % 10000) / 100;
        String timeStr = "" + hour;
        if (hour < 10) {
            timeStr = "0" + timeStr;
        }
        if (minute < 10) {
            timeStr = timeStr + ":0" + minute;
        } else {
            timeStr = timeStr + ":" + minute;
        }
        return timeStr;
    }

    /**
     * 根据使用习惯格式化输出数量
     *
     * @param strNum 需要格式化的数量
     * @param type   使用习惯:1-中国大陆；其它-香港等地区
     * @param limit  是否限长度
     * @return
     * @author Wison
     */
    public static String volumeFormat(String strNum, int type, boolean limit) {
        try {
            Double.parseDouble(strNum);
        } catch (Exception e) {
            return strNum;
        }
        double num = NumberUtil.getDouble(strNum, 0);
        return volumeFormat(num, type, limit);
    }

    /**
     * 根据使用习惯格式化输出数量
     *
     * @param num   需要格式化的数量
     * @param type  使用习惯:1-中国大陆；其它-香港等地区
     * @param limit 是否限长度
     * @return
     */
    public static String volumeFormat(double num, int type, boolean limit) {
        type = 0; //全部按照KMB显示方式[注：如果把type=0这行去掉，KLineActivity中的成交量显示方式函数initMaxLabel需要修改对成交量的处理]
        int len = 2;
        String newVolume = "";
        double absNum = Math.abs(num);
        if (type == 1) {
            //中国大陆地区
            if (limit) {
                //限制长度
                if (absNum >= 100000000) {
                    newVolume = unitConvert(num, 100000000.00, len, "亿");//decimalFormat(num/100000000.00,len)+"亿";
                } else if (absNum >= 10000) {
                    newVolume = unitConvert(num, 10000.00, len, "万");//decimalFormat(num/10000.00,len)+"万";
                } else {
                    newVolume = "" + (int) num;
                }
            } else {
                long inum = (long) num;
                long b = inum / 10000;
                while (b > 0) {
                    newVolume = "," + fillLeftByZero(inum % 10000, 4) + newVolume;
                    inum = inum / 10000;
                    b = inum / 10000;
                }
                if (!"".equals(newVolume)) {
                    newVolume = inum + newVolume;
                } else {
                    newVolume = "" + inum;
                }
            }
        } else {
            //其它地区
            if (limit) {
                //限制长度
                if (absNum >= 1000000000) {
                    newVolume = unitConvert(num, 1000000000.00, len, "B");//decimalFormat(num/1000000000.00,len)+"B";
                } else if (absNum >= 1000000) {
                    newVolume = unitConvert(num, 1000000.00, len, "M");//decimalFormat(num/1000000.00,len)+"M";
                } else if (absNum >= 10000) //10000以下不用转换为K
                {
                    newVolume = unitConvert(num, 1000.00, len, "K");//decimalFormat(num/1000.00,len)+"K";
                } else {
                    newVolume = "" + (int) num;
                }
            } else {
                long inum = (long) num;
                long b = inum / 1000;
                while (b > 0) {
                    newVolume = "," + fillLeftByZero(inum % 1000, 3) + newVolume;
                    inum = inum / 1000;
                    b = inum / 1000;
                }
                if (!"".equals(newVolume)) {
                    newVolume = inum + newVolume;
                } else {
                    newVolume = "" + inum;
                }
            }
        }
        return newVolume;
    }

    private static String unitConvert(double num, double unit, int len, String unitText) {
        double d = num / unit;
        String str = String.valueOf(d);
        int idx = str.indexOf(".");
        if (idx > -1) {
            idx = idx + 1;
            String substr = str.substring(idx);
            if (substr.length() > len) {
                StringBuffer sb = new StringBuffer("0.");
                for (int k = 0; k < len; k++) {
                    sb.append("0");
                }
                sb.append("5");
                d = d + Double.valueOf(sb.toString());
                str = String.valueOf(d);
                int strLen = idx + len;
                strLen = strLen > str.length() ? str.length() : strLen;
                str = str.substring(0, strLen);
            }
        }
        int i = str.length() - 1;
        for (; i >= 0; i--) {
            char c = str.charAt(i);
            if (c > '0' && c <= '9') {
                i = i + 1;
                break;
            } else if (c == '.') {
                break;
            }
        }
        if (i >= 0 && i < str.length()) {
            str = str.substring(0, i);
        }
        return str + unitText;
    }

    /**
     * 增幅
     *
     * @param theData  当前数
     * @param lastData 上次数
     * @param len      小数点后保留的位数
     * @return
     */
    public static String getDataExtent(double theData, double lastData, int len) {
        String data = "-";
        if (theData != 0 && lastData != 0) {
            double diff = (theData - lastData);
            data = decimalFormat(diff / lastData * 100, len);
            double d = getDouble(data, 0);
            if (d > 0) {
                data = "+" + data;
            }
            if (d != 0) {
                data = data + "%";
            }
        }
        return data;
    }

    /**
     * 涨跌
     *
     * @param theData  当前数
     * @param lastData 上次数
     * @param len      小数点后保留的位数
     * @return
     */
    public static String getUpDown(double theData, double lastData, int len) {
        String data = "-";
        if (theData != 0 && lastData != 0) {
            double diff = (theData - lastData);
            data = decimalFormat(diff, len);
            if (diff > 0) {
                data = "+" + data;
            }
        }
        return data;
    }

    private static String fillLeftByZero(long num, int len) {
        String str = "" + num;
        while (str.length() < len) {
            str = "0" + str;
        }
        return str;
    }

    /**
     * 提供精確的加法運算
     *
     * @param
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供了精確的減法運算
     *
     * @param
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 去掉小数点位
     *
     * @param d
     * @return
     * @author Wison
     */
    public static String backPoint(double d) {
        String s = "" + d;
        return backPoint(s);
    }

    /**
     * 去掉小数点位
     *
     * @param d
     * @return
     * @author Wison
     */
    public static String backPoint(String d) {
        String str = d;
        int pointIndex = d.lastIndexOf(".");
        if (pointIndex > 0) {
            char lastChar = d.charAt(d.length() - 1);
            if (lastChar >= '0' && lastChar <= '9') {
                str = d.substring(0, pointIndex);
            }
        }
        return str;
    }

    /**
     * 去掉小数点后面的0， 如 12.030 处理后为12.03
     *
     * @param d
     * @return
     * @author Wison
     */
    public static String backPointZero(double d) {
        String s = "" + d;
        return backPointZero(s);
    }

    /**
     * 去掉小数点后面的0， 如 12.030 处理后为12.03 15.00%处理后为15%
     *
     * @param num 字符串化的浮点型或百分比数据
     * @return
     * @author Wison
     */
    public static String backPointZero(String num) {
        if (num == null) {
            return null;
        }
        String s = num;
        boolean isPercentage = false;
        int percentIndex = s.lastIndexOf("%");
        if (percentIndex > -1) {
            isPercentage = true;
            s = s.substring(0, percentIndex);
        }
        int pointIndex = num.indexOf(".");
        if (pointIndex > 0) {
            for (int i = s.length() - 1; i > 0; i--) {
                char c = s.charAt(i);
                if ('0' == c && i > pointIndex) {
                    s = s.substring(0, i);
                    continue;
                }
                if ('.' == c) {
                    s = s.substring(0, i);
                }
                break;
            }
        }
        if (isPercentage) {
            s += "%";
        }
        return s;
    }

    /***
     *
     * 移除数量元素
     *
     * @param sourceStringArray  原数组
     * @param indexs   5,4,3,2,1  从大到小排列
     * @return
     *
     */
    public static String[] removeStringArrayIndex(String[] sourceStringArray, String indexs) {
        String[] indexArray = indexs.split(",");
        int len = sourceStringArray.length - indexArray.length;
        String[] stringArray = new String[len];
        int index = 0;
        for (int i = 0; i < sourceStringArray.length; i++) {
            boolean isContinue = false;
            for (int j = 0; j < indexArray.length; j++) {
                if (i == NumberUtil.getInt(indexArray[j], 0, 10)) {
                    isContinue = true;
                }
            }
            if (isContinue) {
                continue;
            }
            stringArray[index] = sourceStringArray[i];
            index++;
        }
        return stringArray;
    }

    /**
     * 将科学计数法表示的double值还原成原始值
     */
    public static String formatDouble(double number) {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(number);
    }

}
