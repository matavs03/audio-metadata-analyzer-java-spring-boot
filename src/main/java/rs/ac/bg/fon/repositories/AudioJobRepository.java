package rs.ac.bg.fon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.domain.AudioJob;

import java.util.UUID;

public interface AudioJobRepository extends JpaRepository<AudioJob, UUID> {
}
