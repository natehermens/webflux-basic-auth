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
public class AuthRequest {
	
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}
