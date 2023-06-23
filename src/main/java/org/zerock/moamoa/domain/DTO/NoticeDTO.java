package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {
    private Long id;    // 알람ID

    private User senderID;      // 회원ID(보내는)

    private User receiverID;    // 받는ID

    private String message;

    private Boolean readOrNot;

    private String type;

    private Long referenceID;   // 게시글ID

    private LocalDateTime sendDate;

    public NoticeDTO(Notice notice) {
        id = notice.getId();
        senderID = notice.getSenderID();
        receiverID = notice.getReceiverID();
        message = notice.getMessage();
        readOrNot = notice.getReadOrNot();
        type = notice.getType();
        referenceID = notice.getReferenceID();
        sendDate = notice.getSendDate();
    }
}
