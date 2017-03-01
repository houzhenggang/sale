package com.hshc.sale.util.validation;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * 数据验证Service
 */
public class ValidateService {

    private static DV dv;

    public ValidateService() {
        super();
    }

    // 解析的入口
    public static void valid(Object object) throws Exception {
        // 获取object的类型
        Class<? extends Object> clazz = object.getClass();
        // 获取该类型声明的成员
        Field[] fields = clazz.getDeclaredFields();
        // 遍历属性
        for (Field field : fields) {
            // 对于private私有化的成员变量，通过setAccessible来修改器访问权限
            field.setAccessible(true);
            validate(field, object);
            // 重新设置会私有权限
            field.setAccessible(false);
        }
    }

    public static void validate(Field field, Object object) throws Exception {

        String description;
        Object value;

        // 获取对象的成员的注解信息
        dv = field.getAnnotation(DV.class);
        value = field.get(object);

        if (dv == null) return;

        description = dv.description().equals("") ? field.getName() : dv.description();

        /************* 注解解析工作开始 ******************/
        if (!dv.nullable()) {
            if (value == null || StringUtils.isBlank(value.toString())) {
                throw new Exception(description + "不能为空");
            }
        }

        if (value.toString().length() > dv.maxLength() && dv.maxLength() != 0) {
            throw new Exception(description + "长度不能超过" + dv.maxLength());
        }

        if (value.toString().length() < dv.minLength() && dv.minLength() != 0) {
            throw new Exception(description + "长度不能小于" + dv.minLength());
        }

        if (!dv.regexExpression().equals("")) {
            if (value.toString().matches(dv.regexExpression())) {
                throw new Exception(description + "格式不正确");
            }
        }
        /************* 注解解析工作结束 ******************/
    }
}
