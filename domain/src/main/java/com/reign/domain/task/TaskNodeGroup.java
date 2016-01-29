package com.reign.domain.task;

import java.util.Date;
import java.util.List;

/**
 * one group can contains more than one taskNode
 * one TaskNode can be in more than one group
 * <p/>
 * Created by ji on 15-9-28.
 */
public class TaskNodeGroup {

    //primary key
    private Long id;

    //name
    private String groupName;

    //max number than can be run on this node concurrency
    private Integer taskLimit;

    private String description;

    //taskNode list contains in this gorup
    private List<TaskNode> taskNodeList;

    //create time
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getTaskLimit() {
        return taskLimit;
    }

    public void setTaskLimit(Integer taskLimit) {
        this.taskLimit = taskLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TaskNode> getTaskNodeList() {
        return taskNodeList;
    }

    public void setTaskNodeList(List<TaskNode> taskNodeList) {
        this.taskNodeList = taskNodeList;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
