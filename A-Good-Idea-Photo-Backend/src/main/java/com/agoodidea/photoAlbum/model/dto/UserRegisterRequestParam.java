package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户注册参数
 */
@Data
public class UserRegisterRequestParam implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String username;
    @NotBlank(message = "真实姓名不能为空")
    private String realname;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String password2;
    @Range(min = 0, max = 2, message = "性别格式错误")
    private Integer gender;
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "邮箱验证码不能为空")
    private String captcha;
}
