package com.nhermens.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data  
@AllArgsConstructor 
@ToString
public class ApiResponse {

	private Boolean success;
	private String message;
}
