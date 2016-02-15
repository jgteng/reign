package com.reign.client.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.reign.client.domain.TaskRunStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.util.Properties;

/**
 * Created by ji on 16-2-1.
 */
public class SqliteDaoFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqliteDaoFactory.class);

    private static Object lock = new Object();
    private DataSource dataSource;
    private static volatile SqliteDaoFactory sqliteFactory;

    private SqliteDaoFactory() {
    }

    public static SqliteDaoFactory getInstance() {
        if (sqliteFactory == null) {
            synchronized (lock) {
                if (sqliteFactory == null) {
                    sqliteFactory = new SqliteDaoFactory();
                    sqliteFactory.createConnection();
                }
            }
        }
        return sqliteFactory;
    }

    private void createConnection() {
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "org.sqlite.JDBC");
        properties.setProperty("url", "jdbc:sqlite:/media/ji/document/Workspace/openSource/reign/taskNode/src/main/plugin/sqlite/task_status.db");
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            LOGGER.error("Create sqlLite connection error", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public void insertTaskStatus(Long logId) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO TASK_RUN_STATUS (log_id,result,status_time)VALUES (?,'3',datetime(CURRENT_TIMESTAMP,'localtime'))");
        preparedStatement.setString(1, logId.toString());
        preparedStatement.executeUpdate();
    }

    public void updateProcessId(String logId, int pid) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE TASK_RUN_STATUS SET process_id=? where log_id=?");
        preparedStatement.setString(1, "" + pid);
        preparedStatement.setString(2, logId);
        preparedStatement.executeUpdate();
    }


    public void updateStatus(String logId, int status) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE TASK_RUN_STATUS SET result=?,status_time=datetime(CURRENT_TIMESTAMP,'localtime') where log_id=?");
        preparedStatement.setString(1, status + "");
        preparedStatement.setString(2, logId);
        preparedStatement.executeUpdate();
    }

    public TaskRunStatus getRecentStatusInfoByLogId(String logId) throws SQLException, ParseException {
        TaskRunStatus taskRunStatus = new TaskRunStatus();

        Statement statement = getConnection().createStatement();

        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT log_id as logId,result as result,status_time as statusTime,process_id as processId from TASK_RUN_STATUS where log_id=" + logId + " order by id desc");
            String status = resultSet.getString("result");
            String statusTimeStr = resultSet.getString("statusTime");
            taskRunStatus.setStatus(status);
            taskRunStatus.setStatusTimeStr(statusTimeStr);
            taskRunStatus.setLogId(logId);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return taskRunStatus;
    }

    public static void main(String[] args) {
        try {
//            Connection connection = SqliteDaoFactory.getInstance().getConnection();
//            System.out.println(connection);
//            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TASK_RUN_STATUS (log_id,result,status_time)VALUES ('12','2',datetime(CURRENT_TIMESTAMP,'localtime'))");
//            preparedStatement.executeUpdate();

            SqliteDaoFactory.getInstance().updateStatus("49", 0);
            TaskRunStatus taskRunStatus = SqliteDaoFactory.getInstance().getRecentStatusInfoByLogId(12 + "");
            System.out.println(taskRunStatus);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
