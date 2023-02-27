package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class PhotoCardRequestParam implements Serializable {
    private Integer current;
    private Integer size;
    private Long albumId;
    private String createBy;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
