package com.ef;

import com.ef.model.RequestLogModel;
import com.ef.parser.LogParser;
import com.ef.service.InsertServiceImpl;
import com.ef.service.LogServiceFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.ef.Constants.DB_PASSWORD;
import static com.ef.Constants.DB_USER;
import static com.ef.Constants.LOG_FILE_PATH;

public class Parser {

    public static void main(String[] args) {
        String startDate = args[0].split("=")[1];
        String duration = args[1].split("=")[1];
        int threshold = Integer.valueOf(args[2].split("=")[1]);
        Duration du = Duration.forCode(duration);
        LogParser parser = new LogParser();

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, Constants.inputDateTimeFormatter);
        LocalDateTime endDateTime = startDateTime.plusHours(du.getDuration());
        System.out.println("Processing batch with arguments: " + args[0] + " " + args[1] + " " + args[2]);
        System.out.println("Using configuration: \nlog file path: " + LOG_FILE_PATH + " \ndb:username = " + DB_USER + " \ndb:password = " + DB_PASSWORD);

        // extract the data from the log
        HashMap<String, RequestLogModel> occurrenceLog = parser.parse(LOG_FILE_PATH, startDateTime, endDateTime);

        // filter values that match the requirements
        List<RequestLogModel> requestList = occurrenceLog.values()
                .stream().
                        filter((item) -> item.getNoOfRequest() >= threshold)
                .collect(Collectors.toList());
        try {
            InsertServiceImpl logService = LogServiceFactory.getLogService();
            logService.insert(requestList);
        } catch (SQLException e) {
            throw new RuntimeException("Oops, something went wrong", e);
        }
        System.out.println("Parsing and loading data that matched input parameters:" + args[0] + " " + args[1] + " " + args[2] + " completed successfully.");
    }
}
