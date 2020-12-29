package com.thoughtclan.bday.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeProfile {

	private String firstName;
	
	private String lastName;
	
	private LocalDate eventDate;
}
