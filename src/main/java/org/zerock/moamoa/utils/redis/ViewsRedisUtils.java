package org.zerock.moamoa.utils.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ViewsRedisUtils {
    private static RedisTemplate<String, String> viewsRedisTemplate;   // 컨피그에잇는 레디스 빈 가져와서 쓰는거
    private static final String key = "viewCounts";

    // 조회수 -> 상세페이지 검색하는데 상세 입력 받았을때 번호를 레디스에 입력 (count!! 추가해서 쌓는거)
    // 상품id가 key가 되고 value가 조회수가 되면 좋을텐데...

    // 어케하면되느냐... Redis로 조회수 Count한거 스케줄러로 일정시간마다 반영하면 되는듯 중복 방지도 함 해보자
    // 유저가 게시글 상세 보여주기(/product/{pid})에 들가면 +1 되는걸로

    @Autowired
    private ViewsRedisUtils(@Qualifier(value = "viewsRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        ViewsRedisUtils.viewsRedisTemplate = redisTemplate;
    }

    // 조회수 설정
    // 5분마다 DB에 반영 (redis 데이터 없애고 조회수 발생시 새로 반영)
    public static void addViewCount(Long pid, String useragent){
        List<String> userViewList = viewsRedisTemplate.opsForList().range(pid.toString(), 0, -1);

        // 유저정보 테이블을 pid로 조회
        // userkeyList가 null이거나 pid 리스트에 useragent 없으면 조회수 올림
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

    public static void deleteViewCount(Long pid){
        // key는 남아있고 pid만 삭제해줌
        viewsRedisTemplate.opsForZSet().remove(key, pid.toString());
    }

    // viewcount 데이터 가져옴
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
