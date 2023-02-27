package com.agoodidea.photoAlbum.service;

import com.agoodidea.photoAlbum.model.dto.CreateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.dto.UpdateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.entity.Album;
import com.agoodidea.photoAlbum.model.vo.AlbumVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumService extends IService<Album> {

    Boolean createAlbum(CreateAlbumRequestParam param);

    List<AlbumVO> getPublicAlbums();

    List<AlbumVO> getPrivateAlbums();

    Boolean deleteAlbumById(Long id);

    Boolean updateAlbum(UpdateAlbumRequestParam param);

    AlbumVO getAlbumByID(Long albumId);

    void setCoverImageByID(MultipartFile image,Long albumId);
}
