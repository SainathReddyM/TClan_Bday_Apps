package com.thoughtclan.bday.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO used for persisting new users by admin.
 */
@Getter
@Setter
@AllArgsConstructor
public class NewEmployee {
	
	private String emailId;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dob;
	
	private LocalDate doj;
	
	private String role;
}
