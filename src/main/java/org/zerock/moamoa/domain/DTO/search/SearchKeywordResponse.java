package org.zerock.moamoa.domain.DTO.search;

import lombok.Data;
import org.zerock.moamoa.domain.entity.SearchKeyword;

@Data
public class SearchKeywordResponse {
    String keyword;
    double count;

    public static SearchKeywordResponse fromEntity(SearchKeyword searchKeyword) {
        SearchKeywordResponse response = new SearchKeywordResponse();
        response.keyword = searchKeyword.getKeyword();
        response.count = searchKeyword.getCount();
        return response;
    }
}
