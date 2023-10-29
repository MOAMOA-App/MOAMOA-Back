package org.zerock.moamoa.domain.DTO.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.zerock.moamoa.domain.enums.EmailType;

@Data
public class EmailAddrRequest {
    @NotEmpty(message = "이메일을 입력해주세요.")
    public String email;

    public EmailType type;
}
