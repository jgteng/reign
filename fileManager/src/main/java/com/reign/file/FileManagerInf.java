package com.reign.file;

import java.io.File;

/**
 * Created by ji on 15-12-10.
 */
public interface FileManagerInf {

    public byte[] getFile(String... args) throws Exception;

    public boolean saveFile(File paramFile) throws Exception;
}
