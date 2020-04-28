package com.jxyzh11.springbootdemo.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: QuartzJob1
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/17 17:29
 * @Version: 1.0
 */
@Slf4j
public class QuartzJob1 extends QuartzJobBean {

    protected void executeInternal(JobExecutionContext var1) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //log.debug("QuartzJob1----{}", sdf.format(new Date()));
    }
}
