package com.reign.domain.runtime;

import java.util.Map;

/**
 * Created by ji on 16-1-15.
 */
public class RunTimeBean {

    private String mainScript;
    private String ScriptPath;
    private Map<String, String> env;

    private Integer pid;


    public String getMainScript() {
        return mainScript;
    }

    public void setMainScript(String mainScript) {
        this.mainScript = mainScript;
    }

    public String getScriptPath() {
        return ScriptPath;
    }

    public void setScriptPath(String scriptPath) {
        ScriptPath = scriptPath;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
