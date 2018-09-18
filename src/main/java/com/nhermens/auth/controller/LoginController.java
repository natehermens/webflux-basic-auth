package com.nhermens.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nhermens.auth.payload.AuthRequest;
import com.nhermens.auth.payload.AuthResponse;
import com.nhermens.auth.security.JWTUtil;
import com.nhermens.auth.security.PBKDF2Encoder;
import com.nhermens.auth.security.ReactiveUserDetailsServiceImpl;

import reactor.core.publisher.Mono;

@RestController
public class LoginController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private ReactiveUserDetailsServiceImpl userRepository;

	@PostMapping("/auth")
	public Mono<ResponseEntity<AuthResponse>> auth(@RequestBody AuthRequest ar) {
		
		return userRepository.findByUsername(ar.getUsername())
				.map((userDetails) -> {			
					if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
						return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
					} else {
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
					}
				});
	}
}
