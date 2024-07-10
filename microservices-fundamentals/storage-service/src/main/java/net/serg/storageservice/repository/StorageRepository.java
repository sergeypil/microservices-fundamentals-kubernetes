package net.serg.storageservice.repository;

import net.serg.storageservice.entity.StorageObject;
import org.springframework.data.repository.CrudRepository;

public interface StorageRepository extends CrudRepository<StorageObject, Long> {

    Iterable<StorageObject> findByType(String type);
}
