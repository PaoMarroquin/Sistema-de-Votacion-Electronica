package com.votacion.vote.controller;

import com.votacion.vote.model.Voto;
import com.votacion.vote.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/vote")
public class VotoController {

    private final VotoService votoService;

    @Autowired
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    // RF-002 - Emitir voto inicial (sin confirmar aún)
    @PostMapping("/enviar")
    public ResponseEntity<?> emitirVoto(
            @RequestParam UUID userId,
            @RequestParam UUID eleccionId,
            @RequestParam UUID candidatoId
    ) {
        try {
            Voto voto = votoService.emitirVoto(userId, eleccionId, candidatoId);
            return ResponseEntity.ok(voto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RF-004 - Confirmar voto (cambiar estado a CONFIRMADO)
    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarVoto(@RequestBody Voto voto) {
        try {
            Voto confirmado = votoService.guardarVoto(voto);
            return ResponseEntity.ok(confirmado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al confirmar el voto.");
        }
    }

    // RF-004 - Obtener resumen antes de confirmar
    @GetMapping("/confirmacion/{votoId}")
    public ResponseEntity<?> obtenerResumen(@PathVariable UUID votoId) {
        try {
            Map<String, Object> resumen = votoService.obtenerResumenVoto(votoId);
            return ResponseEntity.ok(resumen);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // RF-003 - Simular boleta electrónica (candidatos simulados por ahora)
    @GetMapping("/boleta/{eleccionId}")
    public ResponseEntity<?> obtenerBoleta(@PathVariable UUID eleccionId) {
        // Simulación básica de candidatos (hasta tener election-service)
        List<Map<String, Object>> candidatos = new ArrayList<>();
        candidatos.add(Map.of("candidatoId", UUID.randomUUID(), "nombre", "Candidato A"));
        candidatos.add(Map.of("candidatoId", UUID.randomUUID(), "nombre", "Candidato B"));

        Map<String, Object> boleta = new HashMap<>();
        boleta.put("eleccionId", eleccionId);
        boleta.put("candidatos", candidatos);

        return ResponseEntity.ok(boleta);
    }

    // RF-008 - Resultados para administrador
    @GetMapping("/resultado")
    public ResponseEntity<?> obtenerResultados() {
        Map<UUID, Long> resultados = votoService.obtenerResultados();
        return ResponseEntity.ok(resultados);
    }

    // Extra: verificar si el usuario ya votó
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> yaVoto(
            @RequestParam UUID userId,
            @RequestParam UUID eleccionId
    ) {
        boolean yaVoto = votoService.yaEmitioVoto(userId, eleccionId);
        return ResponseEntity.ok(yaVoto);
    }
}
