package ru.anger.nexignlab.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.anger.nexignlab.models.Udr;
import ru.anger.nexignlab.services.UdrService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UdrControllerTest {

    @Mock
    private UdrService udrService;

    @InjectMocks
    private UdrController udrController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUdrByMsisdn() {
        // Подготовка данных
        String msisdn = "79991112233";
        Udr udr = new Udr(msisdn, "00:10:00", "00:15:00");

        when(udrService.calculateUdrForSubscriber(msisdn, "2025-01")).thenReturn(udr);

        // Вызов метода
        ResponseEntity<Udr> response = udrController.getUdrByMsisdn(msisdn, "2025-01");

        // Проверка результата
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(udr, response.getBody());
    }

    @Test
    void testGetAllUdr() {
        // Подготовка данных
        List<Udr> udrList = Arrays.asList(
                new Udr("79991112233", "00:10:00", "00:15:00"),
                new Udr("79991112244", "00:20:00", "00:25:00")
        );

        when(udrService.calculateUdrForAllSubscribers("2025-01")).thenReturn(udrList);

        // Вызов метода
        ResponseEntity<List<Udr>> response = udrController.getAllUdr("2025-01");

        // Проверка результата
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(udrList, response.getBody());
    }
}
