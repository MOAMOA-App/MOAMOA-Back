package org.zerock.moamoa.config;

import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.zerock.moamoa.common.scheduler.SearchTotalizationScheduler;

@Configuration
@EnableScheduling
public class SchedulerConfig {
//
//    @Bean
//    public JobDetailFactoryBean expiredEmailJobDetail() {
//        JobDetailFactoryBean factory = new JobDetailFactoryBean();
//        factory.setJobClass(ExpiredEmailScheduler.class);
//        factory.setDurability(true);
//        return factory;
//    }
//
//    /**
//     * 이메일 제거
//     * cron 사용
//     */
//    @Bean
//    public CronTriggerFactoryBean expiredEmailTrigger(JobDetail expiredEmailJobDetail) {
//        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
//        factory.setJobDetail(expiredEmailJobDetail);
//        factory.setCronExpression("0 0 12 * * ?"); // 매일 정각 12시에 실행
//        return factory;
//    }

    /**
     * 검색어 집계
     * 실행 시간 기준 Interval 간격
     */
    @Bean
    public JobDetailFactoryBean searchTotalizationJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(SearchTotalizationScheduler.class);
        factory.setDurability(true);
        return factory;
    }

//    @Bean
//    public SimpleTriggerFactoryBean SearchKeywordTrigger(JobDetail searchTotalizationJobDetail) {
//        SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
//        factory.setJobDetail(searchTotalizationJobDetail);
//        factory.setRepeatInterval(10 * 60 * 1000); // 10분  hour
//        factory.setStartDelay(0);
//        return factory;
//    }

    @Bean
    public CronTriggerFactoryBean SearchKeywordTrigger(JobDetail searchTotalizationJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(searchTotalizationJobDetail);
        factory.setCronExpression("0 0 12 * * ?"); // 매일 정각 12시에 실행
        return factory;
    }


}
