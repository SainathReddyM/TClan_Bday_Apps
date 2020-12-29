package com.thoughtclan.bday.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thoughtclan.bday.entity.SpecialEvent;
import com.thoughtclan.bday.enums.EventStatus;

public interface SpecialEventRepository extends CrudRepository<SpecialEvent, Integer>{

	public SpecialEvent findByEventCode(String eventCode);
	
	public List<SpecialEvent> findByEventStatus(EventStatus eventStatus);
	
	public List<SpecialEvent> findByEventDate(LocalDate eventDate);
	
	public List<SpecialEvent> findByEventDateAfter(LocalDate date);
	
}
