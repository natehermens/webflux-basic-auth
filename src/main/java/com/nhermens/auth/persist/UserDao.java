package com.nhermens.auth.persist;

import com.nhermens.auth.persist.type.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserDao {

	private UserRepository userRepo;
	
	public UserDao(UserRepository repo) {
		userRepo = repo;
	}
	
	public Flux<User> getAll() {
		return userRepo.findAll().map(this::clearPassword);
	}
	
	public Mono<User> getByUserId(String id) {
		return userRepo.findById(id).map(this::clearPassword);
	}
	
	public Mono<User> getByUsername(String username) {
		return userRepo.findByUsername(username).map(this::clearPassword);
	}
	
	public Mono<User> saveUser(User user) {
		return userRepo.insert(user);
	}
	
	private User clearPassword(User user) {
		return user.setPassword("");
	}
}
