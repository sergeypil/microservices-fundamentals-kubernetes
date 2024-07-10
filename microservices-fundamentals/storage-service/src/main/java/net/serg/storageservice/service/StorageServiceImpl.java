package net.serg.storageservice.service;

import lombok.AllArgsConstructor;
import net.serg.storageservice.controller.StorageMapper;
import net.serg.storageservice.dto.StorageDto;
import net.serg.storageservice.entity.StorageObject;
import net.serg.storageservice.repository.StorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;

    @Override
    public StorageDto saveStorage(StorageDto storageDto) {
        StorageObject storageObject = storageMapper.mapDtoToEntity(storageDto);
        StorageObject saved = storageRepository.save(storageObject);
        return storageMapper.mapEntityToDto(saved);
    }

    @Override
    public List<StorageDto> getAllStorages() {
        return StreamSupport
            .stream(storageRepository
                        .findAll()
                        .spliterator(), false)
            .map(storageMapper::mapEntityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public Long[] removeStorages(Long[] ids) {
        var storages = storageRepository.findAllById(List.of(ids));
        storageRepository.deleteAll(storages);
        return StreamSupport
            .stream(storages.spliterator(), false)
            .map(StorageObject::getId)
            .toArray(Long[]::new);
    }

    @Override
    public List<StorageDto> getStorageByType(String type) {
        return StreamSupport
            .stream(storageRepository
                        .findByType(type)
                        .spliterator(), false)
            .map(storageMapper::mapEntityToDto)
            .collect(Collectors.toList());
    }
}
