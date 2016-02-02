package com.reign.domain.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by ji on 15-11-11.
 */
public class NTMessageProtocol {
    private Integer type;
    private JSONObject data;
    private JSONArray arrayData;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONArray getArrayData() {
        return arrayData;
    }

    public void setArrayData(JSONArray arrayData) {
        this.arrayData = arrayData;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
