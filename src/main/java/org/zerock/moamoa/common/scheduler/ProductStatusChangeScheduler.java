package org.zerock.moamoa.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.repository.ProductRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductStatusChangeScheduler implements Job {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 현재 날짜의 자정(00:00:00) 구하기
        LocalDate today = LocalDate.now();
        Instant startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Product> products = productRepository.findAllByStatusIsAndFinishedAtBefore(ProductStatus.READY, startOfDay);
        log.info("product status change start");
        for (Product product : products) {
            product.updateStatus(ProductStatus.IN_PROGRESS);
        }
        log.info("count of status changed product : " + products.size());
    }
}
