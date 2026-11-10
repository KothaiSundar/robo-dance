package com.robodance.auth.service;

import com.robodance.auth.dto.AuthResponse;
import com.robodance.auth.dto.LoginRequest;
import com.robodance.auth.dto.RegisterRequest;
import com.robodance.auth.entity.UserAccount;
import com.robodance.auth.repo.UserAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

	private final UserAccountRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(
			UserAccountRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		String email = request.email().trim().toLowerCase();
		if (userRepository.existsByEmailIgnoreCase(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}

		UserAccount user = new UserAccount();
		user.setEmail(email);
		user.setDisplayName(request.displayName().trim());
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		userRepository.save(user);

		return toResponse(user);
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		UserAccount user = userRepository
				.findByEmailIgnoreCase(request.email().trim())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}

		return toResponse(user);
	}

	private AuthResponse toResponse(UserAccount user) {
		return new AuthResponse(
				jwtService.createToken(user),
				user.getId(),
				user.getEmail(),
				user.getDisplayName());
	}
}
