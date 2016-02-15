package com.reign.component.constants;

/**
 * Created by ji on 15-9-29.
 */
public class MessageTypeConstant {
    public static final int LOGIN_AUTH_REQ_TYPE = 101;
    public static final int LOGIN_AUTH_RESPONSE_TYPE = 102;
    public static final int LOGIN_AUTH_SUCCESS = 1;
    public static final int LOGIN_AUTH_FAIL = 0;

    public static final int HEART_BEAT_TYPE = 201;

    /*******Task相关以3开头*******/
    public static final int TASK_PULL_TYPE = 301;  //向NameNode拉取任务
    public static final int TASK_STATUS_TYPE = 302; //返回任务执行状态
    public static final int SEND_RUNNING_TASKS_TYPE = 303; //向NameNode汇报正在执行任务列表
    public static final int GET_TASK_STATUS_TYPE = 304;//NameNode主动向TaskNode查询任务执行结果


    /******* 返回给TaskNode的消息都是4位 *************/
    public static final int TASK_PULL_RESULT_TYPE=3101; //NameNode向TaskNode发送待执行任务列表

}
