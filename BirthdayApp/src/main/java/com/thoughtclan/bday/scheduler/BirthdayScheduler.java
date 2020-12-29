package com.thoughtclan.bday.scheduler;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.entity.SpecialEvent;
import com.thoughtclan.bday.entity.Testimonial;
import com.thoughtclan.bday.enums.EventStatus;
import com.thoughtclan.bday.enums.EventType;
import com.thoughtclan.bday.repository.EmployeeRepository;
import com.thoughtclan.bday.repository.SpecialEventRepository;
import com.thoughtclan.bday.repository.TestimonialRepository;
import com.thoughtclan.bday.service.FlockService;

@Component
public class BirthdayScheduler {
	
	Logger logger = LoggerFactory.getLogger(BirthdayScheduler.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private SpecialEventRepository specialEventRepository;
	
	@Autowired
	private TestimonialRepository testimonialRepository;
	
	@Autowired
	private FlockService flockService;
	
	@Value("${flaskAppUrl}")
	private String flaskAppUrl;
	
	
	/**
	 * Creates events for upcoming Birthday's and work anniversaries occurring in next 30 days.
	 * Runs at 6AM everyday.
	 */
	@Scheduled(cron="0 0 6 * * *")
	public void createEvents() {
		LocalDate today = LocalDate.now();
		int currentMonth = today.getMonthValue();
		int currentMonthStartDate = today.getDayOfMonth();
		int nextMonth =currentMonth;
		int nextMonthStartDate=currentMonthStartDate;
		int currentMonthEndDate = currentMonthStartDate;
		int nextMonthEndDate = currentMonthStartDate;
		for(int i=1;i<=30;i++){
			LocalDate previous = today;
			today = today.plusDays(1);
			if(today.getMonthValue()!=previous.getMonthValue()) {
				nextMonth = today.getMonthValue();
				nextMonthStartDate =today.getDayOfMonth();
				currentMonthEndDate = previous.getDayOfMonth();
			}
		}
		nextMonthEndDate = today.getDayOfMonth();
		List<Employee> bdayList = employeeRepository.findByUpcomingBday(currentMonth, currentMonthStartDate, currentMonthEndDate, nextMonth, nextMonthStartDate, nextMonthEndDate);
		List<Employee> workAnniversaryList = employeeRepository.findByUpcomingWorkAnniversary(currentMonth, currentMonthStartDate, currentMonthEndDate, nextMonth, nextMonthStartDate, nextMonthEndDate);
		createEvents(bdayList, EventType.BIRTHDAY);
		createEvents(workAnniversaryList, EventType.WORK_ANNIVERSARY);
	}
	
	
	/**
	 * Sends reminders for upcoming events at 9AM everyday.
	 * Cron syntax - <second> <minute> <hour> <dayOfMonth> <month> <dayOfWeek>
	 */
	@Scheduled(cron="0 0 9 * * *")
	public void sendEventReminders() {
		LocalDate outDate=LocalDate.now().plusDays(7);
		List<SpecialEvent> upcomingEvents = specialEventRepository.findByEventDate(outDate);
		for(SpecialEvent event: upcomingEvents) {
			List<Employee> messageList = employeeRepository.findByEmailIdNot(event.getEmployee().getEmailId());
			for(Employee employee: messageList) {
				//code to send messages to everyone.
				if(employee.getUserId()!=null) {
					flockService.sendEventNotification(event, employee);
				}
			}
		}
	}
	
	/**
	 * Sends greetings for today's events. Runs at 8AM,10AM and 12PM everyday. 
	 */
	@Scheduled(cron="0 0 8,10,12 * * *")
	public void sendEventGreetings() {
		LocalDate todayDate = LocalDate.now();
		List<SpecialEvent> todayEvents = specialEventRepository.findByEventDate(todayDate);
		for(SpecialEvent event: todayEvents){
			if(event.getEventStatus().equals(EventStatus.PROCESSED)){
				continue;
			}
			List<Testimonial> testimonials = testimonialRepository.findBySpecialEvent(event);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("eventType", event.getEventType().toString());
			jsonObj.put("for", event.getEmployee().getFirstName());
			for(Testimonial testimonial: testimonials) {
				String fromEmployee = testimonial.getFromEmployee().getFirstName()+" "+testimonial.getFromEmployee().getLastName();
				jsonObj.put(fromEmployee, testimonial.getMessage());
			}
			byte[] greetingCard = fetchGreetingCard(jsonObj);
			event.setGreetingCard(greetingCard);
			specialEventRepository.save(event);
			boolean messageSent = flockService.sendGreeting(event);
			if(messageSent) {
				event.setEventStatus(EventStatus.PROCESSED);
				specialEventRepository.save(event);
			}
		}
	}
	
	/**
	 * Creates new events.
	 * @param employees
	 * @param eventType
	 */
	public void createEvents(List<Employee> employees, EventType eventType) {
		for(Employee emp: employees) {
			LocalDate eventDate;
			LocalDate today = LocalDate.now();
			if(eventType.equals(EventType.BIRTHDAY)) {
				if(emp.getDob().getMonthValue()<today.getMonthValue()){
					 eventDate = LocalDate.of(today.getYear()+1, emp.getDob().getMonthValue(), emp.getDob().getDayOfMonth());
				}else {
					 eventDate = LocalDate.of(today.getYear(), emp.getDob().getMonthValue(), emp.getDob().getDayOfMonth());
				}
			}else {
				if(emp.getDoj().getMonthValue()<today.getMonthValue()){
					 eventDate = LocalDate.of(today.getYear()+1, emp.getDoj().getMonthValue(), emp.getDoj().getDayOfMonth());
				}else {
					 eventDate = LocalDate.of(today.getYear(), emp.getDoj().getMonthValue(), emp.getDoj().getDayOfMonth());
				}
			}
			String eventCode = generateEventCode(emp.getId(),eventType, eventDate);
			SpecialEvent existingEvent = specialEventRepository.findByEventCode(eventCode);
			if(existingEvent==null) {
				System.out.println("-------------------"+"eventCode: "+eventCode+"----------------------");
				SpecialEvent specialEvent = new SpecialEvent(eventCode, emp, eventType, eventDate, EventStatus.NOT_PROCESSED);
				specialEventRepository.save(specialEvent);
			}
		}
	}

	/**
	 * Generates unique event code for every event based on user id, event date and event type.
	 */
	private String generateEventCode(int id, EventType eventType, LocalDate eventDate) {
		StringBuilder eventCode = new StringBuilder();
		if(eventType.equals(EventType.BIRTHDAY)){
			eventCode.append("BDAY");
		}else {
			eventCode.append("WORK");
		}
		eventCode.append(id);
		eventCode.append(String.format("%02d", eventDate.getDayOfMonth()));
		eventCode.append(String.format("%02d", eventDate.getMonthValue()));
		eventCode.append(eventDate.getYear());
		return eventCode.toString();
	}
	
	/**
	 *Fetches greeting card from the flask app.
	 */
	private byte[] fetchGreetingCard(JSONObject jsonObj) {
		try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
			HttpPost httpPost = new HttpPost(flaskAppUrl);
			StringEntity entity = new StringEntity(jsonObj.toString());
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity respEntity = response.getEntity();
			if (respEntity != null) {
				InputStream inStream = respEntity.getContent();
			    String imageString = IOUtils.toString(inStream, "UTF-8");
			    byte[] imageBytes = DatatypeConverter.parseBase64Binary(imageString);
			    return imageBytes;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	
		return null;
	}

}
