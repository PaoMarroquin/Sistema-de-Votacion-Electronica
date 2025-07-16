package com.votation.election.dto;

import java.util.List;
import java.util.UUID;

public record CandidateResponseDTO(
    UUID id,
    String name,
    UUID partyId,
    String candidateNumber,
    String photoUrl,
    String description,
    List<UUID> electionIds
) {}
