package com.reign.domain.task;

import java.util.Date;

/**
 * Created by ji on 15-9-28.
 * TaskNode Info
 */
public class TaskNode {

    //primary key
    private Long id;

    //taskNode name
    private String nodeName;

    //ip address
    private String ip;

    //max number than can be run on this node concurrency
    private Integer taskLimit;

    //create time
    private Date created;

    //description
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTaskLimit() {
        return taskLimit;
    }

    public void setTaskLimit(Integer taskLimit) {
        this.taskLimit = taskLimit;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
