package com.hshc.sale.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tools {

    // 日期格式
    private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");

    // 18位身份证号码检查用
    public static String getId18CheckCode(String sPre17ID) throws Exception {
        int[] nCertiCheckW = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        int nCount = 0;
        int nIdNum = 0;
        if ((sPre17ID == null) || (sPre17ID.length() != 17)) {
            throw new Exception("Invalid certi code length");
        }
        for (int i = 0; i < 17; i++) {
            char c = sPre17ID.charAt(i);
            if ((c <= '9') || (c >= '0')) {
                nIdNum = c - '0';
            } else {
                throw new Exception("Invalid Certi Code char");
            }
            nCount += nIdNum * nCertiCheckW[i];
        }
        nCount = nCount % 11;
        switch (nCount) {
            case 0:
                return "1";
            case 1:
                return "0";
            case 2:
                return "X";
            case 3:
                return "9";
            case 4:
                return "8";
            case 5:
                return "7";
            case 6:
                return "6";
            case 7:
                return "5";
            case 8:
                return "4";
            case 9:
                return "3";
            case 10:
                return "2";
            default:
                return "";
        }
    }

    public static String toGB(String inStr) {
        try {

            if (inStr == null) return "";
            return inStr;

        } catch (Exception e) {
            return "";
        }
    }

    public static String fromGB(String inStr) {
        try {

            if (inStr == null) return "";
            return (new String(inStr.getBytes("UTF-8"), "8859_1"));
        } catch (Exception e) {

            return "";
        }
    }

    public static String fromGB(byte[] inBytes) {
        try {

            if (inBytes == null) return "";
            return (new String(inBytes, "8859_1"));

        } catch (Exception e) {
            return "";
        }
    }

    public static long toLong(String inStr) {
        try {
            if (inStr == null) {
                return 0;
            } else {
                return Long.valueOf(inStr).longValue();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static int toInteger(String inStr) {
        try {
            if (inStr == null) {
                return 0;
            } else {
                return new Integer(inStr).intValue();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static double toDouble(String inStr) {
        try {
            if (inStr == null) {
                return 0;
            } else {
                return Double.valueOf(inStr).doubleValue();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static float toFloat(String inStr) {
        try {
            if (inStr == null) {
                return 0;
            } else {
                return Float.valueOf(inStr).floatValue();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static String msNull(String inStr) {
        if (inStr == null) {
            return "";
        } else {
            return inStr;
        }
    }

    public static Object msNull(Object inStr) {
        if (inStr == null) {
            return "";
        } else {
            return inStr;
        }
    }

    public static BigDecimal getBigDecimal(String str) {
        BigDecimal bd = null;
        if (str == null) return null;
        try {
            bd = new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
        return bd;
    }

    // 根据年，月，日，转化为Timestamp类型
    public static Timestamp getTimestamp(String sDate) {
        Timestamp ts = null;
        if (sDate == null || "".equals(sDate)) return null;
        ts = Timestamp.valueOf(sDate + " 00:00:00.000000000");
        return ts;
    }

    public static Timestamp getEndTimestamp(String sDate) {
        Timestamp ts = null;
        if (sDate == null || "".equals(sDate)) return null;
        ts = Timestamp.valueOf(sDate + " 23:59:59.999999999");
        return ts;
    }

    public static String Date2String(Date date) {
        if (date == null) return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static Date toDate(String sDate) throws java.text.ParseException {
        Date result = null;
        if (sDate == null) result = null;
        else if (sDate.length() == 10 && sDate.indexOf("-") == 4) {
            result = dateFormat1.parse(sDate);
        } else if (sDate.length() == 8) {
            result = dateFormat2.parse(sDate);
        }
        return result;
    }

    public static Timestamp addDate(Timestamp oldDate, int addDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(oldDate.getTime()));
        calendar.add(calendar.DATE, addDays);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static Timestamp addMonth(Timestamp oldDate, int addMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(oldDate.getTime()));
        calendar.add(calendar.MONTH, addMonths);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static Timestamp addYear(Timestamp oldDate, int addYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(oldDate.getTime()));
        calendar.add(calendar.YEAR, addYears);
        return new Timestamp(calendar.getTime().getTime());
    }

    public static Date toDate(int year, int month, int date) {
        return toDate(year, month, date, 0, 0, 0);
    }

    public static Date toDate(int year, int month, int date, int hrs, int min, int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, hrs, min, sec);
        return calendar.getTime();
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getYear(long date) {
        return getYear(new Date(date));
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(long date) {
        return getMonth(new Date(date));
    }

    public static int getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDate(long date) {
        return getDate(new Date(date));
    }

    public static int getHours(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR);
    }

    public static int getMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSeconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    public static String getChineseDate(Date d) {
        if (d == null) {
            return " 年 月 日";
        } else {
            return "" + getYear(d) + "年" + getMonth(d) + "月" + getDate(d) + "日";
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof Date) {
            return Date2String((Date) obj);
        } else {
            return obj.toString();
        }
    }

    public static double getDoubleDischargeTail(double dOrigin, int nCount) {
        return getDouble(dOrigin, nCount, true);
    }

    public static double getDoubleNotDischargeTail(double dOrigin, int nCount) {
        return getDouble(dOrigin, nCount, false);
    }

    public static double getDouble(double dOrigin, int nCount, boolean bDischarge) {
        long lTemp = (long) Math.pow(10, nCount);
        if (bDischarge == true) return (long) (dOrigin * lTemp) / (double) lTemp;
        else return Math.round(dOrigin * lTemp) / (double) lTemp;
    }

    public static int getMonthAmount(Date startDate, Date endDate) {
        int nYear = 0;
        int nMonth = 0;
        int nDay = 0;
        int nMonthAmount = 0;
        Calendar cldStart = Calendar.getInstance();
        Calendar cldEnd = Calendar.getInstance();

        cldStart.setTime(startDate);
        cldEnd.setTime(endDate);

        nYear = cldEnd.get(cldEnd.YEAR) - cldStart.get(cldStart.YEAR);
        nMonth = cldEnd.get(cldEnd.MONTH) - cldStart.get(cldStart.MONTH);
        nDay = cldEnd.get(cldEnd.DATE) - cldStart.get(cldStart.DATE);

        if (nDay > 14) nMonthAmount = nYear * 12 + nMonth + 1;
        else nMonthAmount = nYear * 12 + nMonth;
        return nMonthAmount;
    }

    public static int getDayAmount(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String doubleToStr(double d) {
        String str = NumberFormat.getInstance().format(d);
        String str2 = "";
        int i = 0;
        while ((i >= 0) && str.length() > 0) {
            i = str.indexOf(",");
            if (i == -1) {
                str2 += str;
                break;
            } else {
                str2 += str.substring(0, i);
                str = str.substring(i + 1, str.length());
            }
        }
        return str2;
    }

    static public String emptyStringToNull(String value) {
        if (value == null) return null;
        value = value.trim();
        if (value.length() == 0) return null;
        return value;
    }

    static public String dateToString(Date date, String division) {
        if (date == null) return null;
        if (division == null) division = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String result = "" + year;
        result += division;
        if (month < 10) result += "0";
        result += month;
        result += division;
        if (day < 10) result += "0";
        result += day;
        return result;
    }

    /**
     * 将日期格式转换为yyyy-mm-dd格式的String
     * 
     * @parameter division分割符
     */
    static public String dateToString(Timestamp date, String division) {
        if (date == null) return null;
        return dateToString(new Date(date.getTime()), division);
    }

    /**
     * 将日期格式转换为yyyy-mm-dd格式的String
     * 
     * @parameter division分割符
     */
    static public String dateToString(long date, String division) throws Exception {
        return dateToString(new Date(date), division);
    }

    /**
     * 判断一个数是否是0
     * 
     * @param value 要判断的数
     * @param digits 判断到小数后的位数
     * @return 是否是0
     */
    public static boolean isZero(double value, int digits) {
        return getDouble(value, digits, false) == 0;
    }

    /**
     * 获取obj的String表达,当obj为null时，返回defaultValue
     * 
     * @param obj
     * @param defaultValue
     * @return
     */
    public static String getObjectString(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else {
            return obj.toString();
        }
    }

    public static String processStrings(String str) {
        if (str != null) {
            str = replaceString("&", "&amp;", str);
            str = replaceString(" ", "&nbsp;", str);
            str = replaceString("<", "&lt;", str);
            str = replaceString(">", "&gt;", str);
            str = replaceString("\r\n", "<br>", str);
            str = replaceString("\"", "&quot;", str);

            return (str);
        } else return (str);
    }

    public static String replaceString(String oldStr, String newStr, String wholeStr) {
        if (wholeStr == null) return "";

        if (oldStr == null) return wholeStr;
        if (newStr == null) return wholeStr;
        int start, end;
        StringBuffer result = new StringBuffer();
        result = result.append(wholeStr);
        start = 0;

        while (wholeStr.indexOf(oldStr, start) > -1) {
            start = wholeStr.indexOf(oldStr, start);
            end = start + oldStr.length();
            result.replace(start, end, newStr);
            wholeStr = result.toString();
            start += newStr.length();
        }
        return wholeStr;
    }

    /**
     * 四舍五入取digits位小数
     * 
     * @param value
     * @param digits 小数位数
     * @return String
     */
    public static String roundString(double value, int digits) {
        String format = "#";
        if (digits > 0) {
            format += ".";
        }
        for (int i = 0; i < digits; i++) {
            format += "#";
        }
        DecimalFormat numberFormatter = new DecimalFormat(format);
        return numberFormatter.format(value);
    }

    /**
     * 将GBK编码的字符串转化为Unicode的字符串
     * 
     * @param pGbkString
     * @return
     */
    public static String gbkToUnicode(String pGbkString) {
        String sResult = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter os = new OutputStreamWriter(baos, "UTF-8");
            os.write(pGbkString);
            os.close();
            sResult = baos.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sResult;
    }

    /**
     * 将Unicode编码的字符串转化为GBK的字符串
     * 
     * @param pGbkString
     * @return
     */
    public static String unicodekToGBK(String pUnicodeString) {
        String sResult = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter os = new OutputStreamWriter(baos, "GBK");
            os.write(pUnicodeString);
            os.close();
            sResult = baos.toString("GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sResult;
    }

    /**
     * 将GBK编码的字符串转化为Unicode的字符串
     * 
     * @param pGbkString
     * @return
     */
    public static char[] gbkToUnicode(char[] pGbkString) {
        char[] sResult = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter os = new OutputStreamWriter(baos, "UTF-8");
            os.write(pGbkString);
            os.close();
            sResult = baos.toString("UTF-8").toCharArray();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return sResult;
    }

    /**
     * 将double转化为String
     * 
     * @param d double数据
     * @param limit 小数位数
     * @param hasComma 生成的String是否使用逗号(,)分隔
     * @param bDischarge 是否不采用四舍五入。=true 直接去为尾
     * @return 转化后的String
     */
    public static String doubleToStr(double d, int limit, boolean hasComma, boolean bDischarge) {
        d = getDouble(d, limit, bDischarge);
        String sFormat = "#";
        if (hasComma) {
            sFormat += ",";
        }
        sFormat += "##0";
        if (limit > 0) {
            sFormat += ".";
            for (int i = 0; i < limit; i++) {
                sFormat += "0";
            }
        }
        DecimalFormat numberFormatter = new DecimalFormat(sFormat);
        return numberFormatter.format(d);
    }

    /**
     * 填充指定长度的字符至源字符串前
     * 
     * @param sourceValue 源字串
     * @param length 格式长度
     * @param c 填充字符
     */
    public static String beforFilling(String sourceValue, int length, char c) {
        StringBuffer fill = new StringBuffer(1024);
        int currentLength = 0;
        if (sourceValue != null) currentLength = sourceValue.length();
        for (int i = 0; i < length - currentLength; i++)
            fill.append(c);
        return sourceValue == null ? fill.toString() : fill.toString() + sourceValue;
    }

    /**
     * 填充指定长度的字符至源字符串尾
     * 
     * @param value 源字串
     * @param length 格式长度
     * @param c 填充字符
     */
    public static String afterFilling(String value, int length, char c) {
        StringBuffer fill = new StringBuffer(1024);
        int currentLength = 0;
        if (value != null) currentLength = value.length();
        for (int i = 0; i < length - currentLength; i++)
            fill.append(c);
        return value == null ? fill.toString() : value + fill.toString();
    }

    public static String filterOracleSqlString(String pOrgString) {
        String sResult = pOrgString;
        if (sResult == null) {
            sResult = "";
        }
        sResult = Tools.replaceString("'", "''", sResult);
        return sResult;
    }

    /**
     * ZTREE拼json字符串
     * 
     * @param list 源字串
     * @return json字符串
     */
    public static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {

                json.append(objectToJson(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String objectToJson(Object object) {
        StringBuilder json = new StringBuilder();
        if (object == null) {
            json.append("\"\"");
        } else if (object instanceof String || object instanceof Integer || object instanceof Boolean) {
            json.append("\"").append(object.toString()).append("\"");
        } else if (object instanceof Boolean) {
            json.append(object);
        } else {
            json.append(beanToJson(object));
        }
        // System.out.println( json.toString());
        return json.toString();
    }

    public static String beanToJson(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = objectToJson(props[i].getName());
                    String value = objectToJson(props[i].getReadMethod().invoke(bean));
                    // System.out.println(name.substring(1, name.length()-1));
                    if ("\"name\"".equals(name)) {
                        json.append("name");
                        json.append(":");
                        json.append(value);
                        json.append(",");
                    } else if ("\"id\"".equals(name) || "\"pId\"".equals(name)) {
                        json.append(name.substring(1, name.length() - 1));
                        json.append(":");
                        json.append(value);
                        json.append(",");
                    } else {
                        json.append(name.substring(1, name.length() - 1));
                        json.append(":");
                        json.append(value.substring(1, value.length() - 1));
                        json.append(",");
                    }

                } catch (Exception e) {
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }
}
