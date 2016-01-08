package com.reign.server.thread.template;

import com.reign.component.utils.SchedulUtil;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by ji on 15-9-28.
 * 简单的Schedule线程,定时时间间隔执行,只需要名称和时间间隔参数即可执行
 */
public class SimpleScheduleThread {
    private String name;

    private int interval;

    private Class<? extends ThreadTemplate> executeClass;

    public SimpleScheduleThread(String name, int interval, Class<? extends ThreadTemplate> executeClass) {
        this.name = name;
        this.interval = interval;
        this.executeClass = executeClass;
    }

    public void start() throws Exception {
        Trigger trigger = newTrigger()
                .withIdentity(name + "_Trigger")
                .startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(interval).repeatForever())
                .build();

        JobDetail job = newJob(executeClass)
                .withIdentity(name, null)
                .build();

        SchedulUtil.scheduleJob(job, trigger);
    }
}
