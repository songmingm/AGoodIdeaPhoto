package com.agoodidea.photoAlbum.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by mmsong on 2022/12/23 20:42
 */
@Data
public class PhotoMetaDataDetailVO implements Serializable {
    private String width;
    private String height;
    private String make;
    private String model;
    private String software;
    private String flash;
    private String time;
    private String country;
    private String locations;
}
