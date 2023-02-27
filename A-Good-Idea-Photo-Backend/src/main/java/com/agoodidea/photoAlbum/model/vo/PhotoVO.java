package com.agoodidea.photoAlbum.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * 照片详细信息
 */
@Data
public class PhotoVO implements Serializable {

    private String id;
    private String thumbName;
    private String url;
    private String createBy;
    private String bucketName;
    private String createByAvatarUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private HashMap<String, String> meta;
}
