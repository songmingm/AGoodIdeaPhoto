package com.agoodidea.photoAlbum.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlbumTypeEnum implements KeyValueEnum {

    UN_KNOW(-1, "未知"),
    PUBLIC(1, "公共图库"),
    PRIVATE(2, "私有图库"),
    SHARE(3, "共享图库");

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
