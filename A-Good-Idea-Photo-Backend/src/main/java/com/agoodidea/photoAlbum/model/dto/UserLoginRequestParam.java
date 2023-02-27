package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户名登录参数
 */
@Data
public class UserLoginRequestParam implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
