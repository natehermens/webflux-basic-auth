package com.nhermens.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.nhermens.auth.controller.LoginController;
import com.nhermens.auth.persist.UserDao;
import com.nhermens.auth.persist.type.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

	@Autowired
	private UserDao userDao;
	
	public Mono<UserDetails> findByUsername(String username) {
		log.info("checking for user["+username+"]");
		return userDao.getByUsername(username).map(u -> u.toUserDetails());
	}
}
