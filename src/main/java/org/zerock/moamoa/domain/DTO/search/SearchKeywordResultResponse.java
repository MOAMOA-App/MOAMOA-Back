package org.zerock.moamoa.domain.DTO.search;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class SearchKeywordResultResponse {
    String result;
    Page<SearchKeywordResponse> keyword;

    public static SearchKeywordResultResponse fromEntity(Page<SearchKeywordResponse> keyword, String result) {
        SearchKeywordResultResponse response = new SearchKeywordResultResponse();
        response.result = result;
        response.keyword = keyword;
        return response;
    }

    public static SearchKeywordResultResponse fromEntity(String result) {
        SearchKeywordResultResponse response = new SearchKeywordResultResponse();
        response.result = result;
        return response;
    }
}
