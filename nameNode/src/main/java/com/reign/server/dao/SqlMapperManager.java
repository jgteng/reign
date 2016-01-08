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

    private static SqlSession sqlSession;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("map.cfg.xml");
            SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);
            sqlSession = ssf.openSession(true);
            reader.close();
        } catch (Exception e) {
            LOGGER.error("mybatis mapper异常", e);
        }
    }

    public static SqlSession getSqlSession() {
        return sqlSession;
    }
}
