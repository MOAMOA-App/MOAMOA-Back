package org.zerock.moamoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    // noticeListener가 이벤트 리스닝하고있다가 처리
    // @EventListener는 동기적으로 처리를 진행 but 작업? 다 끝나야 알림보내는식 -> 오래걸림.
    // 순서 바꿔서 맨 앞으로 보낸다고 해도 예외 발생하는 작업 있으면 서비스레이어는 트랜잭션으로 롤백할 수 있지만 알림은 보낸 상태가 됨.
    // -> @TransactionalEventListener로 트랜잭션의 흐름에 따라 이벤트를 제어, 동기적으로 실행하는 부분은 @Async 통해 비동기적 처리
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setThreadNamePrefix("async-thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}