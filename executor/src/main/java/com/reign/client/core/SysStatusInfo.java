package com.reign.client.core;

/**
 * Created by ji on 16-5-18.
 */
public class SysStatusInfo {

    private static final SysStatusInfo _INSTANCE = new SysStatusInfo();

    //是否和Dispatcher认证成功
    private static boolean authSuccess = false;

    private SysStatusInfo() {

    }

    public static SysStatusInfo getInstance() {
        return _INSTANCE;
    }


    public static boolean isAuthSuccess() {
        return authSuccess;
    }

    public static void setAuthSuccess(boolean authSuccess) {
        SysStatusInfo.authSuccess = authSuccess;
    }
}
