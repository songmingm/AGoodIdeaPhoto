package com.agoodidea.photoAlbum.utils.mapstruct;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.MapperConfig;

import java.util.List;
import java.util.stream.Stream;

/**
 * 对象转换工具
 *
 * @author mmsongj
 */
@MapperConfig
public interface BaseMapStruct<S, T> {

    /**
     * 映射同名属性
     *
     * @param source 源数据
     * @return 返回数据类型
     */
    T source2target(S source);

    /**
     * 集合形式的映射同名属性
     *
     * @param source 源数据集合
     * @return 返回数据类型集合
     */
    @InheritConfiguration(name = "source2target")
    List<T> source2target(List<S> source);


    /**
     * 集合流形式的映射同名属性
     *
     * @param source 源数据流
     * @return 返回数据类型集合
     */
    List<T> source2target(Stream<S> source);

}
