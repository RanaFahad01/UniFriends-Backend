package com.ranafahad.unifriends.report;

import com.ranafahad.unifriends.post.PostService;
import com.ranafahad.unifriends.report.dto.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final PostService postService;

    @GetMapping("/reports")
    public ResponseEntity<Page<ReportResponse>> getReports(
            @RequestParam(defaultValue = "PENDING") ReportStatus status,
            @RequestParam(defaultValue = "0") int page) {
        Page<Report> reports = reportService.findByStatus(status, PageRequest.of(page, 20));
        return ResponseEntity.ok(reports.map(ReportResponse::from));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(ReportResponse.from(reportService.findById(id)));
    }

    @PostMapping("/reports/{id}/dismiss")
    public ResponseEntity<ReportResponse> dismissReport(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(ReportResponse.from(reportService.dismissReport(id, principal.getName())));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}
