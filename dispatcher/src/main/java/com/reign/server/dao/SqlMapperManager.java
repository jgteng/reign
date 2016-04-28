package com.reign.server.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 * Created by ji on 15-10-8.
 * Mybatis Mapper
 */
public class SqlMapperManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlMapperManager.class);
    private static SqlSessionFactory ssf;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("map.cfg.xml");
            ssf = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
        } catch (Exception e) {
            LOGGER.error("mybatis mapper error", e);
        }
    }

    public static SqlSession getSqlSession() {
        SqlSession sqlSession = ssf.openSession(true);
        return sqlSession;
    }
}
