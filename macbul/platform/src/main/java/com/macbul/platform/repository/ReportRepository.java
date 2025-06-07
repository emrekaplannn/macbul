// src/main/java/com/macbul/platform/repository/ReportRepository.java
package com.macbul.platform.repository;

import com.macbul.platform.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CRUD + list-by-match/reporter/reported for Report.
 */
public interface ReportRepository extends JpaRepository<Report, String> {

    List<Report> findByMatchId(String matchId);

    List<Report> findByReporterId(String reporterUserId);

    List<Report> findByReportedId(String reportedUserId);
}
