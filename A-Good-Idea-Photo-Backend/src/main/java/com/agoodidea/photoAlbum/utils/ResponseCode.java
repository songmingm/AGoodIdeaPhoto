package com.agoodidea.photoAlbum.utils;

/**
 * 返回状态信息枚举
 */
public enum ResponseCode {

    SUCCESS(2000, "成功"),
    ERROR(5000, "错误"),
    USER_AUTH_ERROR(3004, "请认证通过后在访问资源~"),
    AUTH_PRE_ERROR(3005, "您没有进行此操作的权限");

    private final Integer code;
    private final String msg;

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
