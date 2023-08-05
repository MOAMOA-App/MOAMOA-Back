package org.zerock.moamoa.common.auth.oAuth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String registrationId = userRequest
			.getClientRegistration()
			// application.yaml에 등록한
			// id가 나온다.
			.getRegistrationId();
		String nameAttribute = "";
		// 사용할 데이터를 다시 정리하는 목적의 Map
		Map<String, Object> attributes = new HashMap<>();

		// Kakao 로직
		if (registrationId.equals("kakao")) {
			attributes.put("loginType", "kakao");
			attributes.put("id", oAuth2User.getAttribute("id"));

			Map<String, Object> propMap = oAuth2User.getAttribute("properties");
			attributes.put("name", propMap.get("nickname"));

			Map<String, Object> accountMap = oAuth2User.getAttribute("kakao_account");
			attributes.put("email", accountMap.get("email"));
			nameAttribute = "email";
		}

		// Naver 로직
		if (registrationId.equals("naver")) {
			attributes.put("LoginType", "naver");

			// 받은 사용자 데이터를 정리한다.
			Map<String, Object> responseMap = oAuth2User.getAttribute("response");
			attributes.put("id", responseMap.get("id"));
			attributes.put("email", responseMap.get("email"));
			attributes.put("name", responseMap.get("nickname"));
			nameAttribute = "email";
		}

		// 기본설정으로는 여기까지 오면 인증 성공
		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority("USER")),
			attributes,
			nameAttribute
		);
	}
}
