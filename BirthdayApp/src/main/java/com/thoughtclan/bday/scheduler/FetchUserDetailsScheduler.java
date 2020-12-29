package com.thoughtclan.bday.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flock.api.Users;
import com.flock.model.User;
import com.thoughtclan.bday.entity.AppInstall;
import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.enums.InstallationStatus;
import com.thoughtclan.bday.repository.AppInstallsRepository;
import com.thoughtclan.bday.repository.EmployeeRepository;

@Component
public class FetchUserDetailsScheduler {
	
	Logger logger = LoggerFactory.getLogger(FetchUserDetailsScheduler.class);
	
	@Autowired
	private AppInstallsRepository appInstallsRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * Fetches and persists user details based on app installs.
	 * The method runs once very minute.
	 */
	@Scheduled(fixedRate=60000)
	public void fetchUserDetails() {
		List<AppInstall> newInstalls=appInstallsRepository.findByIsProcessed(false);
		for(AppInstall appInstall: newInstalls) {
			List<Employee> existingEmployee = employeeRepository.findByUserId(appInstall.getUserId());
			if(existingEmployee.isEmpty()) {
				try {
					User user = Users.getInfo(appInstall.getAuthToken());
					List<Employee> existingRecords = employeeRepository.findByEmailId(user.getEmail());
					if(existingRecords.isEmpty()) {
						Employee employee = new Employee(user.getId(),user.getEmail(),user.getFirstName(),user.getLastName(),user.getRole(),user.getProfileImage(),InstallationStatus.INSTALLED,appInstall);
						employeeRepository.save(employee);
						appInstall.setProcessed(true);
						appInstallsRepository.save(appInstall);
					}else {
						Employee existingRecord = existingRecords.get(0);
						existingRecord.setUserId(user.getId());
						existingRecord.setFirstName(user.getFirstName());
						existingRecord.setLastName(user.getLastName());
						existingRecord.setProfileImage(user.getProfileImage());
						existingRecord.setRole(user.getRole());
						existingRecord.setInstallationStatus(InstallationStatus.INSTALLED);
						existingRecord.setAppInstall(appInstall);
						employeeRepository.save(existingRecord);
						appInstall.setProcessed(true);
						appInstallsRepository.save(appInstall);
					}
					
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}else {
				try {
					User user = Users.getInfo(appInstall.getAuthToken());
					Employee employee = existingEmployee.get(0);
					employee.setEmailId(user.getEmail());
					employee.setFirstName(user.getFirstName());
					employee.setInstallationStatus(InstallationStatus.INSTALLED);
					employee.setLastName(user.getLastName());
					employee.setProfileImage(user.getProfileImage());
					employee.setRole(user.getRole());
					employeeRepository.save(employee);
					appInstall.setProcessed(true);
					appInstallsRepository.save(appInstall);
				}catch(Exception e) {
					logger.error(e.getMessage());
				}
			}
		
		}
	}
}
