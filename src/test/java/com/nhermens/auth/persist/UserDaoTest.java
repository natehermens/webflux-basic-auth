package com.nhermens.auth.persist;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

import com.nhermens.auth.persist.type.User;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class UserDaoTest {

	@Test
	public void testGetAll() {
		
		User user = new User();
		user.setId("1");
		Flux<User> fluxUser = Flux.just(user);
		UserRepository userRepo = Mockito.mock(UserRepository.class);
		when(userRepo.findAll()).thenReturn(fluxUser);
		
		UserDao dao = new UserDao(userRepo);
		Flux<User> fluxResult = dao.getAll();
		StepVerifier.create(fluxResult)
				.expectNext(user)
				.verifyComplete();
	}
}
