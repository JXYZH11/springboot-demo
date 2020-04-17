package com.jxyzh11.springbootdemo.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: QuartzConfig
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/17 17:32
 * @Version: 1.0
 */
@Slf4j
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(QuartzJob1.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger1() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(1)//每一秒执行一次
                .repeatForever();//永久重复，一直执行下去
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail jobDetail2() {
        QuartzJobBean quartzJob2 = new QuartzJobBean() {
            @Override
            protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                log.debug("QuartzJob2----{}", sdf.format(new Date()));
            }
        };
        return JobBuilder.newJob(quartzJob2.getClass()).storeDurably().build();
    }

    @Bean
    public Trigger trigger2() {
        //JobDetail的bean注入不能省略
        //JobDetail jobDetail3 = JobBuilder.newJob(QuartzJob2.class).storeDurably().build();
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2) //每2秒执行一次
                .repeatForever(); //永久重复，一直执行下去
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail2())
                .withSchedule(scheduleBuilder).build();
    }
}
