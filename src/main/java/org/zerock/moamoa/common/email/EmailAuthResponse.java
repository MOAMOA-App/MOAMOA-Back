package org.zerock.moamoa.common.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EmailAuthResponse {
    private String code;
}