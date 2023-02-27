package com.agoodidea.photoAlbum.model.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 照片 EXIF（Exchangeable Image File format）数据
 * ”可交换图像文件“的缩写，当中包含了专门为数码相机的照片而定制的元数据
 * 可以记录数码照片的拍摄参数、缩略图及其他属性信息
 * Create by mmsong on 2022/12/20 18:23
 */
@Data
@Builder
public class PhotoMetaData implements Serializable {
    /**
     * 相片宽度，单位:px
     */
    private String width;
    /**
     * 相片高度，单位:px
     */
    private String height;
    /**
     * 拍摄相机制造商
     */
    private String make;
    /**
     * 拍摄相机型号
     */
    private String model;
    /**
     * 手机系统版本号
     */
    private String software;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 高度
     */
    private String altitude;
    /**
     * 闪光灯
     */
    private String flash;
    /**
     * 拍摄时间
     */
    private String time;
}
