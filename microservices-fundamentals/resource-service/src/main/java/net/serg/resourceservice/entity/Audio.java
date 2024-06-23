package net.serg.resourceservice.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audio")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "audio_seq")
    @SequenceGenerator(name = "audio_seq", sequenceName = "audio_seq", allocationSize = 1)
    private Long id;

    @Lob
    @Column(name = "audio_data")
    private byte[] audioData;

}
