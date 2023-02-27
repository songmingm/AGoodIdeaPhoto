package com.agoodidea.photoAlbum.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileTypeEnum implements KeyValueEnum {

    UN_KNOW(-1, "未知"),
    DIR(0, "文件夹"),
    JPG(1, "JPG图片"),
    PNG(2, "PNG图片"),
    PDF(3, "PDF文件");

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
