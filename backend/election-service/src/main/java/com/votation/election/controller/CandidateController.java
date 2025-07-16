package com.votation.election.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votation.election.dto.CandidateRequestDTO;
import com.votation.election.dto.CandidateResponseDTO;
import com.votation.election.service.CandidateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public CandidateResponseDTO create(@Valid @RequestBody CandidateRequestDTO dto) {
        return candidateService.createCandidate(dto);
    }

    @GetMapping("/{id}")
    public CandidateResponseDTO get(@PathVariable UUID id) {
        return candidateService.getCandidateById(id);
    }

    @GetMapping
    public List<CandidateResponseDTO> list() {
        return candidateService.getAllCandidates();
    }

    @PutMapping("/{id}")
    public CandidateResponseDTO update(@PathVariable UUID id, @Valid @RequestBody CandidateRequestDTO dto) {
        return candidateService.updateCandidate(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
    }
}