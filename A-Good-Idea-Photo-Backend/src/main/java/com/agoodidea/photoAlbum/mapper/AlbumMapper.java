package com.agoodidea.photoAlbum.mapper;

import com.agoodidea.photoAlbum.model.entity.Album;
import com.agoodidea.photoAlbum.model.vo.AlbumVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {

    List<AlbumVO> getAlbumByType(@Param("type") Integer type, @Param("userId") Long userId);

    AlbumVO getAlbumById(@Param("albumId") Long albumId);
}
