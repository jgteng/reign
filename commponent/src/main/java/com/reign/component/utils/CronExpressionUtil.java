package com.reign.component.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Trigger;

import java.util.Date;
import java.util.TimeZone;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by ji on 15-12-14.
 */
public class CronExpressionUtil {

    public static Date getNextTime(String cronExpression) throws Exception {
        Trigger trigger = newTrigger().withSchedule(
                CronScheduleBuilder.cronSchedule(cronExpression)
                        .inTimeZone(TimeZone.getDefault())
        ).build();
        return trigger.getFireTimeAfter(new Date());
    }


    public static void main(String[] args) throws Exception {
        String cron = "0 0 12 * * ?";
        Date result = getNextTime(cron);
        System.out.println(result);
    }
}
