package com.reign.client.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            LOGGER.error("Create sqlite connectoin error", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public void insertTaskStatus(Long logId) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO TASK_RUN_STATUS (log_id,result)VALUES (?,'3')");
        preparedStatement.setString(1, logId.toString());
        preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        try {
            Connection connection = SqliteDaoFactory.getInstance().getConnection();
            System.out.println(connection);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TASK_RUN_STATUS (log_id,result)VALUES ('12','2')");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
