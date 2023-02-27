package com.agoodidea.photoAlbum.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "album")
public class Album implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String albumName;
    private String bucketName;
    private String coverUrl;
    private Integer userNum;
    private String maxUser;
    private Integer albumType;
    private String remarks;
    private Long createBy;
    private Date createTime;
    private Integer deleteFlag;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
