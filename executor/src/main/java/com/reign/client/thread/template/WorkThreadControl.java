package com.reign.client.thread.template;

/**
 * Created by ji on 15-9-28.
 * 控制工作线程的启动和停止,获取工作线程是否需要工作的标志位
 */
public class WorkThreadControl {
    private static boolean run = true;

    public static boolean isRun() {
        return run;
    }

    public static synchronized void stopWorkThread() {
        run = false;
    }

    public static synchronized void startWorkThread() {
        run = true;
    }
}
