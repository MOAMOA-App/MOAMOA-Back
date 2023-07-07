package org.zerock.moamoa.config.security;

// @Configuration
// //@EnableWebSecurity : 2.1 버전 이후 Spring Boot Starter Security 에서는 필수가 아니다.
// public class WebSecurityExample {
// 	@Bean //메소드의 결과를 @Bean 객체로 등록해주는 어노테이션
// 	public SecurityFilterChain securityFilterChain ( HttpSecurity http )//DI 자동으로 설정됨
// 		throws Exception
// 	{
// 		/*
// 			HTTP 요청 허가 관련 설정을 하고 싶다.
// 			http
// 				. 내가 하고 싶은 설정 다 때려넣기
// 				.authorizeHttpRequests()
// 				.build();
// 		 */
// 		// 1. requestMatchers 를 사용해 설정할 URL을 지정
// 		// 2. permitAll(), authenticated() 등을 통해 어떤 사용자가 접근 가능한지 설정
// 		http
// 			//CSRF : 사이트 사이간 위조 방지 해제 (disable)
// 			.csrf(AbstractHttpConfigurer::disable)
// 			.authorizeHttpRequests(
// 				authHttp -> authHttp
// 					//requestMatchers -> 어떤 URL 로 오느 요청에 대하여 설정하는지
// 					//permitAll() -> 누가 요청해도 허가한다.
// 					.requestMatchers("/no-auth").permitAll()
// 					.requestMatchers("/re-auth", "/users/my-profile").authenticated()    //인증된 사용자만 허가
// 					.requestMatchers(
// 						"/",
// 						"users/register"
//
// 					).anonymous() //인증이 되지 않은 사용자만 허가
// 			)
// 			//form 을 이요한 로그인 관련 설정
// 			.formLogin(formLogin -> formLogin
// 				// 로그인 하는 페이지(경로)를 지정
// 				.loginPage("/users/login")
// 				//로그인 성공 시 이동하는 페이지(경로)
// 				.defaultSuccessUrl("/users/my-profile")
// 				//로그인 실패 했을 때 이동하는 페이지
// 				.failureUrl("/users/login?fail")
// 				//로그인 과정에서 필요한 경로들을 모든 사용자가 사용할 수 있게끔 권한 설정
// 				.permitAll()
// 			)
// 			//로그아웃 관련 설정
// 			// 로그인 	-> 쿠키를 통해 세션을 생성한다 (아이디, 비밀번호 필요)
// 			//로그아웃 	-> 세션을 제거 한다. -> 세션 정보만 있으면 제거 가능
// 			.logout(logout -> logout
// 				//로그아웃 요청을 보낼 URL
// 				//어떤 UI가 로그아웃 기능을 연결하고 싶으면
// 				//해당 UI가 /users/logout 으로 POST 요청을 보내게 된다.
// 				.logoutUrl("/users/logout")
// 				.logoutSuccessUrl("/users/login")
// 			);
// 		return http.build();
//
//
// 	}
//
// 	@Bean
// 	public UserDetailsManager userDetailsManager(){
//
// 		//spring 에서 미리 만들어놓은 사용자 인증 서비스
// 		return new InMemoryUserDetailsManager();
// 	}
//
// 	@Bean
// 	public PasswordEncoder passwordEncoder(){
// 		//기본적으로 사용자 비밀번호는 관리자 눈에 보이면 안된다.
// 		//모두 암호화하여 저장 및 사용 할 것.
// 		return new BCryptPasswordEncoder();
// 	}
// }
