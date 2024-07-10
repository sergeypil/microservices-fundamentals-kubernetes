package net.serg.storageservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemovedStoragesDto {

    private Long[] ids;
}
