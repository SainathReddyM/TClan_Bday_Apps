package com.thoughtclan.bday.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.thoughtclan.bday.dto.NewEmployee;
import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.repository.EmployeeRepository;
import com.thoughtclan.bday.service.AdminService;
import com.thoughtclan.bday.service.FlockService;

@Service
public class AdminServiceImpl implements AdminService {
	
	Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private FlockService flockService;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	/**
	 * Verifies if the eventToken is from admin user.
	 * @throws Exception 
	 */
	@Override
	public boolean verifyIfAdmin(String eventToken) throws Exception {
		String userId = flockService.getUserIdFromEventToken(eventToken);
		if(flockService.verifyToken(eventToken, userId)){
			List<Employee> employees = employeeRepository.findByUserId(userId);
			if(employees.isEmpty()){
				return false;
			}
			Employee employee = employees.get(0);
			if(employee.getRole().equals("user")){
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns List of all employees.
	 */
	@Override
	public ResponseEntity<?> getAllEmployees(String eventToken) {
		try {
			if(this.verifyIfAdmin(eventToken)) {
				Iterable<Employee> employees= employeeRepository.findAll();
				List<Employee> employeeList = new ArrayList<>();
				for(Employee emp: employees) {
					employeeList.add(emp);
				}
				return ResponseEntity.status(HttpStatus.OK).body(employeeList);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@Override
	public ResponseEntity<?> addNewEmployee(NewEmployee newEmployee, String eventToken) {
		try {
			if(this.verifyIfAdmin(eventToken)){
				Employee employee = new Employee(newEmployee.getEmailId(),newEmployee.getFirstName(),newEmployee.getLastName(), newEmployee.getRole(), newEmployee.getDob(),newEmployee.getDoj());
				employeeRepository.save(employee);
				return ResponseEntity.status(HttpStatus.CREATED).body(employee);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * updates employee profile.
	 */
	@Override
	public ResponseEntity<?> updateEmployee(int id, NewEmployee newEmployee, String eventToken) {
		try {
			if(this.verifyIfAdmin(eventToken)){
				Employee employee = employeeRepository.findById(id).get();
				if(!Strings.isNullOrEmpty(newEmployee.getFirstName())) {
					employee.setFirstName(newEmployee.getFirstName());
				}
				if(!Strings.isNullOrEmpty(newEmployee.getLastName())) {
					employee.setLastName(newEmployee.getLastName());
				}
				if(!Strings.isNullOrEmpty(newEmployee.getRole())) {
					employee.setRole(newEmployee.getRole());
				}
				if(newEmployee.getDob() != null){
					employee.setDob(newEmployee.getDob());
				}
				if(newEmployee.getDoj() != null) {
					employee.setDoj(newEmployee.getDoj());
				}
				Employee updatedEmployee = employeeRepository.save(employee);
				return ResponseEntity.status(HttpStatus.CREATED).body(updatedEmployee);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * Deletes employee.
	 */
	@Override
	public ResponseEntity<?> deleteEmployee(int id, String eventToken) {
		try {
			if(this.verifyIfAdmin(eventToken)) {
				employeeRepository.deleteById(id);
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * Fetches all the employees which matching partial email.
	 */
	@Override
	public ResponseEntity<?> findByPartialEmail(String email, String eventToken) {
		try {
			if(this.verifyIfAdmin(eventToken)){
				List<Employee> employees = employeeRepository.findByEmailIdStartsWith(email);
				return ResponseEntity.status(HttpStatus.OK).body(employees);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	
	

}
