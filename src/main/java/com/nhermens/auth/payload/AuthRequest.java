package com.nhermens.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString
public class  AuthRequest {
	
	private String username;
	private String password;
}