package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 邮箱登录参数
 */
@Data
public class UserLoginByEmailRequestParam implements Serializable {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
