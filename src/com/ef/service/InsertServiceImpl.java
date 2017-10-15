package com.ef.service;

import com.ef.model.Request;
import com.ef.model.RequestLogModel;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.ef.Constants.BATCH_SIZE;
import static com.ef.Constants.DB_PASSWORD;
import static com.ef.Constants.DB_USER;

/**
 * Written with love
 *
 * @author Sasa Prsic 14/10/2017
 */
public class InsertServiceImpl implements IInsertService {
    private final static String BAN_COMMENT = "Ip blocked because it is suspicious that made a lot of request in short amount of time";

    private static String INSERT_LOG_STATEMENT = "INSERT INTO request_log " +
            "(ip, no_of_requests, request_start_date, request_end_date, ban_comment, created_timestamp)" +
            "VALUES (?,?,?,?,?,?)";

    private static String INSERT_REQUESTS_STATEMENT = "INSERT INTO request " +
            "(request_log_id, request , status, user_agent, request_date, created_timestamp)" +
            "VALUES (?,?,?,?,?,?)";

    @Override
    public void insert(List<RequestLogModel> records) throws SQLException {
        Connection connection = null;
        PreparedStatement insertRequestLogStatement = null;
        PreparedStatement insertRequestStatement = null;
        int countRequestLogBatch = 0;
        int countOfLogInserts = records.size();
        int insertedRequestLog = 0;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LogStash", DB_USER, DB_PASSWORD);
            connection.setAutoCommit(false);
            insertRequestLogStatement = connection.prepareStatement(INSERT_LOG_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            insertRequestStatement = connection.prepareStatement(INSERT_REQUESTS_STATEMENT);
            for (RequestLogModel rm : records) {
                insertRequestLogStatement.setString(1, rm.getIp());
                insertRequestLogStatement.setInt(2, rm.getNoOfRequest());
                insertRequestLogStatement.setTimestamp(3, Timestamp.valueOf(rm.getStartDate()));
                insertRequestLogStatement.setTimestamp(4, Timestamp.valueOf(rm.getEndDate()));
                insertRequestLogStatement.setString(5, BAN_COMMENT);
                insertRequestLogStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                insertedRequestLog += 1;
                insertRequestLogStatement.addBatch();
                countRequestLogBatch++;
                if (countOfLogInserts - insertedRequestLog < BATCH_SIZE) {
                    insertRequestLogStatement.executeBatch();
                    ResultSet resultSet = insertRequestLogStatement.getGeneratedKeys();
                    insertRequest(connection, rm.getListOfRequests(), resultSet, insertRequestStatement);
                } else if (countRequestLogBatch % BATCH_SIZE == 0) {
                    insertRequestLogStatement.executeBatch();
                    ResultSet resultSet = insertRequestLogStatement.getGeneratedKeys();
                    insertRequest(connection, rm.getListOfRequests(), resultSet, insertRequestStatement);
                }
                countRequestLogBatch++;
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error while inserting in db", e);
        } finally {
            connectionCleanup(connection, insertRequestLogStatement, insertRequestStatement);
        }
    }

    private void connectionCleanup(Connection connection, PreparedStatement insertRequestLogStatement, PreparedStatement insertRequestStatement) throws SQLException {
        if (insertRequestLogStatement != null) {
            insertRequestLogStatement.clearBatch();
            insertRequestLogStatement.close();
        }
        if (insertRequestStatement != null) {
            insertRequestStatement.clearBatch();
            insertRequestStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    private void insertRequest(Connection connection, List<Request> requestsList, ResultSet resultSet, PreparedStatement insertRequestStatement) throws SQLException {
        while (resultSet.next()) { // the generated keys of the batch
            insertRequestStatement = connection.prepareStatement(INSERT_REQUESTS_STATEMENT);
            int insertedRequest = 0;
            for (Request r : requestsList) {
                insertRequestStatement.setInt(1, resultSet.getInt(1));//foreign key
                insertRequestStatement.setString(2, r.getRequest());
                insertRequestStatement.setInt(3, r.getStatus());
                insertRequestStatement.setString(4, r.getUserAgent());
                insertRequestStatement.setTimestamp(5, Timestamp.valueOf(r.getRequestDateTime()));
                insertRequestStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                insertRequestStatement.addBatch();
                if (insertedRequest % BATCH_SIZE == 0) {
                    insertRequestStatement.executeBatch();
                }
                insertedRequest++;
            }
            insertRequestStatement.executeBatch();
        }
    }
}
