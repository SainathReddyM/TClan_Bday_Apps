package com.thoughtclan.bday.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.entity.SpecialEvent;
import com.thoughtclan.bday.entity.Testimonial;

public interface TestimonialRepository extends CrudRepository<Testimonial, Integer> {

	public Testimonial findBySpecialEventAndFromEmployee(SpecialEvent specialEvent, Employee fromEmployee);
	
	public List<Testimonial> findBySpecialEvent(SpecialEvent specialEvent);
}
