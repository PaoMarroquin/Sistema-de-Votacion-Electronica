
package com.votation.election.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.votation.election.dto.CandidateRequestDTO;
import com.votation.election.dto.CandidateResponseDTO;
import com.votation.election.model.Candidate;
import com.votation.election.model.Election;
import com.votation.election.model.ListEntity;
import com.votation.election.repository.CandidateRepository;
import com.votation.election.repository.ElectionRepository;
import com.votation.election.repository.ListRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ListRepository listEntityRepository;
    private final ElectionRepository electionRepository;

    public CandidateService(
            CandidateRepository candidateRepository,
            ListRepository listEntityRepository,
            ElectionRepository electionRepository) {
        this.candidateRepository = candidateRepository;
        this.listEntityRepository = listEntityRepository;
        this.electionRepository = electionRepository;
    }

    @Transactional
    public CandidateResponseDTO createCandidate(CandidateRequestDTO dto) {
        // Obtener el partido
        ListEntity list = listEntityRepository.findById(dto.list().getId())
                .orElseThrow(() -> new EntityNotFoundException("Party not found"));

        // Obtener las elecciones si se proporcionaron
        List<Election> elections = Optional.ofNullable(dto.electionIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(id -> electionRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Election not found: " + id)))
                .collect(Collectors.toList());

        // Crear entidad Candidate
        Candidate candidate = new Candidate();
        candidate.setName(dto.name());
        candidate.setList(list);
        candidate.setCandidateNumber(dto.candidateNumber());
        candidate.setPhotoUrl(dto.photoUrl());
        candidate.setDescription(dto.description());
        candidate.setElections(elections);

        // Guardar y devolver DTO
        Candidate saved = candidateRepository.save(candidate);
        return mapToResponseDTO(saved);
    }

    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public CandidateResponseDTO getCandidateById(UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Party not found"));
        return mapToResponseDTO(candidate);
    }

    public CandidateResponseDTO updateCandidate(UUID id, CandidateRequestDTO dto) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Party not found"));

        // Actualizar los campos
        candidate.setName(dto.name());
        candidate.setList(listEntityRepository.findById(dto.list().getId())
                .orElseThrow(() -> new EntityNotFoundException("Party not found")));
        candidate.setCandidateNumber(dto.candidateNumber());
        candidate.setPhotoUrl(dto.photoUrl());
        candidate.setDescription(dto.description());

        // Actualizar elecciones
        List<Election> elections = Optional.ofNullable(dto.electionIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(electionId -> electionRepository.findById(electionId)
                        .orElseThrow(() -> new EntityNotFoundException("Election not found: " + electionId)))
                .collect(Collectors.toList());
        candidate.setElections(elections);

        // Guardar y devolver DTO
        Candidate updated = candidateRepository.save(candidate);
        return mapToResponseDTO(updated);
    }

    public void deleteCandidate(UUID id) {
        if (!candidateRepository.existsById(id)) {
            throw new EntityNotFoundException("Party not found");
        }
        candidateRepository.deleteById(id);
    }

    // 🧭 Mapeador interno
    private CandidateResponseDTO mapToResponseDTO(Candidate candidate) {
        return new CandidateResponseDTO(
                candidate.getId(),
                candidate.getName(),
                candidate.getList().getId(),
                candidate.getCandidateNumber(),
                candidate.getPhotoUrl(),
                candidate.getDescription(),
                candidate.getElections().stream()
                        .map(Election::getId)
                        .toList());
    }
}
