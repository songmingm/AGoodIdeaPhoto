package com.agoodidea.photoAlbum.mapper;

import com.agoodidea.photoAlbum.model.dto.PhotoCardRequestParam;
import com.agoodidea.photoAlbum.model.entity.Photo;
import com.agoodidea.photoAlbum.model.vo.PhotoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {

    IPage<PhotoVO> selectPhotosByAlbumId(IPage<PhotoVO> page, @Param("param") PhotoCardRequestParam param);
}
