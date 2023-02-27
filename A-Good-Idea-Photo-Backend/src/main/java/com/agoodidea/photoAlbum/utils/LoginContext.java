package com.agoodidea.photoAlbum.utils;

import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.mapper.AlbumMapper;
import com.agoodidea.photoAlbum.model.entity.Album;
import com.agoodidea.photoAlbum.model.entity.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;


public class LoginContext {

    private static final Logger logger = LoggerFactory.getLogger(LoginContext.class);

    /**
     * 获取登录用户信息
     * SecurityContextHolder 未登录时，信息为空
     */
    public static LoginUser getLoginData() {
        LoginUser loginUser;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            loginUser = (LoginUser) authentication.getPrincipal();
            logger.info("---------------------");
            logger.info("当前登录用户：{}", loginUser.getUser().getUsername());
            logger.info("---------------------");
        } catch (Exception e) {
            throw new BusinessException(ConstantsMsg.User.NOT_LOGIN);
        }
        return loginUser;
    }

    /**
     * 加载用户权限
     */
    public static List<String> getUserPermissions(Long userID, AlbumMapper albumMapper) {
        LambdaQueryWrapper<Album> w1 = new LambdaQueryWrapper<>();
        w1.select(Album::getBucketName).eq(Album::getCreateBy, userID);
        List<Album> albums = albumMapper.selectList(w1);
        return albums.stream()
                .map(Album::getBucketName)
                .distinct()
                .collect(Collectors.toList());
    }
}
