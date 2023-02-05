package com.babydev.app.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.auth.AuthenticationRequest;
import com.babydev.app.domain.auth.AuthenticationResponse;
import com.babydev.app.domain.auth.RegisterRequest;
import com.babydev.app.domain.entity.User;
import com.babydev.app.domain.entity.UserRole;
import com.babydev.app.repository.UserRepository;
import com.babydev.app.security.config.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationResponse register(RegisterRequest request) {
		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(UserRole.STANDARD)
				.build();
		repository.save(user);
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.getEmail(), request.getPassword()));
	
	User user = repository.findByEmail(request.getEmail())
			.orElseThrow();
	String jwtToken = jwtService.generateToken(user);
	return AuthenticationResponse.builder()
			.token(jwtToken)
			.build();
	}

}
