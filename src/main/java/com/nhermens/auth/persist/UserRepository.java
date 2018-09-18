package com.nhermens.auth.persist;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nhermens.auth.persist.type.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User,String> {

	Mono<User> findByUsername(String username);
	
	Flux<User> findByUsernameLike(String username);
}
