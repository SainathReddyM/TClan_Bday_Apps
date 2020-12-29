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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtclan.bday.enums.InstallationStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * Persists Employee profiles.
 *
 */
@Entity
@Table(name="employee")
@Getter
@Setter
public class Employee {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="user_id")
	@JsonIgnore
	private String userId;
	
	@Column(name="email", nullable=false)
	@NotNull
	private String emailId;
	
	@Column(name="first_name",nullable=false)
	@NotNull
	private String firstName;
	
	@Column(name="last_name",nullable=false)
	@NotNull
	private String lastName;
	
	@Column(name="role")
	private String role;
	
	@JsonIgnore
	@Column(name="profile_image")
	private String profileImage;
	
	@Column(name="dob")
	private LocalDate dob;
	
	@Column(name="doj")
	private LocalDate doj;
	
	@Column(name="installation_status")
	@Enumerated(EnumType.STRING)
	private InstallationStatus installationStatus;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="app_installs_id", referencedColumnName = "id")
	private AppInstall appInstall;
	

	public Employee(@NotNull String userId, @NotNull String emailId, @NotNull String firstName,
			@NotNull String lastName, String role, String profileImage, InstallationStatus installationStatus,
			AppInstall appInstall) {
		this.userId = userId;
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.profileImage = profileImage;
		this.installationStatus = installationStatus;
		this.appInstall = appInstall;
	}
	
	
	public Employee() {}



	public Employee(@NotNull String emailId, @NotNull String firstName, @NotNull String lastName, String role, LocalDate dob,
			LocalDate doj) {
		super();
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.dob = dob;
		this.doj = doj;
	}
	
	
	
}
