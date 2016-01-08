package com.reign.manager.service.script;

import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by ji on 15-12-10.
 */
@Service
public class ScriptServiceImpl implements ScriptService{

    public byte[] getFile(String... args) throws Exception {
        return new byte[0];
    }

    public boolean saveFile(File paramFile) throws Exception {
        return false;
    }
}
