package ru.anger.nexignlab.services;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.anger.nexignlab.models.Cdr;
import ru.anger.nexignlab.repositories.CdrRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class CdrReportService {

    @Autowired
    private CdrRepository cdrRepository;

    public String generateCdrReport(String msisdn, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new DateTimeException("Starting date is later then end date");
        }
        List<Cdr> cdrRecords = cdrRepository.findByCallerOrReceiver(msisdn, startTime, endTime);

        String reportContent = generateReportContent(cdrRecords);
        String reportId;
        try {
            reportId = saveReportToFile(reportContent, msisdn);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save report: " + e.getMessage());
        }
        return reportId;
    }

    private String generateReportContent(List<Cdr> cdrRecords) {
        StringBuilder report = new StringBuilder();

        for (Cdr cdr : cdrRecords) {
            report.append(cdr.getCallType()).append(",")
                    .append(cdr.getCaller()).append(",")
                    .append(cdr.getReceiver()).append(",")
                    .append(cdr.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append(",")
                    .append(cdr.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
        }

        return report.toString();
    }

    private String saveReportToFile(String reportContent, String msisdn) throws IOException {
        File reportsDirectory = new File("reports");
        if (!reportsDirectory.exists()) {
            if (!reportsDirectory.mkdirs()) {
                throw new RuntimeException("Failed to create reports directory.");
            }
        }

        String reportId = UUID.randomUUID().toString();
        String fileName = msisdn + "_" + reportId + ".csv";
        String filePath = "reports/" + fileName;

        try (FileWriter fileWriter = new FileWriter(filePath);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            String[] header = {"CallType", "Caller", "Receiver", "StartTime", "EndTime"};
            csvWriter.writeNext(header);

            String[] lines = reportContent.split("\n");
            for (String line : lines) {
                String[] columns = line.split(",");
                csvWriter.writeNext(columns);
            }
        }
        return reportId;
    }
}
