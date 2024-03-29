package org.zerock.moamoa.config;

import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.zerock.moamoa.common.scheduler.*;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    /**
     * 이메일 제거
     */
    @Bean
    public JobDetailFactoryBean expiredEmailJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ExpiredEmailScheduler.class);
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean expiredEmailTrigger(JobDetail expiredEmailJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(expiredEmailJobDetail);
        factory.setCronExpression("0 0 12 * * ?"); // 매일 정각 12시에 실행
        return factory;
    }

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

    @Bean
    public CronTriggerFactoryBean searchKeywordTrigger(JobDetail searchTotalizationJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(searchTotalizationJobDetail);
        factory.setCronExpression("0 0 12 * * ?"); // 매일 정각 12시에 실행
        return factory;
    }

    /**
     * 유효 기한 만료 이메일 요청 삭제
     */
    @Bean
    public JobDetailFactoryBean expiredEmailRequestDeleteJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ExpiredEmailScheduler.class);
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean expiredEmailRequestDeleteTrigger(JobDetail expiredEmailRequestDeleteJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(expiredEmailRequestDeleteJobDetail);
        factory.setCronExpression("0 0 12 * * ?"); // 매일 정각 12시에 실행
        return factory;
    }

    /**
     * 거래 진행 기간 만료 상품 상태 변경
     */
    @Bean
    public JobDetailFactoryBean productStatusChangeJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(ProductStatusChangeScheduler.class);
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public CronTriggerFactoryBean productStatusChangeTrigger(JobDetail productStatusChangeJobDetail) {
        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(productStatusChangeJobDetail);
        factory.setCronExpression("0 0 12 * * ?"); // 매일 정각 12시에 실행
        return factory;
    }

    /**
     * 조회수 집계
     */
    @Bean
    public JobDetailFactoryBean viewCountJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ProductViewsScheduler.class);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean viewCountTrigger(JobDetail viewCountJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(viewCountJobDetail);
        factoryBean.setCronExpression("0 0/3 * * * ?"); // 매 3분마다 실행
        return factoryBean;
    }
}
