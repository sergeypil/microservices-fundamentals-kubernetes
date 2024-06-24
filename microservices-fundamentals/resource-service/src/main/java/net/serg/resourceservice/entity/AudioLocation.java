package net.serg.resourceservice.entity;

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
@Table(name = "audio_location")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AudioLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "audio_location_seq")
    @SequenceGenerator(name = "audio_location_seq", sequenceName = "audio_location_seq", allocationSize = 1)
    private Long id;
    
    private String url;
}
