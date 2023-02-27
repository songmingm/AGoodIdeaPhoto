package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CreateAlbumRequestParam implements Serializable {
    @NotBlank(message = "图库名称不能为空")
    private String albumName;
    @Range(min = 1, max = 99, message = "图库最大共享人数为99")
    private Integer maxUser;
    @Range(min = 1, max = 3, message = "图库属性格式不正确")
    private Integer albumType;
    @NotBlank(message = "描述信息不能为空")
    private String remarks;
    private MultipartFile coverImage;
}
