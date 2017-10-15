package com.ef.parser;

import com.ef.model.Request;
import com.ef.model.RequestLogModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.ef.Constants.logDateTimeFormatter;

/**
 * Written with love
 *
 * @author Sasa Prsic 13/10/2017
 */
public class LogParser {


    public HashMap<String, RequestLogModel> parse(String stringPath, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // if the file is too big and load of data is in there with different ips this could lead to memory overflow
        // one solution could be increasing heap of using off heap memory like (ChronicleMap- open HFT or terracotta off heap )
        // lets assume that there is no that much of data :)
        HashMap<String, RequestLogModel> occurenceMap = new HashMap<>();

        Path path = Paths.get(stringPath);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String[] logLineData;
            String line;
            while ((line = reader.readLine()) != null) {// iterate trough the file line by line
                logLineData = line.split("\\|");
                LocalDateTime logDate = LocalDateTime.parse(logLineData[0], logDateTimeFormatter);
                //get only data that is in the given period startdatetime inclusive
                if (logDate.isEqual(startDateTime) || (logDate.isBefore(endDateTime) && logDate.isAfter(startDateTime))) {
                    String requestDate = logLineData[0];
                    LocalDateTime exactRequestDate = LocalDateTime.parse(requestDate, logDateTimeFormatter);
                    String ipAddress = logLineData[1];
                    String request = logLineData[2];
                    // log format is strict format and we can relay on having quotes on tokens
                    request = request.substring(1, request.length() - 1); // trim quotes
                    Integer responsеCode = Integer.valueOf(logLineData[3]);
                    String userAgent = logLineData[4];
                    userAgent = userAgent.substring(1, userAgent.length() - 1);
                    if (occurenceMap.get(ipAddress) == null) {
                        Request logRequest = new Request(request, responsеCode, userAgent, exactRequestDate);
                        RequestLogModel rlModel = new RequestLogModel(ipAddress, startDateTime, endDateTime, logRequest);
                        occurenceMap.put(ipAddress, rlModel);
                    } else {
                        RequestLogModel rlModel = occurenceMap.get(ipAddress);
                        Request logRequest = new Request(request, responsеCode, userAgent, exactRequestDate);
                        rlModel.addRequest(logRequest);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error while parsing the file");
            throw new RuntimeException("Error while parsing the file", e);
        }
        return occurenceMap;
    }
}