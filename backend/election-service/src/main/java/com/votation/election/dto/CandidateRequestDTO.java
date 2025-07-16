// src/main/java/com/votation/election/dto/CandidateRequestDTO.java
package com.votation.election.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.UUID;

import com.votation.election.model.ListEntity;

public record CandidateRequestDTO(

    @NotBlank(message = "Candidate name cannot be blank")
    String name,

    @NotNull(message = "Party ID cannot be null")
    ListEntity list,

    @NotBlank(message = "Candidate number cannot be blank")
    String candidateNumber,

    @NotBlank(message = "Photo URL cannot be blank")
    String photoUrl,

    @NotBlank(message = "Description cannot be blank")
    String description,

    List<UUID> electionIds // puede estar vacío o null
) {}
