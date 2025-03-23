package ru.anger.nexignlab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.anger.nexignlab.models.Cdr;
import ru.anger.nexignlab.models.Udr;
import ru.anger.nexignlab.repositories.CdrRepository;
import ru.anger.nexignlab.util.CdrNotFoundException;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UdrService {

    @Autowired
    private CdrRepository cdrRepository;

    public Udr calculateUdrForSubscriber(String msisdn, String period) {
        List<Cdr> cdrRecords;
        if (period == null || period.isEmpty()) {
            cdrRecords = cdrRepository.findByCallerOrReceiver(msisdn);
        } else {
            LocalDateTime startTime = getStartTimeForPeriod(period);
            LocalDateTime endTime = getEndTimeForPeriod(period);
            System.out.println(startTime);
            System.out.println(endTime);
            System.out.println(msisdn);
            cdrRecords = cdrRepository.findByCallerOrReceiver(msisdn, startTime, endTime);
        }

        if (cdrRecords.isEmpty())
            throw new CdrNotFoundException();

        Duration incomingCallDuration = calculateDuration(cdrRecords, "02");
        Duration outcomingCallDuration = calculateDuration(cdrRecords, "01");

        String incomingCallTime = formatDuration(incomingCallDuration);
        String outcomingCallTime = formatDuration(outcomingCallDuration);

        return new Udr(msisdn, incomingCallTime, outcomingCallTime);
    }

    public List<Udr> calculateUdrForAllSubscribers(String period) {
        List<String> allSubscribers;
        if (period == null || period.isEmpty()) {
            allSubscribers = cdrRepository.findAllSubscribers();
        } else {
            LocalDateTime startTime = getStartTimeForPeriod(period);
            LocalDateTime endTime = getEndTimeForPeriod(period);
            allSubscribers = cdrRepository.findAllSubscribers(startTime, endTime);
        }

        if (allSubscribers.isEmpty())
            throw new CdrNotFoundException();

        List<Udr> udrList = new ArrayList<>();
        for (String msisdn : allSubscribers) {
            Udr udr = calculateUdrForSubscriber(msisdn, period);
            udrList.add(udr);
        }
        return udrList;
    }

    private LocalDateTime getStartTimeForPeriod(String period) {
        String[] parts = period.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid period format. Expected format: YYYY-MM");
        }

        LocalDateTime date;

        int year = 0;
        int month = 0;

        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        int day = 1;

        if (month < 1 || month > 12) {
            throw new DateTimeException("Wrong month format. Month can only be from 1 to 12");
        }

        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    private LocalDateTime getEndTimeForPeriod(String period) {
        String[] parts = period.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid period format. Expected format: YYYY-MM");
        }

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = YearMonth.of(year, month).lengthOfMonth();

        return LocalDateTime.of(year, month, day, 23, 59, 59);
    }

    private Duration calculateDuration(List<Cdr> cdrRecords, String callType) {
        Duration totalDuration = Duration.ZERO;
        for (Cdr cdr : cdrRecords) {
            if (cdr.getCallType().equals(callType)) {
                Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
                totalDuration = totalDuration.plus(callDuration);
            }
        }
        return totalDuration;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
