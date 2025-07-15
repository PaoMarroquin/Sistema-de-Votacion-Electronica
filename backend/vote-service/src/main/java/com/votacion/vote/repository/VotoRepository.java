package com.votacion.vote.repository;

import com.votacion.vote.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VotoRepository extends JpaRepository<Voto, UUID> {
    Optional<Voto> findByUserIdAndEleccionId(UUID userId, UUID eleccionId);
}
