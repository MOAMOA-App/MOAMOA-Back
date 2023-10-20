package org.zerock.moamoa.common.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.utils.redis.ViewsRedisUtils;

@Component
public class DeleteViewsUserInfoScheduler implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ViewsRedisUtils.deleteAllUserInfo();
    }
}
