package org.zerock.moamoa.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.DTO.email.EmailAuthUpdateRequest;
import org.zerock.moamoa.domain.DTO.email.EmailRequest;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "emails")
public class Email extends BaseEntity {
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

    public static Email toEntity(EmailRequest req) {
        Email email = new Email();
        email.email = req.getEmail();
        email.token = req.getEmail() + Instant.now();
        email.code = req.getCode();
        email.authenticate = req.getAuthenticate();
        return email;
    }

    public void updateCode(EmailRequest req){
        this.email = req.getEmail();
        this.token = req.getEmail() + Instant.now();
        this.code = req.getCode();
    }

    public void updateAuth(EmailAuthUpdateRequest req){
        this.token = req.getToken();
        this.authenticate = true;
    }
}
