package com.votation.election.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Election name cannot be blank")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Start date cannot be null")
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Column(nullable = false)
    private LocalDateTime endDate;

    @ManyToMany(mappedBy = "elections", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Candidate> candidatesInElection;

    @ManyToMany(mappedBy = "elections", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ListEntity> listsInElection;

    @Column(nullable = false)
    private boolean active;
}