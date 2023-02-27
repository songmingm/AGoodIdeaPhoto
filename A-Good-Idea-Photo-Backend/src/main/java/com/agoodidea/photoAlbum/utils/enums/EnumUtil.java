package com.agoodidea.photoAlbum.utils.enums;

import org.springframework.util.ObjectUtils;

public class EnumUtil {

    /**
     * 根据枚举key获取枚举类型
     * 规定所有枚举 —1 对应 未知
     */
    public static String getValueByKey(Integer key,
                                       Class<? extends KeyValueEnum> type) {
        if (ObjectUtils.isEmpty(type) || ObjectUtils.isEmpty(key)) {
            return "未知";
        }
        KeyValueEnum[] enumConstants = type.getEnumConstants();
        if (ObjectUtils.isEmpty(enumConstants)) {
            return "未知";
        }
        for (KeyValueEnum constant : enumConstants) {
            if (key.equals(constant.getKey())) {
                return constant.getValue();
            }
        }
        return "未知";
    }

    /**
     * 根据枚举value获取枚举类型
     */
    public static Integer getKeyByValue(String value,
                                        Class<? extends KeyValueEnum> type) {
        if (ObjectUtils.isEmpty(type) || ObjectUtils.isEmpty(value)) {
            return -1;
        }
        KeyValueEnum[] enumConstants = type.getEnumConstants();
        if (ObjectUtils.isEmpty(enumConstants)) {
            return -1;
        }
        for (KeyValueEnum constant : enumConstants) {
            if (value.equals(constant.getValue())) {
                return constant.getKey();
            }
        }
        return -1;
    }

}
