package com.reign.domain.script;

import java.util.Date;

/**
 * Created by ji on 16-1-21.
 */
public class ScriptOldVersion {

    private Long id;

    private Long scptId;

    private String scptName;

    private String scptType;

    private String scptPath;

    private Integer scptVersion;

    private String description;
    
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScptId() {
        return scptId;
    }

    public void setScptId(Long scptId) {
        this.scptId = scptId;
    }

    public String getScptName() {
        return scptName;
    }

    public void setScptName(String scptName) {
        this.scptName = scptName;
    }

    public String getScptType() {
        return scptType;
    }

    public void setScptType(String scptType) {
        this.scptType = scptType;
    }

    public String getScptPath() {
        return scptPath;
    }

    public void setScptPath(String scptPath) {
        this.scptPath = scptPath;
    }

    public Integer getScptVersion() {
        return scptVersion;
    }

    public void setScptVersion(Integer scptVersion) {
        this.scptVersion = scptVersion;
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
}
