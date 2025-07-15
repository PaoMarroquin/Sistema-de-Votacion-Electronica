package com.votacion.vote.service;

import com.votacion.vote.model.Voto;
import com.votacion.vote.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VotoService {

    private final VotoRepository votoRepository;

    @Autowired
    public VotoService(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    public boolean yaEmitioVoto(UUID userId, UUID eleccionId) {
        return votoRepository.findByUserIdAndEleccionId(userId, eleccionId).isPresent();
    }

    public Voto emitirVoto(UUID userId, UUID eleccionId, UUID candidatoId) {
        if (yaEmitioVoto(userId, eleccionId)) {
            throw new IllegalStateException("El usuario ya emitió su voto en esta elección.");
        }

        Voto voto = new Voto();
        voto.setId(UUID.randomUUID());
        voto.setUserId(userId);
        voto.setEleccionId(eleccionId);
        voto.setCandidatoId(candidatoId);
        voto.setEstado("VALIDO");

        return votoRepository.save(voto);
    }

    public Voto guardarVoto(Voto voto) {
        voto.setEstado("CONFIRMADO");
        return votoRepository.save(voto);
    }

    public Map<String, Object> obtenerResumenVoto(UUID votoId) {
        Optional<Voto> votoOpt = votoRepository.findById(votoId);

        if (votoOpt.isEmpty()) {
            throw new NoSuchElementException("Voto no encontrado.");
        }

        Voto voto = votoOpt.get();

        Map<String, Object> resumen = new LinkedHashMap<>();
        resumen.put("votoId", voto.getId());
        resumen.put("usuario", voto.getUserId());
        resumen.put("eleccion", voto.getEleccionId());
        resumen.put("candidato", voto.getCandidatoId());
        resumen.put("estado", voto.getEstado());

        return resumen;
    }

    public Map<UUID, Long> obtenerResultados() {
        List<Voto> votos = votoRepository.findAll();
        Map<UUID, Long> conteo = new HashMap<>();

        for (Voto voto : votos) {
            UUID candidatoId = voto.getCandidatoId();
            conteo.put(candidatoId, conteo.getOrDefault(candidatoId, 0L) + 1);
        }

        return conteo;
    }
}
