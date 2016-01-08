package com.reign.component.utils;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by ji on 15-9-28.
 */
public class SchedulUtil {

    private static final SchedulerFactory schedFact = new StdSchedulerFactory();

    private static Scheduler scheduler;

    public static void scheduleJob(JobDetail job, Trigger trigger) throws Exception{
        scheduler = schedFact.getScheduler();
        if (!scheduler.isStarted()) {
            scheduler.start();
        }

        scheduler.scheduleJob(job, trigger);
    }
}
