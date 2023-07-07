package org.zerock.moamoa.common.email;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String name;
}
