package com.thoughtclan.bday.serviceimpl;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.thoughtclan.bday.dto.NewTestimonial;
import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.entity.SpecialEvent;
import com.thoughtclan.bday.entity.Testimonial;
import com.thoughtclan.bday.repository.EmployeeRepository;
import com.thoughtclan.bday.repository.SpecialEventRepository;
import com.thoughtclan.bday.repository.TestimonialRepository;
import com.thoughtclan.bday.service.FlockService;
import com.thoughtclan.bday.service.SpecialEventService;

@Service
public class SpecialEventServiceImpl implements SpecialEventService {
	
	Logger logger = LoggerFactory.getLogger(SpecialEventServiceImpl.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private FlockService flockService;
	
	@Autowired
	private SpecialEventRepository specialEventRepository;
	
	@Autowired
	private TestimonialRepository testimonialRepository;

	/**
	 * Fetches list of all the upcoming events.
	 */
	@Override
	public ResponseEntity<?> getUpcomingEvents(String eventToken) {
		try {
			String senderUserId = flockService.getUserIdFromEventToken(eventToken);
			if(flockService.verifyToken(eventToken, senderUserId)) {
				List<SpecialEvent> upcomingEvents = specialEventRepository.findByEventDateAfter(LocalDate.now());
				HashMap<LocalDate,List<SpecialEvent>> orderedEvents = new HashMap<>();
				for(SpecialEvent event: upcomingEvents) {
					if(senderUserId.equals(event.getEmployee().getUserId())) {
						continue;
					}
					if(!orderedEvents.containsKey(event.getEventDate())) {
						List<SpecialEvent> newList = new ArrayList<SpecialEvent>();
						newList.add(event);
						orderedEvents.put(event.getEventDate(), newList);
					}else{
						List<SpecialEvent> existingList = orderedEvents.get(event.getEventDate());
						existingList.add(event);
						orderedEvents.replace(event.getEventDate(), existingList);
					}
				}
				return ResponseEntity.status(HttpStatus.OK).body(orderedEvents);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/**
	 * Returns testimonial submitted by the current user.
	 */
	@Override
	public ResponseEntity<?> getEventTestimonial(int eventId, String eventToken) {
		try {
			String senderUserId = flockService.getUserIdFromEventToken(eventToken);
			if(flockService.verifyToken(eventToken, senderUserId)) {
				SpecialEvent specialEvent = specialEventRepository.findById(eventId).get();
				Employee fromEmployee = employeeRepository.findByUserId(senderUserId).get(0);
				Testimonial testimonial = testimonialRepository.findBySpecialEventAndFromEmployee(specialEvent, fromEmployee);
				return ResponseEntity.status(HttpStatus.OK).body(testimonial);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/**
	 * registers new testimonial
	 */
	@Override
	public ResponseEntity<?> createEventTestimonial(String eventToken, NewTestimonial newTestimonial) {
		try {
			String senderUserId = flockService.getUserIdFromEventToken(eventToken);
			if(flockService.verifyToken(eventToken, senderUserId)) {
				SpecialEvent specialEvent = specialEventRepository.findById(newTestimonial.getEventId()).get();
				Employee fromEmployee = employeeRepository.findByUserId(senderUserId).get(0);
				Testimonial testimonial = new Testimonial(newTestimonial.getMessage(), specialEvent, fromEmployee, LocalDate.now());
				testimonialRepository.save(testimonial);
				return ResponseEntity.status(HttpStatus.CREATED).body(testimonial);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	/**
	 * Updates the testimonial.
	 */
	@Override
	public ResponseEntity<?> updateEventTestimonial(String eventToken, int testimonialId, NewTestimonial updatedTestimonial){
		try {
			String senderUserId = flockService.getUserIdFromEventToken(eventToken);
			if(flockService.verifyToken(eventToken, senderUserId)) {
				Testimonial testimonial = testimonialRepository.findById(testimonialId).get();
				testimonial.setMessage(updatedTestimonial.getMessage());
				testimonial.setRecordedDate(LocalDate.now());
				testimonial = testimonialRepository.save(testimonial);
				return ResponseEntity.status(HttpStatus.CREATED).body(testimonial);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/**
	 * Deletes the testimonial.
	 */
	@Override
	public ResponseEntity<?> deleteEventTestimonail(String eventToken, int testimonialId) {
		try {
			String senderUserId = flockService.getUserIdFromEventToken(eventToken);
			if(flockService.verifyToken(eventToken, senderUserId)) {
				testimonialRepository.deleteById(testimonialId);
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	/**
	 * Fetches event details based on the event Id passed.
	 */
	@Override
	public ResponseEntity<?> getEventDetails(String eventToken, int eventId) {
		try {
			String SenderUserId = flockService.getUserIdFromEventToken(eventToken);
			if(flockService.verifyToken(eventToken, SenderUserId)) {
				SpecialEvent event  = specialEventRepository.findById(eventId).get();
				return ResponseEntity.status(HttpStatus.OK).body(event);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	
	
	

	

	
	

}
