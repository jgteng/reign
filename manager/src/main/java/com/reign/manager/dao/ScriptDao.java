package com.reign.manager.dao;

import com.reign.domain.script.Script;

import java.util.List;

/**
 * Created by ji on 16-1-21.
 */
public interface ScriptDao {

    List<Script> listScript(Script script);

}
