package com.agoodidea.photoAlbum.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 登录用户的信息
 */
@Data
public class LoginUserDataVO {
    private String username;
    private String realname;
    private String avatarUrl;
    private String email;
    private Integer gender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
