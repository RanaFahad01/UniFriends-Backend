package com.ranafahad.unifriends.report;

import com.ranafahad.unifriends.report.dto.ReportResponse;
import com.ranafahad.unifriends.user.User;
import com.ranafahad.unifriends.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserService userService;

    @Transactional
    public Report createReport(String callerEmail, Long reportedUserId, String reason) {
        User reporter = userService.findByEmail(callerEmail);

        // Cannot report yourself
        if (reporter.getId().equals(reportedUserId)) {
            throw new IllegalStateException("You cannot report yourself");
        }
        // Validate reason length
        if (reason == null || reason.length() < 10 || reason.length() > 500) {
            throw new IllegalStateException("Report reason must be between 10 and 500 characters");
        }
        // Cannot have duplicate pending report
        if (reportRepository.existsByReporterIdAndReportedUserIdAndStatus(
                reporter.getId(), reportedUserId, ReportStatus.PENDING)) {
            throw new IllegalStateException("You already have a pending report against this user");
        }
        // Check reporter not banned
        if (reporter.getBannedAt() != null) {
            throw new IllegalStateException("Banned users cannot submit reports");
        }

        User reportedUser = userService.findById(reportedUserId);

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(reason)
                .build();
        return reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public Page<ReportResponse> findByStatus(ReportStatus status, Pageable pageable) {
        return reportRepository.findByStatus(status, pageable)
                .map(ReportResponse::from);
    }

    @Transactional(readOnly = true)
    public ReportResponse getReport(Long id) {
        return ReportResponse.from(findReportEntity(id));
    }

    // Package-private — used internally by dismissReport
    Report findReportEntity(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));
    }

    @Transactional
    public ReportResponse dismissReport(Long reportId, String callerEmail) {
        Report report = findReportEntity(reportId);
        User reviewer = userService.findByEmail(callerEmail);

        report.setStatus(ReportStatus.DISMISSED);
        report.setReviewedAt(LocalDateTime.now());
        report.setReviewedBy(reviewer);
        return ReportResponse.from(reportRepository.save(report));
    }
}
