package com.agoodidea.photoAlbum.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "photo")
@Data
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String objectName;
    private String thumbName;
    private String meta;
    private Long albumId;
    private Date createTime;
    private Long createBy;
}
