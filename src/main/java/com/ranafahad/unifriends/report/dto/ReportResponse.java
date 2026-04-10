package com.ranafahad.unifriends.report.dto;

import com.ranafahad.unifriends.report.Report;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        Long reporterId,
        String reporterUsername,
        Long reportedUserId,
        String reportedUsername,
        String reason,
        String status,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getReporter().getId(),
                report.getReporter().getUsername(),
                report.getReportedUser().getId(),
                report.getReportedUser().getUsername(),
                report.getReason(),
                report.getStatus().name(),
                report.getCreatedAt(),
                report.getReviewedAt()
        );
    }
}
