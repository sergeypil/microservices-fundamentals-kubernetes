package net.serg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "song_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SongMetadata {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="song_metadata_seq")
    @SequenceGenerator(name="song_metadata_seq", sequenceName="song_metadata_SEQ", allocationSize=1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "length")
    private String length;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "song_year")
    private String year;
}
