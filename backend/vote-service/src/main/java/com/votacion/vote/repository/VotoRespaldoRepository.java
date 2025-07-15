package com.votacion.vote.repository;

import com.votacion.vote.model.VotoRespaldo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VotoRespaldoRepository extends JpaRepository<VotoRespaldo, UUID> {
}
