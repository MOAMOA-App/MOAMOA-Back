package org.zerock.moamoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//Audit : 감시하다
//EnableJpaAuditing :
//어노테이션이 추가된 설정이 있을 때,
//객체의 생성 및 수정 시간을 기록 할 수 있다.
@Configuration
@EnableJpaAuditing
public class AuditConfig {
}
