package com.agoodidea.photoAlbum.service.impl;

import com.agoodidea.photoAlbum.configs.BusinessException;
import com.agoodidea.photoAlbum.mapper.UserMapper;
import com.agoodidea.photoAlbum.model.entity.LoginUser;
import com.agoodidea.photoAlbum.model.entity.User;
import com.agoodidea.photoAlbum.service.LoginByEmailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class LoginByEmailServiceImpl implements LoginByEmailService {
    private final UserMapper userMapper;

    public LoginByEmailServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loginByEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("邮箱没有注册");
        }
        // TODO 获取登录用户所拥有的相册
        return new LoginUser(user,null);
    }
}
