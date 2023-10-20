package org.zerock.moamoa.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SearchRedisUtils {
    private static RedisTemplate<String, String> searchRedisTemplate;
    private static final String searchKey = "search_keyword";

    @Autowired
    private SearchRedisUtils(@Qualifier(value = "redisTemplate") RedisTemplate<String, String> redisTemplate) {
        SearchRedisUtils.searchRedisTemplate = redisTemplate;
    }

    public static void addSearchKeyword(String[] keywords) {
        for (String keyword : keywords) {
            // 검색어의 Score 증가
            searchRedisTemplate.opsForZSet().incrementScore(searchKey, keyword, 1);

            // 검색어에 1일 동안의 유효 기간 설정
            searchRedisTemplate.expire(searchKey, 1, TimeUnit.DAYS);
        }
    }

    public static List<RedisResponse> readAllKeywords() {
        // ZRANGEWITHSCORES 명령어를 사용하여 모든 멤버와 점수를 가져옴
        Set<ZSetOperations.TypedTuple<String>> searchKeywords =
                searchRedisTemplate.opsForZSet().rangeWithScores(searchKey, 0, -1);

        List<RedisResponse> topKeywords = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : searchKeywords) {
            topKeywords.add(RedisResponse.builder(topKeywords.size() + 1, tuple.getValue(), tuple.getScore()));
        }
        return topKeywords;
    }

    public static List<RedisResponse> readTopSearchKeywords(int count) {
        Set<ZSetOperations.TypedTuple<String>> result = searchRedisTemplate.opsForZSet().reverseRangeWithScores(searchKey, 0, count - 1);

        List<RedisResponse> topKeywords = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : result) {
            topKeywords.add(RedisResponse.builder(
                    topKeywords.size() + 1,
                    tuple.getValue(),
                    tuple.getScore()
            ));
        }
        return topKeywords;
    }

}
