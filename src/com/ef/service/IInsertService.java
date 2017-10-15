package com.ef.service;

import com.ef.model.RequestLogModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Written with love
 *
 * @author Sasa Prsic 14/10/2017
 */
interface IInsertService {

    /**
     * Inserts the list of objects in the db.
     *
     * @param records - list of all records that will be inserted
     * @throws SQLException
     */
    void insert(List<RequestLogModel> records) throws SQLException;

}
