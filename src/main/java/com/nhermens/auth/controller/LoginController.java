package com.nhermens.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhermens.auth.payload.ApiResponse;
import com.nhermens.auth.payload.AuthRequest;
import com.nhermens.auth.payload.AuthResponse;
import com.nhermens.auth.payload.SignupRequest;
import com.nhermens.auth.persist.UserDao;
import com.nhermens.auth.persist.type.User;
import com.nhermens.auth.security.JWTUtil;
import com.nhermens.auth.security.ReactiveUserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class LoginController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReactiveUserDetailsServiceImpl userRepository;
	
	@Autowired
	private UserDao userDao;

	@PostMapping("/auth")
	public Mono<ResponseEntity<?>> auth(@RequestBody AuthRequest ar) {
		log.info("attempting auth user["+ar.getUsername()+"]");
		return userRepository.findByUsername(ar.getUsername())
				.map((userDetails) -> {			
					if (passwordEncoder.matches(ar.getPassword(), userDetails.getPassword())) {
						log.info("successful auth for user["+ar.getUsername()+"]");
						return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
					} else {
						log.info("failed auth for user["+ar.getUsername()+"]");
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
					}
				})
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}
	
	@PostMapping("/signup")
	public Mono<ResponseEntity<ApiResponse>> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
			
		return userRepository.findByUsername(signupRequest.getUsername())
				.map((userDetails) -> {
					log.info("have UserDetails");
					return ResponseEntity.badRequest().body(new ApiResponse(false, "user "+userDetails.getUsername()+" already exists.")); 
				})
				.switchIfEmpty(createUser(signupRequest));
	}
	
	public Mono<ResponseEntity<ApiResponse>> createUser(SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		
		return userDao.saveUser(user)
				.map(u -> 
					ResponseEntity.ok(new ApiResponse(true, "add user "+request.getUsername()+" with id["+u.getId()+"]"))
				);
	}
}
