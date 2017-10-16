package com.ef.parser;

import com.ef.model.RequestLogModel;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Written with love
 *
 * @author Sasa Prsic 14/10/2017
 */
public class LogParserTest {
    private LogParser parser = new LogParser();
    private final static DateTimeFormatter inputDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    @Test()
    public void testParser2Inputs() throws UnsupportedEncodingException {

        LocalDateTime startDateTime = LocalDateTime.parse("2017-01-01.00:01:00", inputDateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse("2017-01-01.00:02:00", inputDateTimeFormatter);

        HashMap<String, RequestLogModel> occurrence = parser.parse("access.log", startDateTime, endDateTime);

        assertEquals(2, occurrence.values().size());

        RequestLogModel log1 = occurrence.get("192.168.170.128");
        assertNotNull(log1);
        assertEquals(1, log1.getNoOfRequest());
        assertEquals("GET / HTTP/1.1", log1.getListOfRequests().get(0).getRequest());
        assertEquals("192.168.170.128", log1.getIp());
        assertEquals(200, (int) log1.getListOfRequests().get(0).getStatus());

        RequestLogModel log2 = occurrence.get("192.168.169.194");
        assertNotNull(log2);
        assertEquals(1, log2.getNoOfRequest());
        assertEquals("GET / HTTP/1.1", log2.getListOfRequests().get(0).getRequest());
        assertEquals("192.168.169.194", log2.getIp());
        assertEquals(200, (int) log2.getListOfRequests().get(0).getStatus());

    }

    @Test()
    public void testParserLowerBound() throws UnsupportedEncodingException {
        LocalDateTime startDateTime = LocalDateTime.parse("2017-01-01.15:01:19", inputDateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse("2017-01-01.16:01:19", inputDateTimeFormatter);

        HashMap<String, RequestLogModel> occurrence = parser.parse("access.log", startDateTime, endDateTime);

        assertEquals(1, occurrence.values().size());

        assertNotNull(occurrence.get("192.168.169.200"));
        RequestLogModel log = occurrence.get("192.168.169.200");
        assertEquals(1, log.getNoOfRequest());
        assertEquals("GET / HTTP/1.1", log.getListOfRequests().get(0).getRequest());
        assertEquals("192.168.169.200", log.getIp());
        assertEquals(200, (int) log.getListOfRequests().get(0).getStatus());
    }

    @Test()
    public void testParserUpperBound() throws UnsupportedEncodingException {
        LocalDateTime startDateTime = LocalDateTime.parse("2017-01-01.12:01:20", inputDateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse("2017-01-01.13:01:20", inputDateTimeFormatter);

        HashMap<String, RequestLogModel> occurrence = parser.parse("access.log", startDateTime, endDateTime);

        assertEquals(1, occurrence.values().size());

        assertNotNull(occurrence.get("192.168.169.123"));
        RequestLogModel log = occurrence.get("192.168.169.123");
        assertEquals(1, log.getNoOfRequest());
        assertEquals("GET / HTTP/1.1", log.getListOfRequests().get(0).getRequest());
        assertEquals("192.168.169.123", log.getIp());
        assertEquals(200, (int) log.getListOfRequests().get(0).getStatus());
    }


    @Test()
    public void testParserDailyBound() throws UnsupportedEncodingException {
        LocalDateTime startDateTime = LocalDateTime.parse("2017-01-01.00:00:00", inputDateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse("2017-01-01.23:59:59", inputDateTimeFormatter);

        HashMap<String, RequestLogModel> occurrence = parser.parse("access.log", startDateTime, endDateTime);

        assertEquals(4, occurrence.values().size());

        RequestLogModel log = occurrence.get("192.168.170.128");
        assertNotNull(log);
        assertEquals(2, log.getNoOfRequest());
        assertEquals("GET / HTTP/1.1", log.getListOfRequests().get(0).getRequest());
        assertEquals("192.168.170.128", log.getIp());
        assertEquals(200, (int) log.getListOfRequests().get(0).getStatus());

        assertEquals("POST / HTTP/1.1", log.getListOfRequests().get(1).getRequest());
        assertEquals("192.168.170.128", log.getIp());
        assertEquals(200, (int) log.getListOfRequests().get(1).getStatus());


        RequestLogModel log1 = occurrence.get("192.168.169.200");
        assertNotNull(log1);
        assertEquals(1, log1.getNoOfRequest());
        assertEquals("GET / HTTP/1.1", log1.getListOfRequests().get(0).getRequest());
        assertEquals("192.168.169.200", log1.getIp());
        assertEquals(200, (int) log1.getListOfRequests().get(0).getStatus());

        assertNull(occurrence.get("192.168.169.202"));
        assertNull(occurrence.get("192.168.169.203"));
    }
}
