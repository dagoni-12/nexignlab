package ru.anger.nexignlab.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anger.nexignlab.services.CdrGeneratorService;
import ru.anger.nexignlab.services.CdrReportService;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CdrGeneratorController {

    @Autowired
    private CdrReportService cdrReportService;

    @Autowired
    private CdrGeneratorService cdrGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateCdrRecords() {
        cdrGeneratorService.generateCdrRecords();
        return ResponseEntity.ok("CDR records generated successfully.");
    }

    @PostMapping("/generate-report")
    public ResponseEntity<String> generateReport(
            @RequestParam String msisdn,
            @RequestParam String startTime,
            @RequestParam String endTime) {

        String reportId;
        try {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            reportId = cdrReportService.generateCdrReport(msisdn, start, end);
        } catch (DateTimeException e) {
            throw e;
        }
        return ResponseEntity.ok("Report generated successfully. Request ID: " + reportId);
    }
}