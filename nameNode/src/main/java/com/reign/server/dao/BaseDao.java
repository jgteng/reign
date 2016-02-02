package com.reign.server.dao;

import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by ji on 16-2-2.
 */
public class BaseDao {

    <E> List<E> selectList(String statement) {
        SqlSession sqlSession = SqlMapperManager.getSqlSession();
        try {
            return sqlSession.selectList(statement);
        } finally {
            sqlSession.close();
        }
    }

    int update(String statement, Object parameter) {
        SqlSession sqlSession = SqlMapperManager.getSqlSession();
        try {
            return sqlSession.update(statement, parameter);
        } finally {
            sqlSession.close();
        }
    }

    <T> T selectOne(String statement, Object parameter) {
        SqlSession sqlSession = SqlMapperManager.getSqlSession();
        try {
            return sqlSession.selectOne(statement, parameter);
        } finally {
            sqlSession.close();
        }
    }

    int insert(String statement, Object parameter) {
        SqlSession sqlSession = SqlMapperManager.getSqlSession();
        try {
            return sqlSession.insert(statement, parameter);
        } finally {
            sqlSession.close();
        }
    }
}
