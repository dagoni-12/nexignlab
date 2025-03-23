package ru.anger.nexignlab.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anger.nexignlab.models.Cdr;
import ru.anger.nexignlab.repositories.CdrRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CdrReportServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @InjectMocks
    private CdrReportService cdrReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCdrReport() throws Exception {
        // Подготовка данных
        String msisdn = "79991112233";
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 31, 23, 59);

        Cdr cdr = new Cdr();
        cdr.setCallType("01");
        cdr.setCaller(msisdn);
        cdr.setReceiver("79991112244");
        cdr.setStartTime(startTime);
        cdr.setEndTime(startTime.plusMinutes(10));

        when(cdrRepository.findByCallerOrReceiver(msisdn, startTime, endTime))
                .thenReturn(Arrays.asList(cdr));

        // Вызов метода
        String reportId = cdrReportService.generateCdrReport(msisdn, startTime, endTime);

        // Проверка результата
        assertNotNull(reportId);
        verify(cdrRepository, times(1)).findByCallerOrReceiver(msisdn, startTime, endTime);
    }
}
