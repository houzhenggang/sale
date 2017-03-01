package com.hshc.sale.util;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Function: 属性文件工具类
 */
public class PropertiesUtil {
	private static final String SUPPORT_MIME = "mime";
	private static final String UPLOAD = "uploadFile";

    /**
     * 获取属性文件资源
     * @param propertyName 属性文件名
     * @return
     */
    private static ResourceBundle getProperty(String propertyName) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(propertyName);
        return resourceBundle;
    }

    /**
     * 获取属性文件中属性的值
     * @param propertyName 属性文件名
     * @param key 属性名称
     * @return
     */
    public static String getPropertyValue(String propertyName, String key) {
        try {
            String value = getProperty(propertyName).getString(key);
            return value == null ? null : new String(value.getBytes("ISO8859-1"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  获取系统支持文件上传的mime类型
     * @param key
     * @return
     */
    public static String getSUPPORT_MIME(String key) {
        return getPropertyValue(SUPPORT_MIME, key);
    }

    public static Set<String> getMimeKeySet(){
        ResourceBundle property = getProperty(SUPPORT_MIME);
        return property.keySet();
    }

    /**
     * 获取文件上传属性文件key对应的value
     * @param key 文件上传属性key
     * @return 文件上传属性value
     */
    public static String getUPLOAD(String key) {
        return getPropertyValue(UPLOAD, key);
    }
}
