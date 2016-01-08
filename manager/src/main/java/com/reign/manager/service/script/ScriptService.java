package com.reign.manager.service.script;

import java.io.File;

/**
 * Created by ji on 15-12-10.
 */
public interface ScriptService {
    public byte[] getFile(String... args) throws Exception;

    public boolean saveFile(File paramFile) throws Exception;
}
