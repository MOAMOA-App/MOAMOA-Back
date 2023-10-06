package org.zerock.moamoa.common.email;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailRequest {
    @NotEmpty(message = "이메일을 입력해주세요.")
    public String email;
}
