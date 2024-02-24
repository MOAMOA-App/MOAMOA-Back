package org.zerock.moamoa.common.fixture;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithMockCustomUser {
    String userName() default "user";
    String role() default "USER";

}
