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

import com.thoughtclan.bday.dto.NewTestimonial;
import com.thoughtclan.bday.service.SpecialEventService;

@RestController
@CrossOrigin
@RequestMapping("/special-events")
public class SpecialEventController {

	
	@Autowired
	private SpecialEventService specialEventService;
	
	@GetMapping("/events")
	public ResponseEntity<?> getSpecialEvents(@RequestHeader("X-Flock-Event-Token") String eventToken){
		return specialEventService.getUpcomingEvents(eventToken);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getEventDetails(@RequestHeader("X-Flock-Event-Token") String eventToken, @PathVariable("id") int eventId){
		return specialEventService.getEventDetails(eventToken, eventId);
	}
	
	@GetMapping("/events/{id}")
	public ResponseEntity<?> getEventTestimonial(@RequestHeader("X-Flock-Event-Token") String eventToken, @PathVariable("id") int eventId){
		return specialEventService.getEventTestimonial(eventId, eventToken);
	}
	
	@PostMapping("/events")
	public ResponseEntity<?> createEventTestimonial(@RequestHeader("X-Flock-Event-Token") String eventToken, @RequestBody NewTestimonial newTestimonial ){
		return specialEventService.createEventTestimonial(eventToken, newTestimonial);
	}
	
	@PutMapping("/events/{id}")
	public ResponseEntity<?> updateEventTestimonial(@RequestHeader("X-Flock-Event-Token") String eventToken, @PathVariable("id") int testimonialId, @RequestBody NewTestimonial updatedTestimonial){
		return specialEventService.updateEventTestimonial(eventToken, testimonialId, updatedTestimonial);
	}
	
	@DeleteMapping("/events/{id}")
	public ResponseEntity<?> deleteEventTestimonial(@RequestHeader("X-Flock-Event-Token") String eventToken, @PathVariable("id") int testimonialId){
		return specialEventService.deleteEventTestimonail(eventToken, testimonialId);
	}
	
	
	
}
