package ru.anger.nexignlab.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anger.nexignlab.models.Cdr;
import ru.anger.nexignlab.models.Udr;
import ru.anger.nexignlab.repositories.CdrRepository;
import ru.anger.nexignlab.util.CdrNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UdrServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @Mock
    private CdrGeneratorService cdrGeneratorService;

    @InjectMocks
    private UdrService udrService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateUdrForSubscriber() {
        // Подготовка данных
        String msisdn = "79991112233";

        Cdr cdr1 = new Cdr();
        cdr1.setCallType("01");
        cdr1.setCaller(msisdn);
        cdr1.setReceiver("79991112244");
        cdr1.setStartTime(LocalDateTime.now().minusDays(1));
        cdr1.setEndTime(LocalDateTime.now().minusDays(1).plusMinutes(10));

        Cdr cdr2 = new Cdr();
        cdr2.setCallType("02");
        cdr2.setCaller("79991112244");
        cdr2.setReceiver(msisdn);
        cdr2.setStartTime(LocalDateTime.now().minusDays(2));
        cdr2.setEndTime(LocalDateTime.now().minusDays(2).plusMinutes(25));

        // Сохраняем данные в базу
        when(cdrRepository.save(cdr1)).thenReturn(cdr1);
        when(cdrRepository.save(cdr2)).thenReturn(cdr2);
        cdrRepository.save(cdr1);
        cdrRepository.save(cdr2);

        // Настройка мок-объекта для поиска данных
        when(cdrRepository.findByCallerOrReceiver(msisdn))
                .thenReturn(Arrays.asList(cdr1, cdr2));

        // Вызов метода
        Udr udr = udrService.calculateUdrForSubscriber(msisdn, null);

        // Проверка результата
        assertEquals("79991112233", udr.getMsisdn());
        assertEquals("00:10:00", udr.getOutcomingCall().getTotalTime());
        assertEquals("00:25:00", udr.getIncomingCall().getTotalTime());
    }
}
