package com.agoodidea.photoAlbum.service;

import com.agoodidea.photoAlbum.model.dto.PhotoCardRequestParam;
import com.agoodidea.photoAlbum.model.vo.PhotoMetaDataDetailVO;
import com.agoodidea.photoAlbum.model.vo.PhotoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {

    void removePhoto(Long albumID, Long photoID);

    void photosUpload(Long id, MultipartFile[] images);

    IPage<PhotoVO> getPhotosList(PhotoCardRequestParam param);

    PhotoMetaDataDetailVO getPhotoDetail(Long id);
}
