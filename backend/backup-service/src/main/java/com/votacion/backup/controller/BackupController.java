package com.votacion.backup.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.votacion.backup.model.VotoBackup;
import com.votacion.backup.service.BackupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarBackup(@RequestBody VotoBackup voto) {
        try {
            backupService.respaldarVoto(voto);
            return ResponseEntity.ok("Respaldo creado en MinIO");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al respaldar el voto: " + e.getMessage());
        }
    }


    @GetMapping("/descargar/{id}")
    public ResponseEntity<?> descargar(@PathVariable UUID id) {
        try {
            InputStream input = backupService.descargarVoto(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=voto_" + id + ".json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new InputStreamResource(input));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Respaldo no encontrado: " + e.getMessage());
        }
    }

    @GetMapping("/existe/{id}")
    public ResponseEntity<?> existe(@PathVariable UUID id) {
        boolean existe = backupService.existeVoto(id);
        return ResponseEntity.ok(existe);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable UUID id) {
        try {
            backupService.eliminarVoto(id);
            return ResponseEntity.ok("Respaldo eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar respaldo: " + e.getMessage());
        }
    }
}
