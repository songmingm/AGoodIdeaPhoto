package com.agoodidea.photoAlbum.service.impl;

import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.mapper.AlbumMapper;
import com.agoodidea.photoAlbum.mapper.FileMapper;
import com.agoodidea.photoAlbum.model.dto.CreateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.dto.UpdateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.entity.Album;
import com.agoodidea.photoAlbum.model.vo.AlbumVO;
import com.agoodidea.photoAlbum.service.AlbumService;
import com.agoodidea.photoAlbum.utils.FileCommonUtl;
import com.agoodidea.photoAlbum.utils.LoginContext;
import com.agoodidea.photoAlbum.utils.PinyinUtil;
import com.agoodidea.photoAlbum.utils.enums.AlbumTypeEnum;
import com.agoodidea.photoAlbum.utils.mapstruct.AlbumCreateMapstruct;
import com.agoodidea.photoAlbum.utils.mapstruct.AlbumUpdateMapstruct;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    private final AlbumMapper albumMapper;
    private final AlbumCreateMapstruct albumCreateMapstruct;
    private final AlbumUpdateMapstruct albumUpdateMapstruct;
    private final FileMapper fileMapper;
    @Value("${minio.avatar-bucket}")
    private String avatarBucket;

    public AlbumServiceImpl(AlbumMapper albumMapper,
                            AlbumCreateMapstruct albumCreateMapstruct,
                            AlbumUpdateMapstruct albumUpdateMapstruct,
                            FileMapper fileMapper) {
        this.albumMapper = albumMapper;
        this.albumCreateMapstruct = albumCreateMapstruct;
        this.albumUpdateMapstruct = albumUpdateMapstruct;
        this.fileMapper = fileMapper;
    }

    @Override
    @Transactional
    public Boolean createAlbum(CreateAlbumRequestParam param) {
        // 判断图库是否重名
        LambdaQueryWrapper<Album> w1 = new LambdaQueryWrapper<>();
        w1.eq(Album::getAlbumName, param.getAlbumName());
        Long isHas = albumMapper.selectCount(w1);
        if (isHas > 0) {
            throw new BusinessException("图库名称已经存在");
        }
        Long myId = LoginContext.getLoginData().getUser().getId();
        Album album = albumCreateMapstruct.source2target(param);
        // 生成桶名
        String bucketName = "agood-" + PinyinUtil.getFirstSpell(album.getAlbumName()) + "-" + System.currentTimeMillis();
        album.setBucketName(bucketName);
        album.setCreateBy(myId);
        album.setCreateTime(new Date());
        fileMapper.createBucket(bucketName);
        byte[] imageToBytes = FileCommonUtl.compressImageToBytes(param.getCoverImage(), 300, 200);
        InputStream inputSteam = FileCommonUtl.getImageInputSteam(imageToBytes);
        String filename = "thumb-" + FileCommonUtl.renameFile(param.getCoverImage().getOriginalFilename());
        fileMapper.putObjectInBucket(bucketName, inputSteam, imageToBytes.length, filename);
        album.setCoverUrl(filename);
        int flag = albumMapper.insert(album);
        return flag > 0;
    }

    @Override
    public List<AlbumVO> getPublicAlbums() {
        List<AlbumVO> albums = albumMapper.getAlbumByType(AlbumTypeEnum.PUBLIC.getKey(), null);
        albums.forEach(albumVO -> {
            albumVO.setCoverUrl(fileMapper.preview(albumVO.getCoverUrl(), albumVO.getBucketName()));
            albumVO.setCreateByAvatarUrl(fileMapper.preview(albumVO.getCreateByAvatarUrl(), avatarBucket));
        });
        return albums;
    }

    @Override
    public List<AlbumVO> getPrivateAlbums() {
        Long userId = LoginContext.getLoginData().getUser().getId();
        List<AlbumVO> albums = albumMapper.getAlbumByType(AlbumTypeEnum.PRIVATE.getKey(), userId);
        albums.forEach(albumVO -> {
            albumVO.setCoverUrl(fileMapper.preview(albumVO.getCoverUrl(), albumVO.getBucketName()));
            albumVO.setCreateByAvatarUrl(fileMapper.preview(albumVO.getCreateByAvatarUrl(), avatarBucket));
        });
        return albums;
    }

    @Override
    @Transactional
    public Boolean deleteAlbumById(Long id) {
        Album album = albumMapper.selectById(id);
        if (ObjectUtils.isEmpty(album)) {
            throw new BusinessException("相册不存在");
        }
        Long myID = LoginContext.getLoginData().getUser().getId();
        if (!album.getCreateBy().equals(myID)) {
            throw new BusinessException("您没有删除这个相册的权限，仅相册的创建者可以删除");
        }
        Boolean flag = albumMapper.deleteById(id) > 0;
        // 先删除封面，防止因为封面图片存在，bucket不为空时删除失败
        String coverUrl = album.getCoverUrl();
        fileMapper.deleteObject(album.getBucketName(), coverUrl);
        fileMapper.deleteBucket(album.getBucketName());
        // TODO 消息队列异步，邮件通知相册内的用户已删除相册
        return flag;
    }

    @Override
    @Transactional
    public Boolean updateAlbum(UpdateAlbumRequestParam param) {
        Long albumId = param.getId();
        Album album = albumMapper.selectById(albumId);
        String bucketName = album.getBucketName();
        boolean contains = LoginContext.getLoginData().getBuckets().contains(bucketName);
        if (!contains) {
            throw new BusinessException("您没有权限修改这个相册信息");
        }
        Album target = albumUpdateMapstruct.source2target(param);
        return albumMapper.updateById(target) > 0;
    }

    @Override
    public AlbumVO getAlbumByID(Long albumId) {
        Album album = albumMapper.selectById(albumId);
        if (ObjectUtils.isEmpty(album)) {
            throw new BusinessException("相册不存在");
        }
        String bucketName = album.getBucketName();
        // 判断是否是公共图库，不是需要鉴别是否有查看权限
        if (!album.getAlbumType().equals(AlbumTypeEnum.PUBLIC.getKey())) {
            boolean contains = LoginContext.getLoginData().getBuckets().contains(bucketName);
            if (!contains) {
                throw new BusinessException("您没有权限查看这个相册的详细信息");
            }
        }
        AlbumVO albumVO = albumMapper.getAlbumById(albumId);
        albumVO.setCoverUrl(fileMapper.preview(albumVO.getCoverUrl(), albumVO.getBucketName()));
        albumVO.setCreateByAvatarUrl(fileMapper.preview(albumVO.getCreateByAvatarUrl(), avatarBucket));
        return albumVO;
    }

    @Override
    @Transactional
    public void setCoverImageByID(MultipartFile image, Long albumId) {
        boolean isImage = FileCommonUtl.isImage(image.getOriginalFilename());
        if (!isImage) {
            throw new BusinessException("上传的相册封面不是图片格式，请重新选择");
        }
        Album album = albumMapper.selectById(albumId);
        if (ObjectUtils.isEmpty(album)) {
            throw new BusinessException("相册不存在");
        }
        String bucketName = album.getBucketName();
        boolean contains = LoginContext.getLoginData().getBuckets().contains(bucketName);
        if (!contains) {
            throw new BusinessException("您没有权限修改这个相册信息");
        }
        String coverUrl = album.getCoverUrl();
        fileMapper.deleteObject(bucketName, coverUrl);
        byte[] imageToBytes = FileCommonUtl.compressImageToBytes(image, 300, 200);
        InputStream inputSteam = FileCommonUtl.getImageInputSteam(imageToBytes);
        String filename = "thumb-" + FileCommonUtl.renameFile(image.getOriginalFilename());
        fileMapper.putObjectInBucket(bucketName, inputSteam, imageToBytes.length, filename);
        album.setCoverUrl(filename);
        albumMapper.updateById(album);
    }
}
