package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserUpdateRequestParam implements Serializable {
    @NotBlank(message = "昵称不能为空")
    private String username;
    @NotBlank(message = "真实姓名不能为空")
    private String realname;
    @Range(min = 0, max = 2, message = "性别格式错误")
    private Integer gender;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
