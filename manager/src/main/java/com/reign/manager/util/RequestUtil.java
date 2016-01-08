package com.reign.manager.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.reign.manager.exception.ReignParamException;
import com.reign.manager.web.ResponseCodeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ji on 15-12-12.
 */
public class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static Object toClassBean(String content, Class clazz) throws ReignParamException {
        try {
            JSONObject paramObj = JSON.parseObject(content);
            return JSON.toJavaObject(paramObj, clazz);
        } catch (Exception e) {
            LOGGER.error("参数转换异常", e);
            throw new ReignParamException(ResponseCodeConstants.PARAM_ERROR_CODE, "参数转换异常");
        }
    }
}
