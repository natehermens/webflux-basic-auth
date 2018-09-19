package com.nhermens.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nhermens.auth.payload.ApiResponse;
import com.nhermens.auth.payload.AuthRequest;
import com.nhermens.auth.payload.AuthResponse;
import com.nhermens.auth.payload.SignupRequest;
import com.nhermens.auth.persist.UserDao;
import com.nhermens.auth.persist.type.User;
import com.nhermens.auth.security.JWTUtil;
import com.nhermens.auth.security.PBKDF2Encoder;
import com.nhermens.auth.security.ReactiveUserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class LoginController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private ReactiveUserDetailsServiceImpl userRepository;
	
	@Autowired
	private UserDao userDao;

	@PostMapping("/auth")
	public Mono<ResponseEntity<?>> auth(@RequestBody AuthRequest ar) {
		log.info("attempting auth user["+ar.getUsername()+"]");
		return userRepository.findByUsername(ar.getUsername())
				.map((userDetails) -> {			
					if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
						log.info("failed auth for user["+ar.getUsername()+"]");
						return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
					} else {
						log.info("successful auth for user["+ar.getUsername()+"]");
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
					}
				})
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}
	
	@PostMapping("/signup")
	public Mono<ResponseEntity<ApiResponse>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
			
		return userRepository.findByUsername(signupRequest.getUsername())
				.map((userDetails) -> {
					return ResponseEntity.badRequest().body(new ApiResponse(false, "user "+userDetails.getUsername()+" already exists.")); 
				})
				.switchIfEmpty(createUser(signupRequest));
	}
	
	public Mono<ResponseEntity<ApiResponse>> createUser(SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		Mono<User> savedUser = userDao.saveUser(user);
		return Mono.just(ResponseEntity.ok(new ApiResponse(true, "user "+request.getUsername()+" created")));
	}
}
