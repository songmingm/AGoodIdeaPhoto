package com.agoodidea.photoAlbum.service.impl;

import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.mapper.AlbumMapper;
import com.agoodidea.photoAlbum.mapper.UserMapper;
import com.agoodidea.photoAlbum.model.entity.LoginUser;
import com.agoodidea.photoAlbum.model.entity.User;
import com.agoodidea.photoAlbum.utils.LoginContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 自定义登录逻辑处理
 */
@Service
public class LoginByAccountServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final AlbumMapper albumMapper;

    public LoginByAccountServiceImpl(UserMapper userMapper,
                                     AlbumMapper albumMapper) {
        this.userMapper = userMapper;
        this.albumMapper = albumMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> w1 = new LambdaQueryWrapper<>();
        w1.eq(User::getUsername, username);
        User user = userMapper.selectOne(w1);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("昵称不存在");
        }
        // TODO 获取登录用户所拥有的相册
        List<String> userPermissions = LoginContext.getUserPermissions(user.getId(), albumMapper);
        return new LoginUser(user, userPermissions);
    }
}
