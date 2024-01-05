package net.serg.repository;

import net.serg.entity.SongMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SongRepository extends JpaRepository<SongMetadata, Long> {

}
