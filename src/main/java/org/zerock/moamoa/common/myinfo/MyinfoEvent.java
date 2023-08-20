package org.zerock.moamoa.common.myinfo;

import lombok.Builder;
import lombok.Data;

@Data
public class MyinfoEvent {
    private Long uid;
    private int pageNo;
    private int pageSize;

    @Builder
    public MyinfoEvent(Long uid, int pageNo, int pageSize) {
        this.uid = uid;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
