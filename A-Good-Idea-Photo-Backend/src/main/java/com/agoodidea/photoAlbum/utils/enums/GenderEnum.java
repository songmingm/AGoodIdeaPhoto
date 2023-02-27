package com.agoodidea.photoAlbum.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenderEnum implements KeyValueEnum {
    UN_KNOW(-1, "未知"),
    MALE(1, "偶爷们要战斗"),
    FEMALE(0, "娇滴滴的妹妹"),
    TAIGUO(2, "偶从泰国回来");

    private final Integer key;

    private final String value;

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
