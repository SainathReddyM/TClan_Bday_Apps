package com.thoughtclan.bday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtclan.bday.dto.NewEmployee;
import com.thoughtclan.bday.service.AdminService;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@GetMapping("/employees")
	public ResponseEntity<?> getAllEmployees(@RequestHeader("X-Flock-Event-Token") String eventToken){
		return adminService.getAllEmployees(eventToken);
	}
	
	@GetMapping("/employees/{email}")
	public ResponseEntity<?> getEmployeebyEmailId(@PathVariable("email") String email, @RequestHeader("X-Flock-Event-Token") String eventToken){
		return adminService.findByPartialEmail(email, eventToken);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<?> addNewEmployee(@RequestBody NewEmployee newEmployee, @RequestHeader("X-Flock-Event-Token") String eventToken){
		return adminService.addNewEmployee(newEmployee, eventToken);
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") int id, @RequestBody NewEmployee newEmployee, @RequestHeader("X-Flock-Event-Token") String eventToken){
		return adminService.updateEmployee(id, newEmployee, eventToken);
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") int id, @RequestHeader("X-Flock-Event-Token") String eventToken){
		return adminService.deleteEmployee(id, eventToken);
	}
}
