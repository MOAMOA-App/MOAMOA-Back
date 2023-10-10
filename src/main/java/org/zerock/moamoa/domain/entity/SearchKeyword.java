package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.zerock.moamoa.common.domain.entity.BaseEntity;

@Entity
@Getter
public class SearchKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column
    private String keyword;
    private Double count;

    public static SearchKeyword fromDto(String keyword, double count) {
        SearchKeyword entity = new SearchKeyword();
        entity.keyword = keyword;
        entity.count = count;
        return entity;
    }

    public void plusCount(double count) {
        this.count += count;
    }
}
