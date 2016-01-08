package com.reign.domain.task;

/**
 * Created by ji on 15-9-28.
 * 节点和节点组关系实体
 */
public class TaskNodeGroupRel {

    //节点id
    private Long nodeId;

    //节点组id
    private Long groupId;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
