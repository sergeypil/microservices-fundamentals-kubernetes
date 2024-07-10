package net.serg.storageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageDto {

    private Long id;
    private StorageType type;
    private String bucket;
    private String path;

    public enum StorageType {
        PERMANENT,
        STAGING;
    }
}
