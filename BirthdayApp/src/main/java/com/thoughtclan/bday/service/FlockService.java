package com.thoughtclan.bday.service;

import org.springframework.http.ResponseEntity;

import com.thoughtclan.bday.dto.FlockEvent;
import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.entity.SpecialEvent;

public interface FlockService {

	
	public boolean verifyToken(String token, String userId) throws Exception;
	
	public boolean appInstall(FlockEvent flockEvent);
	
	public void appUninstall(FlockEvent flockEvent);

	void sendMessage() throws Exception;
	
	public String getUserIdFromEventToken(String token);
	
	public boolean sendGreeting(SpecialEvent event);
	
	public ResponseEntity<byte[]> fetchEventGreetingCard(String eventCode);
	
	public void sendEventNotification(SpecialEvent event, Employee employee);

	public ResponseEntity<?> getEmployeeRole(String eventToken);
}
