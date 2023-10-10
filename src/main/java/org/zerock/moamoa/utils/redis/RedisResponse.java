package org.zerock.moamoa.utils.redis;

import lombok.Builder;
import lombok.Data;

@Data
public class RedisResponse {
    private Integer ranking;
    private Object keyword;
    private Double score;

    @Builder
    public static RedisResponse builder(Integer ranking, Object keyword, Double score) {
        RedisResponse response = new RedisResponse();
        response.ranking = ranking;
        response.keyword = keyword;
        response.score = score;
        return response;
    }
}
