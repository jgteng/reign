package com.reign.server.thread.template;

import com.reign.component.utils.SchedulUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by ji on 15-9-28.
 * Support complex cron expression scheduler task
 */
public class CronScheduleThread {

    private String name;

    private String cron;

    private Class<? extends ThreadTemplate> executeClass;

    public CronScheduleThread(String name, String cron, Class<? extends ThreadTemplate> executeClass) {
        this.name = name;
        this.cron = cron;
        this.executeClass = executeClass;
    }

    public void start() throws Exception {
        Trigger trigger = newTrigger()
                .withIdentity(name + "_Trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        JobDetail job = newJob(executeClass)
                .withIdentity(name, null)
                .build();

        SchedulUtil.scheduleJob(job, trigger);
    }
}
