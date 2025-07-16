package com.votation.election.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votation.election.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
}
