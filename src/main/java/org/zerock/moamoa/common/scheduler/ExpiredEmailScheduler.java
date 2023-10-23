package org.zerock.moamoa.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.Email;
import org.zerock.moamoa.repository.EmailRepository;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExpiredEmailScheduler implements Job {
    private final EmailRepository emailRepository;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Instant ExpiredDate = Instant.now().minusSeconds(180);
        List<Email> emails = emailRepository.findAllByUpdatedAtBefore(ExpiredDate);
        log.info("count of expired email request : " + emails.size());
        emailRepository.deleteAll(emails);
        log.info("delete expired email request");
    }
}
