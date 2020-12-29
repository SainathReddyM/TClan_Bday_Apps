package com.thoughtclan.bday.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtclan.bday.enums.EventStatus;
import com.thoughtclan.bday.enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="special_events")
public class SpecialEvent {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="event_code",nullable=false)
	@NotNull
	@JsonIgnore
	private String eventCode;
	
	@ManyToOne
	@JoinColumn(name="employee", nullable=false)
	private Employee employee;
	
	@Column(name="event_type", nullable=false)
	@NotNull
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Column(name="event_date", nullable=false)
	@NotNull
	private LocalDate eventDate;
	
	@Column(name="event_status", nullable=false)
	@NotNull
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus;
	
	@Lob
	@JsonIgnore
	@Column(name="greeting_card")
	private byte[] greetingCard;
	
	public SpecialEvent() {}

	public SpecialEvent(@NotNull String eventCode, Employee employee, @NotNull EventType eventType,
			@NotNull LocalDate eventDate, @NotNull EventStatus eventStatus) {
		this.eventCode = eventCode;
		this.employee = employee;
		this.eventType = eventType;
		this.eventDate = eventDate;
		this.eventStatus = eventStatus;
	}
	
	
	
}
