package com.coin.exchange.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 对字符串的相关操作， 包括 数字类和String类的互转， 格式化等。该类主要是为了避免一些 Exception
 *
 * @author
 */
public class StringHelper {
    public static final String number_regex = "^(-?\\d+)(\\.\\d+)?$";

    /**
     * 格式化输出浮点型数据
     *
     * @param value     需要格式化的浮点数据
     * @param precision 小数点后保留的位数
     * @return
     */
    public static String decimalFormat(double value, int precision) {
        String newDecimal = null;
        if (value != 0) {
            StringBuffer format = new StringBuffer("0");
            for (int i = 0; i < precision; i++) {
                if (i == 0) {
                    format.append(".");
                }
                format.append("0");
            }
            DecimalFormat decimalFormat = new DecimalFormat(format.toString());
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);
            newDecimal = decimalFormat.format(value);
        } else {
            if (precision == 0) {
                newDecimal = "0";
            } else {
                newDecimal = "0.0";
            }
        }
        return newDecimal;
    }


    /**
     * 格式化输出浮点型数据(不进行分隔)
     *
     * @param value     需要格式化的浮点数据
     * @param precision 小数点后保留的位数
     * @return
     */
    public static String decimalFormatNoGrouping(double value, int precision) {
        String newDecimal = null;
        if (value != 0) {
            StringBuffer format = new StringBuffer("0");
            for (int i = 0; i < precision; i++) {
                if (i == 0) {
                    format.append(".");
                }
                format.append("0");
            }
            DecimalFormat decimalFormat = new DecimalFormat(format.toString());
            decimalFormat.setGroupingUsed(true);
            //decimalFormat.setGroupingSize(3);
            newDecimal = decimalFormat.format(value);
        } else {
            if (precision == 0) {
                newDecimal = "0";
            } else {
                newDecimal = "0.0";
            }
        }
        return newDecimal;
    }


    /**
     * 将字符串转换为浮点型数值
     *
     * @param value    要转换的字符串
     * @param defValue 默认返回的浮点值
     * @return 转换后的浮点数值
     */
    public static double getDouble(String value, double defValue) {
        double v = defValue;
        try {
            v = Double.parseDouble(value);
        } catch (Exception e) {
            v = defValue;
        }
        return v;
    }


    /**
     * 将字符串转换为浮点型数值
     *
     * @param value    要转换的字符串
     * @param defValue 默认返回的浮点值
     * @return 转换后的浮点数值
     */
    public static float getFloat(String value, float defValue) {
        float v = defValue;
        try {
            v = Float.parseFloat(value);
        } catch (Exception e) {
            v = defValue;
        }
        return v;
    }


    /**
     * 按指定进制将字符串转换为整型数值
     *
     * @param s 要转换的字符串
     * @param d 默认返回的整型值
     * @return 转换后的整数值
     */
    public static int getInt(String s, int d) {
        int v = d;
        try {
            v = Integer.parseInt(s);
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
     * 截取数字字符串的整数部分</br>
     * 仅仅根据"." 进行截取， 并不保证一定返回数字
     *
     * @param value
     * @return
     */
    public static String cutInteger(String value) {
        if (value == null)
            return "0";

        if (value.substring(0, 1).equals(".")) {
            value = "0" + value;
        }

        int divide = value.indexOf(".");
        return divide > 0 ? value.substring(0, divide) : value;
    }


    /**
     * 截取数字字符串， 最多保留精度位小数</br>
     * 仅仅根据"." 进行截取， 并不保证一定返回数字
     *
     * @param value
     * @return
     */
    public static String cutDecimal(String value, int precision) {
        if (value == null)
            return "0";

        try {
            if (value.substring(0, 1).equals(".")) {
                value = "0" + value;
            }

            int divide = value.indexOf(".");
            // 没有小数点
            if (divide < 0)
                return value;
                // 小数点后面的尾数大于或等于 精度位
            else if (value.length() - 1 - divide >= precision)
                return value.substring(0, divide + precision + 1);
                // 小数点后面的尾数小于精度位
            else {
                String result = value;
                for (int i = 0; i < (precision - (value.length() - 1 - divide)); i++) {
                    result = result + "0";
                }
                return result;
            }
        } catch (Exception e) {
            return "0";

        }


    }

    /**
     * 改变数字字符串， 保持精度位小数位</br>
     * 根据"." 进行截取，
     *
     * @param value
     * @return
     */
    public static String keepDecimal(String value, int precision) {
        if (value == null || value.equals("")) {
            //return "0";
            String result = "0.";
            for (int i = 0; i < precision; i++) {
                result = result + "0";
            }
            return result;
        }
        value = value.replace(",", "");
        try {
            if (value.substring(0, 1).equals(".")) {
                value = "0" + value;
            }
            if (value.length() >= 2 && value.substring(0, 2).equals("-.")) {
                value = new StringBuilder(value).insert(1, "0").toString();
            }

            int divide = value.indexOf(".");
            // 没有小数点
            if (divide < 0) {
                String result = value + ".";
                for (int i = 0; i < precision; i++) {
                    result = result + "0";
                }
                return result;
            }
            // 小数点后面的尾数大于或等于 精度位
            else if (value.length() - 1 - divide >= precision) {
                String result = value.substring(0, divide + precision + 1);
                return result;
            }
            // 小数点后面的尾数小于精度位
            else {
                String result = value;
                for (int i = 0; i < (precision - (value.length() - 1 - divide)); i++) {
                    result = result + "0";
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            String result = "0.";
            for (int i = 0; i < precision; i++) {
                result = result + "0";
            }
            return result;
        }

    }

    /**
     * 删除number开头包含c的字符
     *
     * @param number
     * @param number
     * @return
     */
    public static String deleteStartChar(String number, char c) {
        int len = number.length();
        for (int i = 0; i < len; i++) {
            if (number.charAt(i) != c) {
                return number.substring(i);
            }
        }
        return number;
    }

    public static String formatPrice(String price) {

        try {
            float fprice = Float.parseFloat(price);
            return new DecimalFormat("0.00##").format(fprice);
        } catch (Exception e) {
            return price;
        }
    }

    /**
     * 大数据加法
     */
    public static String add(String s1, String s2) {
        if (s1 == null || s1.equals("")) {
            s1 = "0";
        }

        if (s2 == null || s2.equals("")) {
            s2 = "0";
        }
        s1 = s1.replace(",", "");
        s2 = s2.replace(",", "");
        try {
            BigDecimal b1 = new BigDecimal(s1);
            BigDecimal b2 = new BigDecimal(s2);
            return b1.add(b2).toPlainString();
        } catch (Exception e) {
            return "--";
        }
    }

    /**
     * 大数据减法
     */
    public static String sub(String s1, String s2) {
        if (s1 == null || s1.equals("")) {
            s1 = "0";
        }

        if (s2 == null || s2.equals("")) {
            s2 = "0";
        }
        s1 = s1.replace(",", "");
        s2 = s2.replace(",", "");
        try {
            BigDecimal b1 = new BigDecimal(s1);
            BigDecimal b2 = new BigDecimal(s2);
            return b1.subtract(b2).toPlainString();
        } catch (Exception e) {
            return "--";
        }
    }

    /**
     * 大数据乘法
     */
    public static String multiply(String s1, String s2) {
        if (s1 == null || s1.equals("")) {
            s1 = "0";
        }

        if (s2 == null || s2.equals("")) {
            s2 = "0";
        }
        s1 = s1.replace(",", "");
        s2 = s2.replace(",", "");
        try {
            BigDecimal b1 = new BigDecimal(s1);
            BigDecimal b2 = new BigDecimal(s2);
            return b1.multiply(b2).toPlainString();
        } catch (Exception e) {
            return "--";
        }
    }

    /**
     * 大数据除法
     */
    public static String divide(String s1, String s2, int precision) {
        if (s1 == null || s1.equals("")) {
            s1 = "0";
        }

        if (s2 == null || s2.equals("")) {
            s2 = "1";
        }
        s1 = s1.replace(",", "");
        s2 = s2.replace(",", "");

        try {
            BigDecimal b1 = new BigDecimal(s1);
            BigDecimal b2 = new BigDecimal(s2);
            return b1.divide(b2, precision, BigDecimal.ROUND_HALF_UP).toPlainString();
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 用正则表达式匹配，是否为数字字符串(包含小数点和正负号)
     */
    public static boolean isAllNum(String src) {
        try {
            Double.parseDouble(src.replace(",", ""));
        } catch (Exception e) {
            return false;
        }
        return true;
//		String pattern = "([-\\+]?[0-9]([0-9]*)(\\.[0-9]+)?)|(^0$)";
//		Pattern pat = Pattern.compile(pattern);
//		Matcher mat = pat.matcher(src);
//		boolean result = mat.matches();
    }


    /***
     * 确保正确的四舍五入（防止因计算机中double的精度丢失而四舍五入不准确问题）
     * @param value
     * @param decimal
     * @return
     */
    public static String formatDecimalReal(String value, int decimal) {
        String resutl = "";
        try {
            resutl = new BigDecimal(value).setScale(decimal, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return resutl;

    }
}
