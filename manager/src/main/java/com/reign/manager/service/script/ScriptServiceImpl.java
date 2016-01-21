package com.reign.manager.service.script;

import com.reign.domain.script.Script;
import com.reign.manager.dao.ScriptDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Created by ji on 15-12-10.
 */
@Service
public class ScriptServiceImpl implements ScriptService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptServiceImpl.class);

    @Autowired
    private ScriptDao scriptDao;

    public byte[] getFile(String... args) throws Exception {
        return new byte[0];
    }

    public boolean saveFile(File paramFile) throws Exception {
        return false;
    }

    public List<Script> listScript(Script script) throws Exception {
        return scriptDao.listScript(script);
    }

}
