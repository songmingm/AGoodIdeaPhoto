package com.agoodidea.photoAlbum.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 */
@Data
public class ResponseJsonData<T> implements Serializable {
    private Integer status;
    private String msg;
    private T data;

    public static <T> ResponseJsonData<T> successForData(T data) {
        ResponseJsonData<T> rs = new ResponseJsonData<>();
        rs.setStatus(ResponseCode.SUCCESS.getCode());
        rs.setMsg(ResponseCode.SUCCESS.getMsg());
        rs.data = data;
        return rs;
    }

    public static <T> ResponseJsonData<T> successNoData() {
        ResponseJsonData<T> rs = new ResponseJsonData<>();
        rs.setStatus(ResponseCode.SUCCESS.getCode());
        rs.setMsg(ResponseCode.SUCCESS.getMsg());
        rs.setData(null);
        return rs;
    }

    public static <T> ResponseJsonData<T> successNoData(String message) {
        ResponseJsonData<T> rs = new ResponseJsonData<>();
        rs.setStatus(ResponseCode.SUCCESS.getCode());
        rs.setMsg(message);
        rs.setData(null);
        return rs;
    }

    public static <T> ResponseJsonData<T> failed(String message) {
        ResponseJsonData<T> rs = new ResponseJsonData<>();
        rs.setStatus(ResponseCode.ERROR.getCode());
        rs.setMsg(message);
        return rs;
    }

    public static <T> ResponseJsonData<T> failed(ResponseCode responseCode) {
        ResponseJsonData<T> rs = new ResponseJsonData<>();
        rs.setStatus(responseCode.getCode());
        rs.setMsg(responseCode.getMsg());
        return rs;
    }

}
