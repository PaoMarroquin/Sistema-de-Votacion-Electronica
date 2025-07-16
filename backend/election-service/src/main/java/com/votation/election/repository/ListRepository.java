package com.votation.election.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votation.election.model.ListEntity;

public interface ListRepository extends JpaRepository<ListEntity, UUID> {
}