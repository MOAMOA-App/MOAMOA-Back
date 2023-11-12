package org.zerock.moamoa.common.auth;

import lombok.Data;

@Data
public class ValidResponse {
    boolean isValid;
    Exception exception;

    public ValidResponse(boolean isValid) {
        this.isValid = isValid;
    }

    public ValidResponse(boolean isValid, Exception exception) {
        this.isValid = isValid;
        this.exception = exception;
    }
}
