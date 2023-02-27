package com.agoodidea.photoAlbum.utils.mapstruct;

import com.agoodidea.photoAlbum.model.dto.CreateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.entity.Album;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
@Component
public interface AlbumCreateMapstruct extends BaseMapStruct<CreateAlbumRequestParam, Album> {
    @Override
    Album source2target(CreateAlbumRequestParam source);

    @Override
    List<Album> source2target(List<CreateAlbumRequestParam> source);

    @Override
    List<Album> source2target(Stream<CreateAlbumRequestParam> source);
}
