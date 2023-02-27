package com.agoodidea.photoAlbum.model.vo;

import com.agoodidea.photoAlbum.utils.enums.AlbumTypeEnum;
import com.agoodidea.photoAlbum.utils.enums.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlbumVO implements Serializable {

    private Long id;
    private String albumName;
    private Integer maxUser;
    private String coverUrl;
    private String bucketName;
    private String albumType;
    private Integer userNum;
    private String remarks;
    private String createBy;
    private String createByAvatarUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getAlbumType() {
        return EnumUtil.getValueByKey(Integer.valueOf(albumType), AlbumTypeEnum.class);
    }
}
