package com.reign.manager.exception;

/**
 * Created by ji on 15-12-12.
 */
public class ReignBaseException extends Exception{
    private Integer code;

    private String message;

    public ReignBaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
