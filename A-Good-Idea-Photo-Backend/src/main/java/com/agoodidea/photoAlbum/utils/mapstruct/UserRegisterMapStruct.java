package com.agoodidea.photoAlbum.utils.mapstruct;

import com.agoodidea.photoAlbum.model.dto.UserRegisterRequestParam;
import com.agoodidea.photoAlbum.model.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * 注册用户对象转换
 */
@Mapper(componentModel = "spring")
@Component
public interface UserRegisterMapStruct extends BaseMapStruct<UserRegisterRequestParam, User> {
    @Override
    User source2target(UserRegisterRequestParam source);

    @Override
    List<User> source2target(List<UserRegisterRequestParam> source);

    @Override
    List<User> source2target(Stream<UserRegisterRequestParam> source);
}
