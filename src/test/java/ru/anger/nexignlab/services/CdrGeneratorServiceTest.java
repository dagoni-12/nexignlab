package ru.anger.nexignlab.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.anger.nexignlab.repositories.CdrRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class CdrGeneratorServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @InjectMocks
    private CdrGeneratorService cdrGeneratorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateCdrRecords() {
        // Вызов метода генерации CDR-записей
        cdrGeneratorService.generateCdrRecords();

        // Проверка вызова метода удаления всех записей
        verify(cdrRepository, times(1)).deleteAll();

        // Проверка вызова метода сохранения записей
        verify(cdrRepository, times(1)).saveAll(anyList());
    }
}
