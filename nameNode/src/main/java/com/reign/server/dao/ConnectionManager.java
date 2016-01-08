package com.reign.server.dao;


import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by ji on 15-10-8.
 * 数据源管理
 */
public class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private static DataSource dataSource;

    public static void init() throws Exception{
        if (dataSource == null) {
            synchronized (LOGGER) {
                if (dataSource == null) {
                    InputStream in = ConnectionManager.class.getClassLoader().getResourceAsStream("ds.properties");
                    Properties properties = new Properties();
                    properties.load(in);
                    dataSource = DruidDataSourceFactory.createDataSource(properties);
                }
            }
        }
    }

    public static Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
}
