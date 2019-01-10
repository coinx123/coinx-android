package com.coin.libbase.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleUtils {

    public static String formatTwoDecimalString(double d) {
        return formatDecimalFixed(d, 2);
    }

//    public static String formatFourDecimalString(double d) {
//        return formatDecimalFixed(d, 4);
//    }

    public static double formatTwoDecimal(double d) {
        return formatDecimal(d, 2);
    }

    /**
     * 固长小数点
     *
     * @param d
     * @param length
     * @return
     */
    public static String formatDecimalFixed(double d, int length) {
        return String.format("%." + length + "f", d);
    }

    /**
     * 非固长小数
     *
     * @param d
     * @param length
     * @return
     */
    public static double formatDecimal(double d, int length) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String formatDecimalFloor(double d, int length) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(length, BigDecimal.ROUND_FLOOR).toPlainString();
    }

    public static String formatSixDecimalString(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0#####");
        return decimalFormat.format(d);
    }

    public static String formatFourDecimalString(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0###");
        return decimalFormat.format(d);
    }

    public static String showAllDecimalString(double d) {
        return BigDecimal.valueOf(d).toPlainString();
    }

    public static void main(String[] args) {
        System.out.println(formatTwoDecimalString(1.1));
        System.out.println(formatTwoDecimalString(0));
        System.out.println(formatTwoDecimalString(0.00001));
        System.out.println(formatTwoDecimalString(0.011));
        System.out.println(formatTwoDecimalString(0.015));

        System.out.println(formatTwoDecimal(1.1));
        System.out.println(formatTwoDecimal(0));
        System.out.println(formatTwoDecimal(0.00001));
        System.out.println(formatTwoDecimal(0.011));
        System.out.println(formatTwoDecimal(0.015));

    }

}