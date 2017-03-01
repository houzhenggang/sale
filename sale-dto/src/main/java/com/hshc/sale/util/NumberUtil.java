package com.hshc.sale.util;

import java.math.BigDecimal;

public class NumberUtil {

    public static long toLong(String inStr) {
        return Tools.toLong(inStr);
    }

    public static int toInteger(String inStr) {
        return Tools.toInteger(inStr);
    }

    public static double toDouble(String inStr) {
        return Tools.toDouble(inStr);
    }

    public static float toFloat(String inStr) {
        return Tools.toFloat(inStr);
    }

    /**
     * 根据输入的String返回BigDecimal，或者若String非数字串，返回null
     * 
     * @param str
     * @return
     */
    public static BigDecimal toBigDecimal(String str) {
        return Tools.getBigDecimal(str);
    }

    /**
     * 根据输入的String返回BigDecimal，或者若String非数字串，返回null
     * 
     * @deprecated,不推荐使用，尽量使用NumberUtil.toBigDecimal方法
     * @param str
     * @return
     */
    public static BigDecimal getBigDecimal(String str) {
        return Tools.getBigDecimal(str);
    }

    /**
     * Function name: getDouble()
     * 
     * @deprecated,不推荐使用，尽量使用NumberUtil.trunc()方法
     *                                            Description: covert double to double
     *                                            Input: ORIGIN double, count, bDischarge
     *                                            Output: final double
     *                                            Author: Richard Zhang
     *                                            Date: Aug 15,2001
     */
    public static double getDoubleDischargeTail(double dOrigin, int nCount) {
        return Tools.getDoubleDischargeTail(dOrigin, nCount);
    }

    /**
     * 保留指定小数位数的四舍五入方法
     * 
     * @deprecated,不推荐使用，尽量使用NumberUtil.round()方法
     * @param dOrigin
     * @param nCount
     * @return
     */
    public static double getDoubleNotDischargeTail(double dOrigin, int nCount) {
        return Tools.getDoubleNotDischargeTail(dOrigin, nCount);
    }

    /**
     * 默认保留两位小数的四舍五入方法
     * 
     * @param amount
     * @return double
     */
    public static double round(double amount) {
        return Tools.getDoubleNotDischargeTail(amount, 2);
    }

    /**
     * 保留指定小数位数的四舍五入方法
     * 
     * @param amount
     * @param digits
     * @return
     */
    public static double round(double amount, int digits) {
        return Tools.getDoubleNotDischargeTail(amount, digits);
    }

    /**
     * 默认截取两位小数方法
     * 
     * @param amount
     * @return double
     */
    public static double trunc(double amount) {
        return Tools.getDoubleDischargeTail(amount, 2);
    }

    /**
     * 截取指定小数位数的方法
     * 
     * @param amount
     * @param digits
     * @return
     */
    public static double trunc(double amount, int digits) {
        return Tools.getDoubleDischargeTail(amount, digits);
    }

    /**
     * 默认保留指定小数位数方法
     * 
     * @deprecated,不推荐使用，尽量使用NumberUtil.round()方法
     * @param dOrigin
     * @param nCount
     * @param bDischarge (true 截位, false 四舍五入)
     * @return
     */
    public static double getDouble(double dOrigin, int nCount, boolean bDischarge) {
        return Tools.getDouble(dOrigin, nCount, bDischarge);
    }

    /**
     * Function name: doubleToStr()
     * 
     * @deprecated,不推荐使用，使用FormatDisplay.toDecimal();
     *                                                Description: 解决科学计数法问题
     *                                                Input: double
     *                                                Output: 该double对应的字符串
     *                                                Author: ernest
     *                                                Date: Sept 2,2001
     */
    public static String doubleToStr(double d) {
        return Tools.doubleToStr(d);
    }

    /**
     * 判断一个数是否是0
     * 
     * @param value 要判断的数
     * @param digits 判断到小数后的位数
     * @return 是否是0
     */
    public static boolean isZero(double value, int digits) {
        return Tools.isZero(value, digits);
    }

    /**
     * 四舍五入取digits位小数
     * 
     * @deprecated,不推荐使用，尽量使用round或FormatDisplay.toDecimal();
     *                                                        @param value
     * @param digits 小数位数
     * @return String
     */
    public static String roundString(double value, int digits) {
        return Tools.roundString(value, digits);
    }

    /**
     * 将double转化为String
     * 
     * @deprecated,不推荐使用，使用NumberUtil.round()+FormatDisplay.toDecimal();
     *                                                                   @param d double数据
     * @param limit 小数位数
     * @param hasComma 生成的String是否使用逗号(,)分隔
     * @param bDischarge 是否不采用四舍五入。=true 直接去为尾
     * @return 转化后的String
     */
    public static String doubleToStr(double d, int limit, boolean hasComma, boolean bDischarge) {
        return Tools.doubleToStr(d, limit, hasComma, bDischarge);
    }

    /**
     * 合并两个数组到新数组
     * 
     * @param array1
     * @param array2
     * @return
     */
    public static long[] mergeArray(long[] array1, long[] array2) {
        int mergeLength = (array1 != null ? array1.length : 0) + (array2 != null ? array2.length : 0);
        long[] mergeArray = null;
        if (mergeLength > 0) {
            mergeArray = new long[(int) mergeLength];
            int pos = 0;
            if (array1 != null) {
                for (pos = 0; pos < array1.length; pos++) {
                    mergeArray[pos] = array1[pos];
                }
            }
            if (array2 != null) {
                for (int i = 0; i < array2.length; i++) {
                    mergeArray[i + pos] = array2[i];
                }
            }
        }
        return mergeArray;
    }

    /**
     * 过滤数组
     * 
     * @param array
     * @param filterArray
     * @return
     */
    public static long[] filterArray(long[] array, long[] filterArray) {
        int outLength = (array != null ? array.length : 0) - (filterArray != null ? filterArray.length : 0);
        long[] outArray = null;
        if (outLength > 0) {
            outArray = new long[(int) outLength];
            int i = 0;
            for (int pos = 0; array != null && pos < array.length; pos++) {
                boolean equals = false;
                for (int j = 0; filterArray != null && j < filterArray.length; j++) {
                    if (array[pos] == filterArray[j]) equals = true;
                }
                if (!equals) outArray[i++] = array[pos];
            }
        }
        return outArray;
    }

    public static void main(String[] argv) {
        long[] a1 = {1, 2, 3, 4, 5, 6};
        long[] a2 = null;
        long[] a3 = filterArray(a1, a2);
        for (int i = 0; i < a3.length; i++) {
            System.out.println(a3[i]);
        }
    }

}
