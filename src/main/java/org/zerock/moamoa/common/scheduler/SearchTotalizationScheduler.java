package org.zerock.moamoa.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.zerock.moamoa.domain.entity.SearchKeyword;
import org.zerock.moamoa.repository.SearchKeywordRepository;
import org.zerock.moamoa.utils.redis.RedisResponse;
import org.zerock.moamoa.utils.redis.SearchRedisUtils;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchTotalizationScheduler implements Job {
    private final SearchKeywordRepository searchKeywordRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("search totalization start");
        List<RedisResponse> keywords = SearchRedisUtils.readAllKeywords();
        long addCount = 0;
        for (RedisResponse response : keywords) {
            String key = response.getKeyword().toString();
            double count = response.getScore();
            Optional<SearchKeyword> opKeyword = searchKeywordRepository.findByKeyword(key);
            if (opKeyword.isPresent()) {
                opKeyword.get().plusCount(count);
            } else {
                SearchKeyword keyword = SearchKeyword.fromDto(key, count);
                searchKeywordRepository.save(keyword);
            }
            addCount++;
        }
        log.info("add " + addCount + " keywords");
    }
}
