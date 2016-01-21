package com.reign.domain.script;

import com.reign.domain.CommonBean;

import java.util.Date;

/**
 * Created by ji on 16-1-21.
 */
public class Script extends CommonBean{

    private Long id;

    private String scptName;

    private String scptType;

    private String scptPath;

    private String description;

    private Integer scptVersion;

    private Date created;

    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getScptVersion() {
        return scptVersion;
    }

    public void setScptVersion(Integer scptVersion) {
        this.scptVersion = scptVersion;
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
}
