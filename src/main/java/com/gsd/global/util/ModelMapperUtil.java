package com.gsd.global.util;


import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

/**
 * Created by Yohan lee
 * Created on 2021/11/28.
 **/
public class ModelMapperUtil {

    private final static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
        ;
    }

    public static <D, E>  D toDTO(E entity, Class<? extends D> dto) {
        return modelMapper.map(entity, dto);
    }

    public static <E, D> E toEntity(D dto, Class<? extends E> entity) {
        return modelMapper.map(dto, entity);
    }


}
