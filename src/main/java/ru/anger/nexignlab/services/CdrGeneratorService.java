package ru.anger.nexignlab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.anger.nexignlab.models.Cdr;
import ru.anger.nexignlab.repositories.CdrRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CdrGeneratorService {

    @Autowired
    private CdrRepository cdrRepository;

    private final List<String> subscribers = List.of(
            "79876543221", "79996667755", "79123456789",
            "79992221122", "79993334455", "79994445566",
            "79995556677", "79996667788", "79997778899",
            "79998889900"
    );

    public void generateCdrRecords() {
        Random random = new Random();
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = startDate.plusYears(1);

        List<Cdr> records = new ArrayList<>();

        while (startDate.isBefore(endDate)) {
            String callType = random.nextBoolean() ? "01" : "02";
            String caller = subscribers.get(random.nextInt(subscribers.size()));
            String receiver = subscribers.get(random.nextInt(subscribers.size()));
            LocalDateTime callStartTime = startDate;
            LocalDateTime callEndTime = callStartTime.plusSeconds(random.nextInt(3600));

            Cdr record = new Cdr(
                    callType,
                    caller,
                    receiver,
                    callStartTime,
                    callEndTime
            );

            records.add(record);
            startDate = callEndTime.plusSeconds(random.nextInt(3600));
        }
        cdrRepository.deleteAll();
        cdrRepository.saveAll(records);
    }
}
