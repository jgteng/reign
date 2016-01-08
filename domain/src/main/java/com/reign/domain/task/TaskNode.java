package com.reign.domain.task;

import java.util.Date;

/**
 * Created by ji on 15-9-28.
 * 任务运行节点(机器)
 */
public class TaskNode {

    //主键
    private Long id;

    //节点名称
    private String nodeName;

    //ip地址
    private String ip;

    //创建时间
    private Date created;

    //描述
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
