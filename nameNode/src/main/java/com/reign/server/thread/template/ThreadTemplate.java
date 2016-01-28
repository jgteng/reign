package com.reign.server.thread.template;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by ji on 15-9-25.
 * You can define schedule thread work by using this ThreadTemplate
 */
public abstract class ThreadTemplate implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (WorkThreadControl.isRun()) {
            this.run();
        }
    }

    /**
     * 具体逻辑由子类实现
     */
    public abstract void run();
}
