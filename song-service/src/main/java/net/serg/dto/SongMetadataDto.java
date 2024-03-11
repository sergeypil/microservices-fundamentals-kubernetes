package net.serg.dto;

import lombok.Builder;

@Builder
public record SongMetadataDto(String name, String artist, String album, String length, Long resourceId, String year, String genre) {

}
