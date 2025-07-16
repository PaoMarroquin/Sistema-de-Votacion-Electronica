package com.votation.election.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.votation.election.dto.ElectionRequestDTO;
import com.votation.election.dto.ElectionResponseDTO;
import com.votation.election.exception.ResourceNotFoundException;
import com.votation.election.mapper.ElectionMapper;
import com.votation.election.model.Candidate;
import com.votation.election.model.Election;
import com.votation.election.repository.CandidateRepository;
import com.votation.election.repository.ElectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ElectionService {
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public ElectionResponseDTO createElection(ElectionRequestDTO dto) {
        List<Candidate> candidates = candidateRepository.findAllById(dto.candidateIds());
        Election election = new Election();
        election.setName(dto.name());
        election.setStartDate(dto.startDate());
        election.setEndDate(dto.endDate());
        election.setActive(dto.active());
        election.setCandidatesInElection(candidates);

        Election savedElection = electionRepository.save(election);
        return ElectionMapper.toDto(savedElection);
    }

    public ElectionResponseDTO getElection(UUID id) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with id " + id));
        return ElectionMapper.toDto(election);
    }

    public List<ElectionResponseDTO> getAllElections() {
        return electionRepository.findAll().stream()
                .map(ElectionMapper::toDto)
                .toList();
    }

    public void deleteElection(UUID id) {
        if (!electionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Election not found with id " + id);
        }
        electionRepository.deleteById(id);
    }

    public ElectionResponseDTO updateElection(UUID id, ElectionRequestDTO dto) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found with id " + id));

        election.setName(dto.name());
        election.setStartDate(dto.startDate());
        election.setEndDate(dto.endDate());
        election.setActive(dto.active());

        List<Candidate> candidates = candidateRepository.findAllById(dto.candidateIds());
        election.setCandidatesInElection(candidates);

        Election updatedElection = electionRepository.save(election);
        return ElectionMapper.toDto(updatedElection);
    }
}