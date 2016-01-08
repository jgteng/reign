package com.reign.domain.task;

import com.reign.aop.annotation.FieldCheck;
import com.reign.domain.CommonBean;

import java.util.Date;

/**
 * Created by ji on 15-9-28.
 * 任务实体
 */
public class Task extends CommonBean {

    //任务Id
    private Long id;

    //任务名称
    @FieldCheck(NotEmpty = true)
    private String taskName;

    //任务运行入口
    @FieldCheck(NotEmpty = true)
    private String mainScript;

    //任务运行规则
    @FieldCheck(NotEmpty = true)
    private String taskRule;

    //任务运行节点id
    private Long nodeId;

    //任务运行节点类型
    private String nodeType;

    //任务实际运行节点id
    private Long runNodeId;

    //任务描述
    private String description;

    //创建时间
    private Date created;

    //修改时间
    private Date modified;

    //上次运行时间
    private Date lastRunTime;

    //下次运行时间
    private Date nextTime;

    //任务状态
    private Integer status;

    //是否禁用.1:已禁用
    private Integer disabled;

    /*************************
     *       For display
     ************************/
    private String nodeName;

    private String runNodeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getMainScript() {
        return mainScript;
    }

    public void setMainScript(String mainScript) {
        this.mainScript = mainScript;
    }

    public String getTaskRule() {
        return taskRule;
    }

    public void setTaskRule(String taskRule) {
        this.taskRule = taskRule;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Long getRunNodeId() {
        return runNodeId;
    }

    public void setRunNodeId(Long runNodeId) {
        this.runNodeId = runNodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getRunNodeName() {
        return runNodeName;
    }

    public void setRunNodeName(String runNodeName) {
        this.runNodeName = runNodeName;
    }
}
