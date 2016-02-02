package com.reign.server.dao;


import com.reign.domain.task.Task;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by ji on 15-10-10.
 */
public class TestDao {
    public static void main(String[] args) {
        SqlSession sqlSession = SqlMapperManager.getSqlSession();
        try {

            //初始化数据源
            ConnectionManager.init();

            Task task = new Task();
            task.setTaskName("a");
            task.setMainScript("mainScript");
            task.setTaskRule("rule");
            task.setStatus(0);
            sqlSession.insert("addTask", task);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

    }
}
