package com.nhermens.auth.persist;

import org.springframework.stereotype.Component;

import com.nhermens.auth.persist.type.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserDao {

	private UserRepository userRepo;
	
	public UserDao(UserRepository repo) {
		userRepo = repo;
	}
	
	public Flux<User> getAll() {
		return userRepo.findAll();
	}
	
	public Mono<User> getByUserId(String id) {
		return userRepo.findById(id);
	}
	
	public Mono<User> getByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public Mono<User> saveUser(User user) {
		return userRepo.insert(user);
	}
	
	public static User clearPassword(User user) {
		return user.setPassword("");
	}
}
