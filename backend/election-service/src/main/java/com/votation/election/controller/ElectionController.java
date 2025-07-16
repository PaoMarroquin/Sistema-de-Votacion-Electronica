package com.votation.election.controller;

import com.votation.election.dto.ElectionRequestDTO;
import com.votation.election.dto.ElectionResponseDTO;
import com.votation.election.service.ElectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;

    @PostMapping
    public ElectionResponseDTO create(@Valid @RequestBody ElectionRequestDTO dto) {
        return electionService.createElection(dto);
    }

    @GetMapping("/{id}")
    public ElectionResponseDTO get(@PathVariable UUID id) {
        return electionService.getElection(id);
    }

    @PutMapping("/{id}")
    public ElectionResponseDTO update(@PathVariable UUID id, @Valid @RequestBody ElectionRequestDTO dto) {
        return electionService.updateElection(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        electionService.deleteElection(id);
    }

    @GetMapping
    public List<ElectionResponseDTO> list() {
        return electionService.getAllElections();
    }
}