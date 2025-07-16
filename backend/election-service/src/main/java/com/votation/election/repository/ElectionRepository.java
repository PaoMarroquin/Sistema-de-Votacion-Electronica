package com.votation.election.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votation.election.model.Election;

public interface ElectionRepository extends JpaRepository<Election, UUID> {
}