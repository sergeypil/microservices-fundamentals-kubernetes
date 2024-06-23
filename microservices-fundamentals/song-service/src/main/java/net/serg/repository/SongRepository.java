package net.serg.repository;

import net.serg.entity.SongMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public interface SongRepository extends JpaRepository<SongMetadata, Long> {

    Optional<SongMetadata> findByResourceId(long resourceId);
    void deleteByResourceIdIn(Set<Long> resourceIds);
}
