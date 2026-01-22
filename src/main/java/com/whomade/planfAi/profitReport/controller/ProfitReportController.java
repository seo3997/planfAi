package com.whomade.planfAi.profitReport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whomade.planfAi.profitReport.service.ProfitReportService;
import com.whomade.planfAi.profitReport.vo.ProfitVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ProfitReportController {

    private final ProfitReportService profitReportService;
    private final ObjectMapper objectMapper;

    public ProfitReportController(ProfitReportService profitReportService, ObjectMapper objectMapper) {
        this.profitReportService = profitReportService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<String> saveReport(@RequestBody Map<String, Object> payload) {
        try {
            String userId = (String) payload.get("userId"); // Treat userId as email for simplicity
            Object content = payload.get("content");

            if (userId == null || content == null) {
                return ResponseEntity.badRequest().body("userId (email) and content are required");
            }

            // Delegate business logic to Service
            Long userNo = profitReportService.findOrCreateUser(userId);

            // Extract Report ID if exists (for Update)
            Long reportId = null;
            if (payload.get("id") != null) {
                reportId = ((Number) payload.get("id")).longValue();
            }

            String reportDataJson = objectMapper.writeValueAsString(content);

            profitReportService.saveReport(userNo, reportDataJson, reportId);

            return ResponseEntity.ok("Report saved successfully");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing JSON");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProfitVo>> getReports(@PathVariable String userId) {
        Long userNo = profitReportService.findOrCreateUser(userId);
        List<ProfitVo> reports = profitReportService.getReports(userNo);
        return ResponseEntity.ok(reports);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        profitReportService.deleteReport(reportId);
        return ResponseEntity.ok("Report deleted successfully");
    }
}
