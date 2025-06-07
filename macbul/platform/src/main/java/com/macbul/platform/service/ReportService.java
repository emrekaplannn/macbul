// src/main/java/com/macbul/platform/service/ReportService.java
package com.macbul.platform.service;

import com.macbul.platform.dto.*;
import com.macbul.platform.exception.ResourceNotFoundException;
import com.macbul.platform.model.*;
import com.macbul.platform.repository.*;
import com.macbul.platform.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class ReportService {

    @Autowired private ReportRepository reportRepo;
    @Autowired private MatchRepository  matchRepo;
    @Autowired private UserRepository   userRepo;
    @Autowired private MapperUtil       mapper;

    /** Create a new report */
    public ReportDto createReport(ReportCreateRequest req) {
        Match match = matchRepo.findById(req.getMatchId())
            .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + req.getMatchId()));
        User reporter = userRepo.findById(req.getReporterUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Reporter not found: " + req.getReporterUserId()));
        User reported = userRepo.findById(req.getReportedUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Reported user not found: " + req.getReportedUserId()));

        Report r = new Report();
        r.setId(UUID.randomUUID().toString());
        r.setMatch(match);
        r.setReporter(reporter);
        r.setReported(reported);
        r.setReason(req.getReason());
        r.setDetails(req.getDetails());
        r.setCreatedAt(req.getCreatedAt() != null ? req.getCreatedAt() : System.currentTimeMillis());
        // status defaults to NEW

        return mapper.toReportDto(reportRepo.save(r));
    }

    /** Get one by ID */
    public ReportDto getReportById(String id) {
        Report r = reportRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));
        return mapper.toReportDto(r);
    }

    /** List all */
    public List<ReportDto> getAllReports() {
        return reportRepo.findAll()
            .stream()
            .map(mapper::toReportDto)
            .collect(Collectors.toList());
    }

    /** List by match */
    public List<ReportDto> getByMatchId(String matchId) {
        return reportRepo.findByMatchId(matchId)
            .stream()
            .map(mapper::toReportDto)
            .collect(Collectors.toList());
    }

    /** List by reporter */
    public List<ReportDto> getByReporter(String reporterUserId) {
        return reportRepo.findByReporterId(reporterUserId)
            .stream()
            .map(mapper::toReportDto)
            .collect(Collectors.toList());
    }

    /** List by reported */
    public List<ReportDto> getByReported(String reportedUserId) {
        return reportRepo.findByReportedId(reportedUserId)
            .stream()
            .map(mapper::toReportDto)
            .collect(Collectors.toList());
    }

    /** Update status/resolvedAt */
    public ReportDto updateReport(String id, ReportUpdateRequest req) {
        Report r = reportRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found: " + id));

        if (req.getStatus() != null) {
            r.setStatus(req.getStatus());
        }
        if (req.getResolvedAt() != null) {
            r.setResolvedAt(req.getResolvedAt());
        }

        return mapper.toReportDto(reportRepo.save(r));
    }

    /** Delete */
    public void deleteReport(String id) {
        if (!reportRepo.existsById(id)) {
            throw new ResourceNotFoundException("Report not found: " + id);
        }
        reportRepo.deleteById(id);
    }
}
