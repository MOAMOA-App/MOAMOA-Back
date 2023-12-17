package org.zerock.moamoa.utils.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ViewsRedisUtils {
    private static RedisTemplate<String, String> viewsRedisTemplate;   // 컨피그에잇는 레디스 빈 가져와서 쓰는거
    private static final String key = "viewCounts";

    @Autowired
    private ViewsRedisUtils(@Qualifier(value = "viewsRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        ViewsRedisUtils.viewsRedisTemplate = redisTemplate;
    }

    /** 조회수 설정 */
    public static void addViewCount(Long pid, String useragent){
        List<String> userViewList = viewsRedisTemplate.opsForList().range(pid.toString(), 0, -1);

        // 유저정보 테이블을 pid로 조회, userkeyList가 null이거나 pid 리스트에 useragent 없으면 조회수 올림
        if (userViewList == null || !userViewList.contains(useragent)) {
            // 없으면 조회수 +1, 유저정보 추가
            viewsRedisTemplate.opsForZSet().incrementScore(key, pid.toString(), 1);
            viewsRedisTemplate.opsForList().rightPushAll(pid.toString(), useragent);
        }

        // 조회수 리스트는 3분마다 삭제
        viewsRedisTemplate.expire(key, 3, TimeUnit.MINUTES);

        // pid 기준 유저 조회 리스트는 하루 지날 시 삭제
        viewsRedisTemplate.expire(pid.toString(), 1, TimeUnit.DAYS);
    }

    /** viewcount 데이터 가져옴 */
    public static Map<Long, Integer> getAllViewCounts() {
        String key = "viewCounts";
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = viewsRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1);

        Map<Long, Integer> viewCounts = new HashMap<>();
        assert rangeWithScores != null;
        rangeWithScores.forEach(tuple -> {
            Long pid = Long.parseLong(Objects.requireNonNull(tuple.getValue()));
            Double val = tuple.getScore();
            assert val != null;
            int count = Math.toIntExact(Math.round(val));
            viewCounts.put(pid, Math.toIntExact(count));
        });

        return viewCounts;
    }
}
