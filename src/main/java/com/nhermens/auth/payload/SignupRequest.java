package com.nhermens.auth.payload;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
@ToString
public class SignupRequest {

	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
}
