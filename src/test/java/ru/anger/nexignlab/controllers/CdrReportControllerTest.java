package ru.anger.nexignlab.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.anger.nexignlab.services.CdrReportService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CdrReportControllerTest {

    @Mock
    private CdrReportService cdrReportService;

    @InjectMocks
    private CdrGeneratorController cdrReportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateReport() {
        // Подготовка данных
        String msisdn = "79991112233";
        String startTime = "2025-01-01T00:00:00";
        String endTime = "2025-01-31T23:59:59";
        String reportId = "61f0c404-5cb3-11e7-907b-a6006ad3dba0";

        when(cdrReportService.generateCdrReport(msisdn, LocalDateTime.parse(startTime), LocalDateTime.parse(endTime)))
                .thenReturn(reportId);

        // Вызов метода
        ResponseEntity<String> response = cdrReportController.generateReport(msisdn, startTime, endTime);

        // Проверка результата
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Report generated successfully. Request ID: " + reportId, response.getBody());
    }
}
