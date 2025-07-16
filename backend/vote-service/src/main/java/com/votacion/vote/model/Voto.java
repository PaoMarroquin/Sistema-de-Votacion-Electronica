package com.votacion.vote.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "voto", indexes = {
        @Index(name = "idx_user_eleccion", columnList = "user_id, eleccion_id"),
        @Index(name = "idx_eleccion_candidato", columnList = "eleccion_id, candidato_id")
})

public class Voto {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "eleccion_id", nullable = false)
    private UUID eleccionId;

    @Column(name = "candidato_id", nullable = false)
    private UUID candidatoId;

    @Column(name = "fecha_emision", insertable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "estado")
    private String estado;

    // Getters y setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getEleccionId() {
        return eleccionId;
    }

    public void setEleccionId(UUID eleccionId) {
        this.eleccionId = eleccionId;
    }

    public UUID getCandidatoId() {
        return candidatoId;
    }

    public void setCandidatoId(UUID candidatoId) {
        this.candidatoId = candidatoId;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
