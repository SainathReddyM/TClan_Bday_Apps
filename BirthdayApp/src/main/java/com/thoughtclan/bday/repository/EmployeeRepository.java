package com.thoughtclan.bday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.thoughtclan.bday.entity.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer>  {
	
	public List<Employee> findByUserId(String userId);
	
	public List<Employee> findByEmailIdNot(String email);
	
	public List<Employee> findByEmailId(String email);
	
	public List<Employee> findByEmailIdStartsWith(String email);
	
	@Query(value="select * from employee where (MONTH(dob)=?1 and DAY(dob) between ?2 and ?3) or (MONTH(dob)=?4 and DAY(dob) between ?5 and ?6)", nativeQuery = true)
	public List<Employee> findByUpcomingBday(int currentMonth, int currentMonthStartDate, int currentMonthEndDate ,int nextMonth, int nextMonthStartDate, int nextMonthEndDate);

	@Query(value="select * from employee where (MONTH(doj)=?1 and DAY(doj) between ?2 and ?3) or (MONTH(doj)=?4 and DAY(doj) between ?5 and ?6)", nativeQuery = true)
	public List<Employee> findByUpcomingWorkAnniversary(int currentMonth, int currentMonthStartDate, int currentMonthEndDate ,int nextMonth, int nextMonthStartDate, int nextMonthEndDate);


}
