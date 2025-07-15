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
}
