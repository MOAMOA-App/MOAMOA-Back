package org.zerock.moamoa.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.zerock.moamoa.domain.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
	private Long id;
	private String username;
	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static CustomUserDetails fromEntity(User user) {
		CustomUserDetails details = new CustomUserDetails();
		details.id = user.getId();
		details.username = user.getEmail();
		details.password = user.getPassword();
		return details;
	}
}