package com.agoodidea.photoAlbum.utils.mapstruct;

import com.agoodidea.photoAlbum.model.entity.User;
import com.agoodidea.photoAlbum.model.vo.LoginUserDataVO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
@Component
public interface LoginUserDataMapStruct  extends BaseMapStruct<User, LoginUserDataVO>{
    @Override
    LoginUserDataVO source2target(User source);

    @Override
    List<LoginUserDataVO> source2target(List<User> source);

    @Override
    List<LoginUserDataVO> source2target(Stream<User> source);
}
