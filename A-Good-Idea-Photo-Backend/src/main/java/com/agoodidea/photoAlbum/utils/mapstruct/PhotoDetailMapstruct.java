package com.agoodidea.photoAlbum.utils.mapstruct;

import com.agoodidea.photoAlbum.model.entity.PhotoMetaData;
import com.agoodidea.photoAlbum.model.vo.PhotoMetaDataDetailVO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
@Component
public interface PhotoDetailMapstruct extends BaseMapStruct<PhotoMetaData, PhotoMetaDataDetailVO> {
    @Override
    PhotoMetaDataDetailVO source2target(PhotoMetaData source);

    @Override
    List<PhotoMetaDataDetailVO> source2target(List<PhotoMetaData> source);

    @Override
    List<PhotoMetaDataDetailVO> source2target(Stream<PhotoMetaData> source);
}
