
package com.hshc.sale.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public final class BeanUtil {
    public static Object convert(Class classToConvert, Object source) {
        if (source == null || "".equals(source)) {
            return null;
        }
        if (!classToConvert.equals(String.class) && "".equals(source)) {
            return null;
        }
        try {
            if (classToConvert.equals(source.getClass())) {
                return source;
            }

            if (classToConvert.equals(String.class)) {
                return source.toString();
            }
            if (classToConvert.equals(Integer.class)) {
                return new Integer(NumberUtil.toInteger(source.toString()));
            }
            if (classToConvert.equals(Long.class)) {
                return new Long(NumberUtil.toLong(source.toString()));
            }
            if (classToConvert.equals(Float.class)) {
                return new Float(NumberUtil.toFloat(source.toString()));
            }
            if (classToConvert.equals(Double.class)) {
                return new Double(NumberUtil.toDouble(source.toString()));
            }
            if (classToConvert.equals(int.class)) {
                return new Integer(NumberUtil.toInteger(source.toString()));
            }
            if (classToConvert.equals(long.class)) {
                return new Long(NumberUtil.toLong(source.toString()));
            }
            if (classToConvert.equals(float.class)) {
                return new Float(NumberUtil.toFloat(source.toString()));
            }
            if (classToConvert.equals(double.class)) {
                return new Double(NumberUtil.toDouble(source.toString()));
            }
            if (classToConvert.equals(BigDecimal.class)) {
                return NumberUtil.getBigDecimal(source.toString());
            }

            if (classToConvert.equals(Timestamp.class)) {
                if (source == null) {
                    return null;
                } else if (source instanceof Date) {
                    return new Timestamp(((Date) source).getTime());
                } else {
                    return DateUtil.stringToTimestamp(source.toString());
                }
            }

            if (classToConvert.equals(Date.class)) {
                if (source == null) {
                    return null;
                } else if (source.getClass().equals(Timestamp.class)) {
                    return new Date(((Timestamp) source).getTime());
                } else {
                    return new Date(DateUtil.stringToTimestamp(source.toString()).getTime());
                }
            }

            if (classToConvert.equals(boolean.class)) {
                return new Boolean("true".equals(source.toString()) ? true : false);
            }
            // array support
            if (classToConvert.equals(String[].class)) {
                String[] returnValue = {source.toString()};
                return returnValue;
            }
            if (classToConvert.equals(int[].class)) {
                int[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new int[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = NumberUtil.toInteger(((String[]) source)[i]);
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new int[1];
                        returnValue[0] = NumberUtil.toInteger((String) source);
                    }
                }
                return returnValue;
            }
            if (classToConvert.equals(long[].class)) {
                long[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new long[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = NumberUtil.toLong(((String[]) source)[i]);
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new long[1];
                        returnValue[0] = NumberUtil.toLong((String) source);
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(float[].class)) {
                float[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new float[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = NumberUtil.toFloat(((String[]) source)[i]);
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new float[1];
                        returnValue[0] = NumberUtil.toFloat((String) source);
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(double[].class)) {
                double[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new double[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = NumberUtil.toDouble(((String[]) source)[i]);
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new double[1];
                        returnValue[0] = NumberUtil.toDouble((String) source);
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(Integer[].class)) {
                Integer[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new Integer[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = new Integer(NumberUtil.toInteger(((String[]) source)[i]));
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new Integer[1];
                        returnValue[0] = new Integer(NumberUtil.toInteger((String) source));
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(Long[].class)) {
                Long[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new Long[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = new Long(NumberUtil.toLong(((String[]) source)[i]));
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new Long[1];
                        returnValue[0] = new Long(NumberUtil.toLong((String) source));
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(Float[].class)) {
                Float[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new Float[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = new Float(NumberUtil.toFloat(((String[]) source)[i]));
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new Float[1];
                        returnValue[0] = new Float(NumberUtil.toFloat((String) source));
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(Double[].class)) {
                Double[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new Double[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = new Double(NumberUtil.toDouble(((String[]) source)[i]));
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new Double[1];
                        returnValue[0] = new Double(NumberUtil.toDouble((String) source));
                    }
                }

                return returnValue;
            }
            if (classToConvert.equals(Timestamp[].class)) {
                Timestamp[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new Timestamp[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = (Timestamp) convert(Timestamp.class, ((String[]) source)[i]);
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new Timestamp[1];
                        returnValue[0] = (Timestamp) convert(Timestamp.class, source);
                    }
                }
                return returnValue;
            }
            if (classToConvert.equals(Date[].class)) {
                Date[] returnValue = null;
                if (source.getClass().equals(String[].class)) {
                    int len = ((String[]) source).length;
                    returnValue = new Date[len];
                    for (int i = 0; i < len; i++) {
                        returnValue[i] = (Date) convert(Date.class, ((String[]) source)[i]);
                    }
                } else {
                    if (source.getClass().equals(String.class)) {
                        returnValue = new Date[1];
                        returnValue[0] = (Date) convert(Date.class, source);
                    }
                }
                return returnValue;
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public static Object getBean(Class aClass, HttpServletRequest request) {
        try {
            return copyObjectByFix(aClass, request, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object copyObjectByFix(Class aClass, HttpServletRequest request, boolean rejectPrefix)
        throws InstantiationException, IllegalAccessException {
        Object destObj = aClass.newInstance();
        Field[] field = aClass.getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String paramName = field[i].getName();
            if (rejectPrefix) {
                paramName = rejectPrefix(paramName);
            }
            Object paramValue = null;
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues == null) continue;
            switch (paramValues.length) {
                case 0:
                    paramValue = null;
                    break;
                case 1:
                    paramValue = paramValues[0];
                    paramValue = toGB(paramValue.toString());
                    break;
                default:
                    paramValue = paramValues;
            }
            try {
                setPropertyValueForBean(destObj, field[i].getName(), paramValue, rejectPrefix);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return destObj;
    }

    /**
     * 通过指定的bean从request中取值并返回一个list
     * 
     * @param aClass
     * @param request
     * @param rejectPrefix
     * @return
     */
    public static List getBeanList(Class aClass, HttpServletRequest request, boolean rejectPrefix) {
        try {
            Map map = new HashMap();
            int sLength = 0;
            Field[] field = aClass.getDeclaredFields();
            for (int i = 0; i < field.length; i++) {
                String fieldName = field[i].getName();
                if (rejectPrefix) {
                    fieldName = rejectPrefix(fieldName);
                }
                if ("clmInvId".equals(fieldName)) {// 不取它
                    continue;
                }
                String values[] = request.getParameterValues(fieldName);// 从页面上取值
                if (values != null) {
                    sLength = values.length;
                    map.put(field[i].getName(), values);
                    // Log.info("bean property:[" + fieldName+ "] array length:[" + values.length + "]");
                }
            }
            return getBeanList(aClass, sLength, map, rejectPrefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List getBeanList(Class aclass, int length, Map map, boolean rejectPrefix)
        throws SQLException, IllegalAccessException, InstantiationException, Exception {
        List newBeans = new ArrayList();
        for (int i = 0; i < length; i++) {// 数组长度
            Object newBean = aclass.newInstance();
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                String values[] = (String[]) map.get(name);
                try {
                    setPropertyValueForBean(newBean, name, values[i], rejectPrefix);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }
            newBeans.add(newBean);
        }
        return newBeans;
    }

    private static boolean setPropertyValueForBean(Object newBean, String propName, Object propValue, boolean rejectPrefix) throws Exception {
        Class[] paramTypes = new Class[1];
        Object[] args = new Object[1];
        boolean addOrNot = false;
        Method writeMethod = getPropertyWriteMethod(newBean.getClass(), propName, rejectPrefix);
        if (writeMethod != null && Modifier.isPublic(writeMethod.getModifiers())) {
            addOrNot = true;
            paramTypes = writeMethod.getParameterTypes();
            args[0] = BeanUtil.convert(paramTypes[0], propValue);
            if (args[0] != null) {
                writeMethod.invoke(newBean, args);
                addOrNot = false;
            }
        }
        return addOrNot;
    }

    private static Method getPropertyWriteMethod(Class beanClass, String propName, boolean rejectPrefix) throws NoSuchMethodException {
        if (rejectPrefix) {
            propName = rejectPrefix(propName);
        }
        Method readMethod = getPropertyReadMethod(beanClass, propName);
        if (readMethod == null) {
            return null;
        }
        Class[] argTypes = {readMethod.getReturnType()};
        propName = propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
        return beanClass.getMethod("set" + propName, argTypes);
    }

    private static Method getPropertyReadMethod(Class beanClass, String propName) throws NoSuchMethodException {
        Method method = null;
        propName = propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
        try {
            method = beanClass.getMethod("get" + propName, null);
        } catch (NoSuchMethodException nsex) {
            method = beanClass.getMethod("is" + propName, null);
        }
        return method;
    }

    private static String rejectPrefix(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        String result = "";
        // 去除以上字符并且把剩下的以小写字母打头
        if (str.startsWith("mstr")) {
            result = str.substring("mstr".length());
        } else if (str.startsWith("mdec")) {
            result = str.substring("mdec".length());
        } else if (str.startsWith("mlng")) {
            result = str.substring("mlng".length());
        } else if (str.startsWith("mdate")) {
            result = str.substring("mdate".length());
        } else if (str.startsWith("mblob")) {
            result = str.substring("mblob".length());
        } else {
            result = str;
        }
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }

    private static String toGB(String inStr) {
        try {

            if (inStr == null) return "";
            return inStr;
        } catch (Exception e) {
            return "";
        }
    }

    public static Object getBean(Class beanClass, String prefix, String postfix, HttpServletRequest request) {
        try {
            return getBean(beanClass, prefix, postfix, request, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getBean(Class beanClass, String prefix, String postfix, HttpServletRequest request, boolean toGBK)
        throws IllegalAccessException, InstantiationException {
        return copyObjectByFix(beanClass, prefix, postfix, request, toGBK);
    }

    private static Object copyObjectByFix(Class aClass, String prefix, String postfix, HttpServletRequest request, boolean toGBK)
        throws InstantiationException, IllegalAccessException {
        Object destObj = aClass.newInstance();
        Field[] field = aClass.getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String paramName = prefix + "." + field[i].getName() + postfix;
            Object paramValue = null;
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues == null) continue;
            switch (paramValues.length) {
                case 0:
                    paramValue = null;
                    break;
                case 1:
                    paramValue = paramValues[0];
                    if (toGBK) {
                        paramValue = toGB(paramValue.toString());
                    }
                    break;
                default:
                    paramValue = paramValues;
            }
            try {
                setPropertyValueForBean(destObj, field[i].getName(), paramValue, true);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return destObj;
    }

    public static List getBeanList(Class beanClass, String prefix, String postfix, HttpServletRequest request) {
        try {
            return getBeanList(beanClass, prefix, postfix, request, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List getBeanList(Class beanClass, String prefix, String postfix, HttpServletRequest request, boolean toGBK)
        throws IllegalAccessException, InstantiationException {
        int size = getBeanListSizeByFix(beanClass, prefix, postfix, request);
        List beanList = new ArrayList();
        for (int i = 0; i < size; i++) {
            beanList.add(copyObject0ByFix(beanClass, prefix, postfix, request, toGBK, i));
        }
        return beanList;
    }

    private static int getBeanListSizeByFix(Class aClass, String prefix, String postfix, HttpServletRequest request)
        throws InstantiationException, IllegalAccessException {
        Field[] field = aClass.getDeclaredFields();
        int size = 0;
        for (int i = 0; i < field.length; i++) {
            String paramName = prefix + "." + field[i].getName() + postfix;
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues != null) {
                size = paramValues.length;
                break;
            }
        }
        return size;
    }

    private static Object copyObject0ByFix(Class aClass, String prefix, String postfix, HttpServletRequest request, boolean toGBK, int index)
        throws InstantiationException, IllegalAccessException {
        Object destObj = aClass.newInstance();
        Field[] field = aClass.getDeclaredFields();
        for (int i = 0; i < field.length; i++) {
            String paramName = prefix + "." + field[i].getName() + postfix;
            Object paramValue = null;
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues == null) {
                continue;
            }
            if (paramValues.length > index) {
                paramValue = paramValues[index];
            }
            try {
                setPropertyValueForBean(destObj, field[i].getName(), paramValue, true);
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        return destObj;
    }
}
