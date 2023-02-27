package com.agoodidea.photoAlbum.controller;

import com.agoodidea.photoAlbum.model.dto.CreateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.dto.PhotoCardRequestParam;
import com.agoodidea.photoAlbum.model.dto.UpdateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.vo.AlbumVO;
import com.agoodidea.photoAlbum.model.vo.PhotoMetaDataDetailVO;
import com.agoodidea.photoAlbum.model.vo.PhotoVO;
import com.agoodidea.photoAlbum.service.AlbumService;
import com.agoodidea.photoAlbum.service.PhotoService;
import com.agoodidea.photoAlbum.utils.ConstantsMsg;
import com.agoodidea.photoAlbum.utils.ResponseJsonData;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    public AlbumController(AlbumService albumService,
                           PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    @PostMapping("create")
    private ResponseJsonData<String> createAlbum(@Validated CreateAlbumRequestParam param) {
        Boolean flag = albumService.createAlbum(param);
        return flag ? ResponseJsonData.successNoData(ConstantsMsg.Common.INSERT_SUCCESS)
                : ResponseJsonData.failed(ConstantsMsg.Common.INSERT_ERROR);
    }

    @GetMapping("public")
    private ResponseJsonData<List<AlbumVO>> getAllPublicAlbums() {
        List<AlbumVO> publicAlbums = albumService.getPublicAlbums();
        return ResponseJsonData.successForData(publicAlbums);
    }

    @GetMapping("private")
    private ResponseJsonData<List<AlbumVO>> getAllPrivateAlbums() {
        return ResponseJsonData.successForData(albumService.getPrivateAlbums());
    }

    @DeleteMapping("del/{id}")
    private ResponseJsonData<String> deleteAlbum(@PathVariable("id") Long id) {
        Boolean flag = albumService.deleteAlbumById(id);
        return flag ? ResponseJsonData.successNoData(ConstantsMsg.Common.DELETE_SUCCESS) :
                ResponseJsonData.failed(ConstantsMsg.Common.DELETE_ERROR);
    }

    @DeleteMapping("del/photo")
    private ResponseJsonData<String> deletePhotos(Long albumID, Long photoID) {
        photoService.removePhoto(albumID, photoID);
        return ResponseJsonData.successNoData();
    }

    @PostMapping("photos/upload/{id}")
    private ResponseJsonData<String> photosUpload(@PathVariable("id") Long id, MultipartFile[] images) {
        photoService.photosUpload(id, images);
        return ResponseJsonData.successNoData();
    }

    @PostMapping("photos/card")
    private ResponseJsonData<IPage<PhotoVO>> getPhotosCards(@RequestBody PhotoCardRequestParam param) {
        IPage<PhotoVO> pageData = photoService.getPhotosList(param);
        return ResponseJsonData.successForData(pageData);
    }

    @GetMapping("detail/{albumId}")
    private ResponseJsonData<AlbumVO> getAlbumDetail(@PathVariable Long albumId) {
        return ResponseJsonData.successForData(albumService.getAlbumByID(albumId));
    }

    @PutMapping("update")
    private ResponseJsonData<String> updateAlbumDetail(@RequestBody @Validated UpdateAlbumRequestParam param) {
        Boolean flag = albumService.updateAlbum(param);
        return flag ? ResponseJsonData.successNoData(ConstantsMsg.Common.UPDATE_SUCCESS) :
                ResponseJsonData.failed(ConstantsMsg.Common.UPDATE_ERROR);
    }

    @PostMapping("setCover")
    private ResponseJsonData<String> updateAlbumCoverImage(MultipartFile cover, Long albumId) {
        albumService.setCoverImageByID(cover, albumId);
        return ResponseJsonData.successNoData(ConstantsMsg.Common.UPDATE_SUCCESS);
    }

    @GetMapping("photo/detail/{photoId}")
    private ResponseJsonData<PhotoMetaDataDetailVO> getOnePhotoDetail(@PathVariable("photoId") Long photoId) {
        PhotoMetaDataDetailVO photoDetail = photoService.getPhotoDetail(photoId);
        return ResponseJsonData.successForData(photoDetail);
    }
}
