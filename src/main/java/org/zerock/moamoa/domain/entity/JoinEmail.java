package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.joinEmails.JoinEmailAuthUpdateRequest;
import org.zerock.moamoa.domain.DTO.joinEmails.JoinEmailRequest;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "join_email")
public class JoinEmail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "code", length = 6, nullable = false)
    private String code;

    @Column(name = "authenticate", nullable = false)
    private Boolean authenticate = false;

    public static JoinEmail toEntity(JoinEmailRequest req) {
        JoinEmail joinEmail = new JoinEmail();
        joinEmail.email = req.getEmail();
        joinEmail.token = req.getEmail() + Instant.now();
        joinEmail.code = req.getCode();
        joinEmail.authenticate = req.getAuthenticate();
        return joinEmail;
    }

    public void updateCode(JoinEmailRequest req){
        this.email = req.getEmail();
        this.token = req.getEmail() + Instant.now();
        this.code = req.getCode();
    }

    public void updateAuth(JoinEmailAuthUpdateRequest req){
        this.token = req.getToken();
        this.authenticate = true;
    }
}
