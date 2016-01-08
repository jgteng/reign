package com.reign.domain.task;

import java.util.Date;
import java.util.List;

/**
 * Created by ji on 15-9-28.
 * 节点组,一个节点组可包含多个节点,每个节点也可以隶属于多个节点组
 */
public class TaskNodeGroup {

    //主键
    private Long id;

    //节点组名称
    private String groupName;

    //描述
    private String description;

    //节点组包含的节点列表
    private List<TaskNode> taskNodeList;

    //创建时间
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
