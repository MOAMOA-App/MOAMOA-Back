package org.zerock.moamoa.common.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {
	private final UserRepository userRepository;

	//username = email
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmailOrThrow(username);
		CustomUserDetails details = CustomUserDetails.fromEntity(user);
		log.info(String.valueOf(details));
		return details;
	}

	@Override
	public void createUser(UserDetails user) {

	}

	@Override
	public void updateUser(UserDetails user) {

	}

	@Override
	public void deleteUser(String username) {

	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {

	}

	@Override
	public boolean userExists(String username) {
		return userRepository.existsByEmail(username);
	}
}
