package com.votation.election.mapper;

import java.util.stream.Collectors;

import com.votation.election.dto.ElectionResponseDTO;
import com.votation.election.model.Election;

public class ElectionMapper {
    public static ElectionResponseDTO toDto(Election election) {
        return new ElectionResponseDTO(
                election.getId(),
                election.getName(),
                election.getStartDate(),
                election.getEndDate(),
                election.isActive(),
                election.getCandidatesInElection().stream()
                        .map(c -> c.getName())
                        .collect(Collectors.toList()));
    }
}