package com.thoughtclan.bday.service;


import org.springframework.http.ResponseEntity;

import com.thoughtclan.bday.dto.NewEmployee;

public interface AdminService {

	public ResponseEntity<?> getAllEmployees(String evenToken);
	
	public ResponseEntity<?> addNewEmployee(NewEmployee newEmployee, String eventToken);
	
	public ResponseEntity<?> updateEmployee(int id, NewEmployee newEmployee, String eventToken);
	
	public ResponseEntity<?> deleteEmployee(int id, String eventToken);
	
	public ResponseEntity<?> findByPartialEmail(String email, String eventToken);
	
	public boolean verifyIfAdmin(String eventToken) throws Exception;
	

}
