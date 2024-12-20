package com.xc.blogbackend.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段复制工具类
 */
public class FieldCopyUtils {

    /**
     * 复制源对象的属性到新创建的目标对象，仅复制相同类型和相同字段名的值
     *
     * @param source 源对象
     * @param targetClass 目标对象的类
     * @param <T> 目标对象的类型
     * @return 新创建的目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null || targetClass == null) {
            throw new RuntimeException("Source and target class cannot be null");
        }
        try {
            // 创建目标对象实例
            T target = targetClass.getDeclaredConstructor().newInstance();
            // 获取源对象的类
            Class<?> sourceClass = source.getClass();

            // 获取源对象的所有字段
            Field[] sourceFields = sourceClass.getDeclaredFields();
            for (Field sourceField : sourceFields) {
                // 跳过 serialVersionUID 字段
                if ("serialVersionUID".equals(sourceField.getName())) {
                    continue;
                }
                // 设置源字段为可访问，允许访问私有字段
                sourceField.setAccessible(true);
                // 试图在目标对象中找到与源字段同名的字段
                try {
                    Field targetField = targetClass.getDeclaredField(sourceField.getName());
                    // 设置目标字段为可访问，允许访问私有字段
                    targetField.setAccessible(true);

                    // 仅当源字段类型与目标字段类型相同时，才进行复制
                    if (sourceField.getType().equals(targetField.getType())) {
                        // 获取源字段的值
                        Object value = sourceField.get(source);
                        // 将值设置到目标字段
                        targetField.set(target, value);
                    }
                } catch (NoSuchFieldException e) {
                    // 如果目标对象没有同名字段，忽略该字段
                } catch (IllegalAccessException e) {
                    // 如果无法访问目标字段，抛出运行时异常
                    throw new RuntimeException("Failed to access field: " + sourceField.getName(), e);
                }
            }
            return target; // 返回新创建的目标对象
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy properties to target class: " + targetClass.getName(), e);
        }
    }

    /**
     * 复制源对象列表的属性到目标对象列表，仅复制相同类型和相同字段名的值
     *
     * @param sourceList 源对象列表
     * @param targetClass 目标对象的类
     * @param <T> 目标对象的类型
     * @return 目标对象列表
     */
    public static <T> List<T> copyProperties(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || targetClass == null) {
            throw new RuntimeException("Source list and target class cannot be null");
        }

        List<T> targetList = new ArrayList<>();
        for (Object source : sourceList) {
            try {
                T target = copyProperties(source, targetClass);// 复制属性
                targetList.add(target); // 添加到目标列表
            } catch (Exception e) {
                throw new RuntimeException("Failed to copy properties to target class: " + targetClass.getName(), e);
            }
        }
        return targetList; // 返回目标对象列表
    }
}
