package com.agoodidea.photoAlbum.utils.mapstruct;

import com.agoodidea.photoAlbum.model.dto.UpdateAlbumRequestParam;
import com.agoodidea.photoAlbum.model.entity.Album;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
@Component
public interface AlbumUpdateMapstruct extends BaseMapStruct<UpdateAlbumRequestParam, Album> {
    @Override
    Album source2target(UpdateAlbumRequestParam source);

    @Override
    List<Album> source2target(List<UpdateAlbumRequestParam> source);

    @Override
    List<Album> source2target(Stream<UpdateAlbumRequestParam> source);
}
