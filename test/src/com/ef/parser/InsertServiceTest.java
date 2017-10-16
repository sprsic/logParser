package com.ef.parser;

import com.ef.model.Request;
import com.ef.model.RequestLogModel;
import com.ef.service.IInsertService;
import com.ef.service.LogServiceFactory;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Written with love
 *
 * @author Sasa Prsic 16/10/2017
 */
public class InsertServiceTest {
    private IInsertService insertService = LogServiceFactory.getLogService();


    @Test
    public void testSimpleInsert() {
        List<RequestLogModel> insertList = new ArrayList<>(1);
        LocalDateTime date = LocalDateTime.now();
        Request request = new Request("GET", 200, "Chrome", date);
        RequestLogModel rl = new RequestLogModel("127.0.0.1", date, date, request);
        insertList.add(rl);
        try {
            List<RequestLogModel> inserted = insertService.insert(insertList);
            assertEquals(1, inserted.size());
            assertEquals("127.0.0.1", inserted.get(0).getIp());
            assertEquals(1, inserted.get(0).getListOfRequests().size());
            assertEquals(200, (int) inserted.get(0).getListOfRequests().get(0).getStatus());
            assertEquals("Chrome", inserted.get(0).getListOfRequests().get(0).getUserAgent());
            assertEquals(date, inserted.get(0).getListOfRequests().get(0).getRequestDateTime());
            assertEquals("GET", inserted.get(0).getListOfRequests().get(0).getRequest());
        } catch (SQLException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInsertNullValues() {
        List<RequestLogModel> insertList = new ArrayList<>(1);
        LocalDateTime date = LocalDateTime.now();
        Request request = new Request(null, 200, "Chrome", date);
        RequestLogModel rl = new RequestLogModel("127.0.0.1", date, date, request);
        insertList.add(rl);
        try {
            List<RequestLogModel> inserted = insertService.insert(insertList);
        } catch (Exception e) {
            System.out.println("Exception thrown, that is good :)");
            return;
        }
        fail("Exception should be thrown");
    }
}