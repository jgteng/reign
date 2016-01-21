package com.reign.manager.service.script;

import com.reign.domain.script.Script;

import java.io.File;
import java.util.List;

/**
 * Created by ji on 15-12-10.
 */
public interface ScriptService {
    public byte[] getFile(String... args) throws Exception;

    public boolean saveFile(File paramFile) throws Exception;

    List<Script> listScript(Script script) throws Exception;
}
