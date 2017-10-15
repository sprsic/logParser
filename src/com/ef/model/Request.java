package com.ef.model;

import java.time.LocalDateTime;

/**
 * Class that represents a single request
 *
 * @author Sasa Prsic 15/10/2017
 */
public class Request {
    private String request;
    private Integer status;
    private String userAgent;
    private LocalDateTime requestDateTime;

    public Request(String request, Integer status, String userAgent, LocalDateTime requestDateTime) {
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
        this.requestDateTime = requestDateTime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(LocalDateTime requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
}
