package com.reign.client.thread.template;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by ji on 15-9-25.
 * 模板类,定义线程执行逻辑,可控制是否执行
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
