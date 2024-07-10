package net.serg.storageservice.controller;

import net.serg.storageservice.dto.StorageDto;
import net.serg.storageservice.entity.StorageObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "bucket", source = "bucket")
    @Mapping(target = "path", source = "path")
    StorageDto mapEntityToDto(StorageObject storageObject);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "bucket", source = "bucket")
    @Mapping(target = "path", source = "path")
    StorageObject mapDtoToEntity(StorageDto songDto);
}
