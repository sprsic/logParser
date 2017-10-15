package com.ef.service;

/**
 * Written with love
 *
 * @author Sasa Prsic 15/10/2017
 */
public class LogServiceFactory {
    private static InsertServiceImpl logService = new InsertServiceImpl();

    private LogServiceFactory() {
    }

    //factory method for getting the log service
    public static InsertServiceImpl getLogService() {
        return logService;
    }
}
