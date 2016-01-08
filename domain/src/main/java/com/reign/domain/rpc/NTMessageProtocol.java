package com.reign.domain.rpc;

/**
 * Created by ji on 15-11-11.
 */
public class NTMessageProtocol {
    private Integer type;
    private Object data;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
