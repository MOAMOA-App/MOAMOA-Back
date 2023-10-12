package org.zerock.moamoa.domain.DTO.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailAddrRequest {
    @NotEmpty(message = "이메일을 입력해주세요.")
    public String email;
}