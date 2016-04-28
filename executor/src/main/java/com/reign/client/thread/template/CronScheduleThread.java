package com.reign.client.thread.template;

import com.reign.component.utils.SchedulUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by ji on 15-9-28.
 * 支持复杂cron表达式的Scheduler任务
 */
public class CronScheduleThread  {

    private String name;

    private String cron;

    public CronScheduleThread(String name, String cron) {
        this.name = name;
        this.cron = cron;
    }

    public void start() throws Exception {
        Trigger trigger = newTrigger()
                .withIdentity(name + "_Trigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        JobDetail job = newJob(ThreadTemplate.class)
                .withIdentity(name, null)
                .build();

        SchedulUtil.scheduleJob(job, trigger);
    }
}
