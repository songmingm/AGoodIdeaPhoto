package com.agoodidea.photoAlbum.service.impl;

import cn.hutool.http.HttpUtil;
import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.mapper.AlbumMapper;
import com.agoodidea.photoAlbum.mapper.FileMapper;
import com.agoodidea.photoAlbum.mapper.PhotoMapper;
import com.agoodidea.photoAlbum.model.dto.PhotoCardRequestParam;
import com.agoodidea.photoAlbum.model.entity.Album;
import com.agoodidea.photoAlbum.model.entity.Photo;
import com.agoodidea.photoAlbum.model.entity.PhotoMetaData;
import com.agoodidea.photoAlbum.model.vo.PhotoMetaDataDetailVO;
import com.agoodidea.photoAlbum.model.vo.PhotoVO;
import com.agoodidea.photoAlbum.service.PhotoService;
import com.agoodidea.photoAlbum.utils.FileCommonUtl;
import com.agoodidea.photoAlbum.utils.GPSUtil;
import com.agoodidea.photoAlbum.utils.LoginContext;
import com.agoodidea.photoAlbum.utils.mapstruct.PhotoDetailMapstruct;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final Gson gson = new Gson();
    private final FileMapper fileMapper;
    private final AlbumMapper albumMapper;
    private final PhotoMapper photoMapper;
    @Value("${minio.avatar-bucket}")
    private String avatarBucket;
    private final PhotoDetailMapstruct photoDetailMapstruct;
    @Value("${baidu.serverAk}")
    private String ak;
    @Value("${baidu.serverUrl}")
    private String GPSServerUrl;

    public PhotoServiceImpl(FileMapper fileMapper, AlbumMapper albumMapper, PhotoMapper photoMapper, PhotoDetailMapstruct photoDetailMapstruct) {
        this.fileMapper = fileMapper;
        this.albumMapper = albumMapper;
        this.photoMapper = photoMapper;
        this.photoDetailMapstruct = photoDetailMapstruct;
    }

    /**
     * 删除相册
     */
    @Override
    @Transactional
    public void removePhoto(Long albumID, Long photoID) {
        Album album = albumMapper.selectById(albumID);
        if (ObjectUtils.isEmpty(album)) {
            throw new BusinessException("相册不存在");
        }
        Photo photo = photoMapper.selectById(photoID);
        if (ObjectUtils.isEmpty(photo)) {
            throw new BusinessException("照片不存在");
        }
        List<String> buckets = LoginContext.getLoginData().getBuckets();
        String bucketName = album.getBucketName();
        boolean flag = buckets.contains(bucketName);
        if (!flag) {
            throw new BusinessException("您没有操作的权限");
        }
        // 删除缩略图和原图
        photoMapper.deleteById(photo);
        fileMapper.deleteObject(bucketName, photo.getThumbName());
        fileMapper.deleteObject(bucketName, photo.getObjectName());
    }

    /**
     * 上传时会保存原文件和缩略图文件，预览缩略图文件
     */
    @Transactional
    @Override
    public void photosUpload(Long id, MultipartFile[] images) {
        Album album = albumMapper.selectById(id);
        if (ObjectUtils.isEmpty(album)) {
            throw new BusinessException("相册不存在");
        }
        Long userId = LoginContext.getLoginData().getUser().getId();
        String bucketName = album.getBucketName();
        for (MultipartFile image : images) {
            // 上传原图
            PhotoMetaData imageMetaData = FileCommonUtl.getImageMetaData(image);
            HashMap metaMap = gson.fromJson(gson.toJson(imageMetaData), HashMap.class);
            String objectName = fileMapper.putObjectInBucket(bucketName, image, metaMap);
            // 上传缩略图
            byte[] bytes = FileCommonUtl.compressImageToBytes(image, 300, 150);
            InputStream inputSteam = FileCommonUtl.getImageInputSteam(bytes);
            String thumbName = "thumb-" + objectName;
            fileMapper.putObjectInBucket(bucketName, inputSteam, bytes.length, thumbName);
            Photo photo = new Photo();
            photo.setAlbumId(id);
            photo.setObjectName(objectName);
            photo.setThumbName(thumbName);
            photo.setCreateBy(userId);
            photo.setCreateTime(new Date());
            photoMapper.insert(photo);
        }
    }

    @Override
    public IPage<PhotoVO> getPhotosList(PhotoCardRequestParam param) {
        if (ObjectUtils.isEmpty(param.getCurrent())) {
            param.setCurrent(1);
        }
        if (ObjectUtils.isEmpty(param.getSize())) {
            param.setSize(6);
        }
        // TODO 判断是否用户有这个相册的访问权限
        Page<PhotoVO> page = new Page<>(param.getCurrent(), param.getSize());
        IPage<PhotoVO> pageData = photoMapper.selectPhotosByAlbumId(page, param);
        List<PhotoVO> photos = pageData.getRecords();
        for (PhotoVO photo : photos) {
            String thumbName = photo.getThumbName();
            String previewUrl = fileMapper.preview(thumbName, photo.getBucketName());
            String avatarUrl = fileMapper.preview(photo.getCreateByAvatarUrl(), avatarBucket);
            photo.setCreateByAvatarUrl(avatarUrl);
            photo.setUrl(previewUrl);
        }
        return pageData;
    }

    @Override
    public PhotoMetaDataDetailVO getPhotoDetail(Long id) {
        Photo photo = photoMapper.selectById(id);
        if (ObjectUtils.isEmpty(photo)) throw new BusinessException("照片不存在");
        String objectName = photo.getObjectName();
        LambdaQueryWrapper<Album> w1 = Wrappers.lambdaQuery(Album.class);
        w1.select(Album::getBucketName).eq(Album::getId, photo.getAlbumId());
        Album album = albumMapper.selectOne(w1);
        String bucketName = album.getBucketName();
        Map<String, String> metaData = fileMapper.getMetaData(bucketName, objectName);
        PhotoMetaData photoMetaData = gson.fromJson(gson.toJson(metaData), PhotoMetaData.class);
        PhotoMetaDataDetailVO vo = photoDetailMapstruct.source2target(photoMetaData);
        // 纬度和经度
        Double lat = GPSUtil.dms2d(photoMetaData.getLatitude());
        Double lng = GPSUtil.dms2d(photoMetaData.getLongitude());
        HashMap<String, Object> param = new HashMap<>();
        param.put("ak", ak);
        param.put("location", lat + "," + lng);
        param.put("output", "json");
        JsonElement responseJson = JsonParser.parseString(HttpUtil.get(GPSServerUrl, param, 3000));
        // 百度api 返回状态码
        int status = responseJson.getAsJsonObject().get("status").getAsInt();
        if (status == 0) {
            // 提取精确位置信息
            JsonObject result = responseJson.getAsJsonObject().get("result").getAsJsonObject();
            String location = result.get("formatted_address").getAsString();
            String country = result.get("addressComponent").getAsJsonObject()
                    .get("country").getAsString();
            vo.setCountry(country);
            vo.setLocations(location);
        }
        return vo;
    }
}
