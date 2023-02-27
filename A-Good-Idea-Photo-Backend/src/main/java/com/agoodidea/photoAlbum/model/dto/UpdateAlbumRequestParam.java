package com.agoodidea.photoAlbum.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateAlbumRequestParam implements Serializable {
    @NotNull(message = "请选择修改的相册")
    private Long id;
    @NotBlank(message = "图库名称不能为空")
    private String albumName;
    @Range(min = 1, max = 99, message = "图库最大共享人数为99")
    private Integer maxUser;
    @NotBlank(message = "描述信息不能为空")
    private String remarks;
}
