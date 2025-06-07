// src/main/java/com/macbul/platform/controller/ReportController.java
package com.macbul.platform.controller;

import com.macbul.platform.dto.*;
import com.macbul.platform.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Report API", description = "File and manage user reports")
@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    @Autowired private ReportService service;

    @Operation(summary = "File a new report")
    @PostMapping
    public ResponseEntity<ReportDto> create(@RequestBody ReportCreateRequest req) {
        return ResponseEntity.ok(service.createReport(req));
    }

    @Operation(summary = "Get report by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getReportById(id));
    }

    @Operation(summary = "List all reports")
    @GetMapping
    public ResponseEntity<List<ReportDto>> listAll() {
        return ResponseEntity.ok(service.getAllReports());
    }

    @Operation(summary = "List reports for a match")
    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<ReportDto>> listByMatch(@PathVariable String matchId) {
        return ResponseEntity.ok(service.getByMatchId(matchId));
    }

    @Operation(summary = "List reports by reporter")
    @GetMapping("/reporter/{reporterUserId}")
    public ResponseEntity<List<ReportDto>> listByReporter(@PathVariable String reporterUserId) {
        return ResponseEntity.ok(service.getByReporter(reporterUserId));
    }

    @Operation(summary = "List reports against a user")
    @GetMapping("/reported/{reportedUserId}")
    public ResponseEntity<List<ReportDto>> listByReported(@PathVariable String reportedUserId) {
        return ResponseEntity.ok(service.getByReported(reportedUserId));
    }

    @Operation(summary = "Update report status or resolved timestamp")
    @PutMapping("/{id}")
    public ResponseEntity<ReportDto> update(
        @PathVariable String id,
        @RequestBody ReportUpdateRequest req
    ) {
        return ResponseEntity.ok(service.updateReport(id, req));
    }

    @Operation(summary = "Delete a report")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
