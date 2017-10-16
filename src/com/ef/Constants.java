package com.ef;

import java.time.format.DateTimeFormatter;

/**
 * Written with love
 *
 * @author Sasa Prsic 13/10/2017
 */
public class Constants {
    public final static DateTimeFormatter inputDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
    public final static DateTimeFormatter logDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public final static String DB_PASSWORD = "piramida";
    public final static String DB_USER = "root";
    public final static int BATCH_SIZE = 1000;
    public final static String LOG_FILE_PATH = "access.log";

    private Constants() {
    }
}
