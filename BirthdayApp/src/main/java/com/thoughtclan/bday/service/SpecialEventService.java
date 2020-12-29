package com.thoughtclan.bday.service;

import org.springframework.http.ResponseEntity;

import com.thoughtclan.bday.dto.NewTestimonial;

public interface SpecialEventService {

	public ResponseEntity<?>  getUpcomingEvents(String eventToken);
	
	public ResponseEntity<?> getEventTestimonial(int eventId, String eventToken);
	
	public ResponseEntity<?> createEventTestimonial(String eventToken, NewTestimonial newTestimonial);
	
	public ResponseEntity<?> updateEventTestimonial(String eventToken, int testimonialId, NewTestimonial updatedTestimonial);
	
	public ResponseEntity<?> deleteEventTestimonail(String eventToken, int testimonialId);
	
	public ResponseEntity<?> getEventDetails(String eventToken, int eventId);
	
}
