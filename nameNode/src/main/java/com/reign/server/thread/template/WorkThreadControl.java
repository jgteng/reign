package com.reign.server.thread.template;

/**
 * Created by ji on 15-9-28.
 * this Controller can control schedule thread work or not
 */
public class WorkThreadControl {
    private static volatile boolean run = true;

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
