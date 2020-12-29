package com.thoughtclan.bday.entity;


import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="testimonials")
public class Testimonial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="message")
	@NotNull
	@Size(max=160)
	private String message;
	
	@ManyToOne
	@JoinColumn(name="special_event_id",nullable=false)
	@JsonIgnore
	private SpecialEvent specialEvent;
	
	@ManyToOne
	@JoinColumn(name="from_employee", nullable=false)
	private Employee fromEmployee;
	
	@Column(name="recorded_date")
	@NotNull
	private LocalDate recordedDate;
	
	public Testimonial() {}

	public Testimonial(@NotNull String message, SpecialEvent specialEvent, Employee fromEmployee,
			@NotNull LocalDate recordedDate) {
		super();
		this.message = message;
		this.specialEvent = specialEvent;
		this.fromEmployee = fromEmployee;
		this.recordedDate = recordedDate;
	}
	
	
}
