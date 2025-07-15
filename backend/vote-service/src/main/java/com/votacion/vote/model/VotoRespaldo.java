package com.votacion.vote.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "voto_respaldo")
public class VotoRespaldo {

    @Id
    @Column(name = "voto_id")
    private UUID votoId;

    @OneToOne
    @JoinColumn(name = "voto_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Voto voto;

    @Column(name = "minio_path")
    private String minioPath;

    @Column(name = "hash")
    private String hash;

    @Column(name = "fecha_respaldo", insertable = false, updatable = false)
    private LocalDateTime fechaRespaldo;

    // Getters y setters
}
