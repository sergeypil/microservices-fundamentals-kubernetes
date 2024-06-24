package net.serg.resourceprocessor.dto;

import lombok.Builder;

@Builder
public record SongMetadataDto(String name, String artist, String album, String length, Long resourceId, String year) {

}
