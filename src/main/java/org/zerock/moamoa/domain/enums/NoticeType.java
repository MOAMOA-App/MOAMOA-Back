package org.zerock.moamoa.domain.enums;

import lombok.Getter;

@Getter
public enum NoticeType {
    POST_CHANGED(0, "게시글이 변경되었습니다."),
    STATUS_CHANGED(1, "게시글의 상태가 변경되었습니다."),
    WISH_CHANGED(2, "관심 게시글이 변경되었습니다."),
    NEW_ANNOUNCE(3, "새로운 공지사항이 추가되었습니다."),
    NEW_CHATROOM(4, "새로운 채팅방이 생성되었습니다."),
    ;

    NoticeType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final int code;
    private final String msg;

    public static NoticeType fromCode(int code) {
        for (NoticeType noticeType : NoticeType.values()) {
            if (noticeType.getCode() == code) {
                return noticeType;
            }
        }
        throw new IllegalArgumentException("Invalid NoticeType code: " + code);
    }
}
