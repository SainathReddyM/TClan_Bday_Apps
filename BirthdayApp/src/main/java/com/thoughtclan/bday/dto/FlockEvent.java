package com.thoughtclan.bday.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlockEvent {

	private String name;
	private String userId;
	private String token;
}
