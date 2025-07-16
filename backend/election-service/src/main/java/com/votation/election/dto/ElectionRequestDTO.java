package com.votation.election.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ElectionRequestDTO(
        @NotBlank String name,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        boolean active,
        List<UUID> candidateIds) {

    public ElectionRequestDTO {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (candidateIds == null || candidateIds.isEmpty()) {
            throw new IllegalArgumentException("At least one candidate ID must be provided");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Election name cannot be null or blank");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }

        if (active && startDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Active elections cannot have a start date in the past");
        }

        if (endDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("End date cannot be in the past");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        if (startDate.isBefore(LocalDateTime.now()) && active) {
            throw new IllegalArgumentException("An election cannot be active if its start date is in the past");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}