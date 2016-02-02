package com.reign.server.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ji on 16-2-2.
 */
public class DaoFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoFactory.class);

    private static final ConcurrentHashMap<String, BaseDao> _DAO_MAP = new ConcurrentHashMap<String, BaseDao>();

    private DaoFactory() {
    }

    public static BaseDao getDao(Class<? extends BaseDao> clazz) {
        BaseDao baseDao = null;
        String clazzName = clazz.getSimpleName();
        if (_DAO_MAP.containsKey(clazzName)) {
            return _DAO_MAP.get(clazzName);
        } else {
            synchronized (_DAO_MAP) {
                if (!_DAO_MAP.containsKey(clazzName)) {
                    try {
                        baseDao = clazz.newInstance();
                        _DAO_MAP.put(clazzName, baseDao);
                    } catch (Exception e) {
                        LOGGER.error("DaoFactory get dao instance error,{}", clazz.getName(), e);
                    }
                }
            }
        }
        return baseDao;
    }
}
