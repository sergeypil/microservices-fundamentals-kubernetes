package net.serg.storageservice.service;

import net.serg.storageservice.dto.StorageDto;
import net.serg.storageservice.entity.StorageObject;

import java.util.List;

public interface StorageService {

    StorageDto saveStorage(StorageDto storageDto);

    List<StorageDto> getAllStorages();

    Long[] removeStorages(Long[] ids);

    List<StorageDto> getStorageByType(String type);
}
