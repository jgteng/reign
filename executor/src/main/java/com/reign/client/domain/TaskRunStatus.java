package com.reign.client.domain;

/**
 * Created by ji on 16-2-15.
 */
public class TaskRunStatus {

    private String logId;

    private String status;

    private String statusTimeStr;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTimeStr() {
        return statusTimeStr;
    }

    public void setStatusTimeStr(String statusTimeStr) {
        this.statusTimeStr = statusTimeStr;
    }
}
