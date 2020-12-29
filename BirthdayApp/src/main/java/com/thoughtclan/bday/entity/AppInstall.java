package com.thoughtclan.bday.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/*
 * Persists data of app install's on flock.
 */
@Getter
@Setter
@Entity
@Table(name="app_installs")
public class AppInstall {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="auth_token",nullable=false)
	@NotNull
	private String authToken;
	
	@Column(name="user_id",nullable=false)
	@NotNull
	private String userId;
	
	@Column(name="is_processed",nullable=false)
	@NotNull
	private boolean isProcessed;
	
	@Column(name="installation_time")
	private Timestamp installationTime;
	
	@OneToOne(mappedBy = "appInstall")
	private Employee employee;
}
