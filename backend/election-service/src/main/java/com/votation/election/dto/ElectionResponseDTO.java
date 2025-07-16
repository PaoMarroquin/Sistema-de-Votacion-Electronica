package com.votation.election.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ElectionResponseDTO(
    UUID id,
    String name,
    LocalDateTime startDate,
    LocalDateTime endDate,
    boolean active,
    List<String> candidates
) {
    public ElectionResponseDTO {
        if (id == null) {
            throw new IllegalArgumentException("Election ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Election name cannot be blank");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}
