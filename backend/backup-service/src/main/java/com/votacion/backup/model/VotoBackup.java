package com.votacion.backup.model;

import lombok.Data;

import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class VotoBackup {
    private UUID id;
    private UUID userId;
    private UUID eleccionId;
    private UUID candidatoId;
    private LocalDateTime fechaEmision;
    private String estado;
}
