package com.ef.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a request log that parser has detect with a given parameters
 *
 * @author Sasa Prsic 13/10/2017
 */
public class RequestLogModel {
    private String ip;
    private List<Request> listOfRequests = new ArrayList<>();
    private int noOfRequest;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public RequestLogModel(String ip, LocalDateTime startDate, LocalDateTime endDate, Request request) {
        this.ip = ip;
        this.noOfRequest = 1; //initial number of request first time find in the log
        this.startDate = startDate;
        this.endDate = endDate;
        this.listOfRequests.add(request);
    }

    public String getIp() {
        return ip;
    }

    public List<Request> getListOfRequests() {
        return listOfRequests;
    }

    public int getNoOfRequest() {
        return noOfRequest;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void incrementNumberOfRequest() {
        this.noOfRequest++;
    }

    public void addRequest(Request request) {
        this.listOfRequests.add(request);
        incrementNumberOfRequest();
    }
}
