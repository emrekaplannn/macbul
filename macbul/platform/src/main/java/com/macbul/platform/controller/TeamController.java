// src/main/java/com/macbul/platform/controller/TeamController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Team API", description = "Create, read, update, delete, list teams")
@RestController
@RequestMapping("/v1/teams")
public class TeamController {

    @Autowired private TeamService teamService;

    @Operation(summary = "Create team")
    @PostMapping
    public ResponseEntity<TeamDto> create(@RequestBody TeamCreateRequest req) {
        return ResponseEntity.ok(teamService.createTeam(req));
    }

    @Operation(summary = "Get team by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @Operation(summary = "List all teams")
    @GetMapping
    public ResponseEntity<List<TeamDto>> listAll() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @Operation(summary = "List teams by match ID")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<TeamDto>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(teamService.getTeamsByMatchId(matchId));
    }

    @Operation(summary = "Update team average score")
    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> update(
        @PathVariable String id,
        @RequestBody TeamUpdateRequest req
    ) {
        return ResponseEntity.ok(teamService.updateTeam(id, req));
    }

    @Operation(summary = "Delete team")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
