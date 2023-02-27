package com.agoodidea.photoAlbum.service;

import com.agoodidea.photoAlbum.model.dto.UserLoginByEmailRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserLoginRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserRegisterRequestParam;
import com.agoodidea.photoAlbum.model.dto.UserUpdateRequestParam;
import com.agoodidea.photoAlbum.model.entity.User;
import com.agoodidea.photoAlbum.model.vo.LoginUserDataVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends IService<User> {

    Integer register(UserRegisterRequestParam param);

    String loginByUsername(UserLoginRequestParam param);

    String loginByEmail(UserLoginByEmailRequestParam param);

    LoginUserDataVO getLoginUserData();

    void logout();

    Boolean updateMyData(UserUpdateRequestParam param);

    Boolean uploadAvatar(MultipartFile file);

    List<String> getUserPermissions();
}
