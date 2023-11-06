package org.zerock.moamoa.common.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.utils.redis.ViewsRedisUtils;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductViewsScheduler implements Job {
    // Job은 스케줄링 대상 작업을 정의한 인터페이스, 인터페이스에 정의되어 있는 execute() 메소드를 통해 실행할 작업 구현
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Map<Long, Integer> viewCounts = ViewsRedisUtils.getAllViewCounts();

        viewCounts.forEach((key, value) -> {
            int count = value;
            if (count != 0) {
                Product product = productRepository.findByIdOrThrow(key);
                product.updateViewCount(count);

//                ViewsRedisUtils.deleteViewCount(product.getId());
            }
        });
    }
}
