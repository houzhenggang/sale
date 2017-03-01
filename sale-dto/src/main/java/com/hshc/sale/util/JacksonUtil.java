package com.hshc.sale.util;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * 转换为普通JavaBean：readValue(json,Student.class)
     * 转换为List:readValue(json,List.class
     * ).但是如果我们想把json转换为特定类型的List，比如List<Student>，就不能直接进行转换了。
     * 因为readValue(json,List
     * .class)返回的其实是List<Map>类型，你不能指定readValue()的第二个参数是List<
     * Student>.class，所以不能直接转换。
     * 我们可以把readValue()的第二个参数传递为Student[].class.然后使用Arrays
     * .asList();方法把得到的数组转换为特定类型的List。 转换为Map：readValue(json,Map.class)
     * 我们使用泛型，得到的也是泛型
     * 
     * @param content
     *            要转换的JavaBean类型
     * @param valueType
     *            原始json字符串数据
     * @return JavaBean对象
     */
    public static <T> T toObject(String content, Class<T> valueType) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            /* 设置忽略多余字段 */
            // objectMapper.getDeserializationConfig().set(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            // false);
            // ObjectReader r =
            // objectMapper.reader().without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把JavaBean转换为json字符串 普通对象转换：toJson(Student) List转换：toJson(List)
     * Map转换:toJson(Map) 我们发现不管什么类型，都可以直接传入这个方法
     * 
     * @param object
     *            JavaBean对象
     * @return json字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String s =
            "{\"HasData\":false,\"Records\":[],\"ErrorCode\":-61,\"Success\":true,\"ErrMessage\":\"输入车牌号有误\",\"ResultType\":\"实时数据\",\"LastSearchTime\":\"\",\"Other\":\"\"}";
        // Result object = JSON.parseObject(s, Result.class);
        // JacksonUtil.toObject(s, Result.class);
        // U.JSON.toBean(s, "S", Result.class);
        // System.out.println(object);
    }
}
