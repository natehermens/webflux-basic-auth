package com.nhermens.auth.payload;

import java.util.List;

import lombok.Data;

@Data
public class UserResponse {

	private String id;
	private String username;
	private String email;
	private List<String> roles;
	private Boolean active;
}
