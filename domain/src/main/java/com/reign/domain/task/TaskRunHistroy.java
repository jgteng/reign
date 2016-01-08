package com.reign.domain.task;

import java.util.Date;

/**
 * Created by ji on 15-9-28.
 * 任务运行历史表
 */
public class TaskRunHistroy {

    //主键
    private Long id;

    //任务id
    private Long taskId;

    //运行批次号
    private Long runBachNum;

    //任务名称
    private String taskName;

    //任务开始运行时间
    private Date startTime;

    //任务结束时间
    private Date endTime;

    //运行状态
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getRunBachNum() {
        return runBachNum;
    }

    public void setRunBachNum(Long runBachNum) {
        this.runBachNum = runBachNum;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
