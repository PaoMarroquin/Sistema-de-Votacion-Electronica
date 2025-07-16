package com.votation.election.mapper;

import com.votation.election.dto.CandidateRequestDTO;
import com.votation.election.model.Candidate;
import com.votation.election.model.Election;

public class CandidateMapper {
    public static Candidate toEntity(CandidateRequestDTO dto, Election election) {
        Candidate candidate = new Candidate();
        candidate.setName(dto.name());
        candidate.setList(dto.list());
        candidate.setCandidateNumber(dto.candidateNumber());
        candidate.setPhotoUrl(dto.photoUrl());
        candidate.setDescription(dto.description());
        //candidate.setElections(election);
        return candidate;
    }
}
