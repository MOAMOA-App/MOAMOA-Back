package org.zerock.moamoa.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 그냥 단순 암호화 복호화만 하는거니까 안써도될듯 혹시 몰라서 남겨놓음
@Configuration
public class JasyptConfig {
//    @Value("${encryptor.key}")
//    private String password;
//
//    @Bean("jasyptStringEncryptor")
//    public StringEncryptor stringEncryptor() {
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//
//        // .setPassword: encrypt key 설정. 이 encrypt key에 따라 암호화를 진행 -> 얘만 알면 다시 복호화 가능
//        // 얘를 노출시키지 않으려면 환경 변수에 설정해줘야 (지금은 임시로 이렇게 해줌)
//        config.setPassword("impromptupw123!");   // 암호화에 사용할 암호 설정
//                            // password
//
//        config.setAlgorithm("PBEWithMD5AndDES");
//        config.setPoolSize("1");
//        config.setProviderName("SunJCE");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
//        config.setStringOutputType("base64");
//        encryptor.setConfig(config);
//
//        return encryptor;
//    }
}
