package net.serg.resourceservice.repository;

import net.serg.resourceservice.entity.AudioLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<AudioLocation, Long> {

}
