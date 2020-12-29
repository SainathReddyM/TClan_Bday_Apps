package com.thoughtclan.bday.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.flock.TokenVerifier;
import com.flock.api.Chat;
import com.flock.model.Attachment;
import com.flock.model.SendMessageOptionalParams;
import com.flock.model.Views;
import com.thoughtclan.bday.dto.FlockEvent;
import com.thoughtclan.bday.entity.AppInstall;
import com.thoughtclan.bday.entity.Employee;
import com.thoughtclan.bday.entity.SpecialEvent;
import com.thoughtclan.bday.enums.EventType;
import com.thoughtclan.bday.enums.InstallationStatus;
import com.thoughtclan.bday.repository.AppInstallsRepository;
import com.thoughtclan.bday.repository.EmployeeRepository;
import com.thoughtclan.bday.repository.SpecialEventRepository;
import com.thoughtclan.bday.service.FlockService;

/**
 * Handles incoming data from flock events.
 *
 */
@Service
public class FlockServiceImpl implements FlockService {
	
	Logger logger = LoggerFactory.getLogger(FlockServiceImpl.class);
	
	@Value("${flockAppId}")
	private String appId;
	
	@Value("${flockAppSecret}")
	private String appSecret;
	
	@Value("${domainAddress}")
	private String domainAddress;
	
	@Value("${flockAppBotToken}")
	private String flockAppBotToken;
	
	@Value("${greetingPreNameMessage}")
	private String greetingPreNameMessage;
	
	@Value("${greetingPostNameMessage}")
	private String greetingPostNameMessage;
	
	@Value("${flockUiURL}")
	private String flockUiURL;
	
	@Value("${tClanHubflockWebHookUrl}")
	private String tClanHubflockWebHookUrl;
	
	@Autowired
	private SpecialEventRepository specialEventRepository;
	
	private TokenVerifier tokenVerifier;
	
	private JWTVerifier jwtVerifier;
	
	@Autowired
	private AppInstallsRepository appInstallsRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	

	/**
	 * Verifies the JWT Token.
	 */
	@Override
	public boolean verifyToken(String token, String userId) throws Exception {
		return tokenVerifier.verifyToken(token, userId);
	}
	
	/**
	 * Persists data of new app installs.
	 */
	@Override
	public boolean appInstall(FlockEvent flockEvent) {
		try {
			List<AppInstall> existingAppInstalls = appInstallsRepository.findByUserId(flockEvent.getUserId());
			if(existingAppInstalls.isEmpty()) {
				AppInstall newAppInstall = new AppInstall();
				newAppInstall.setAuthToken(flockEvent.getToken());
				newAppInstall.setUserId(flockEvent.getUserId());
				newAppInstall.setProcessed(false);
				newAppInstall.setInstallationTime(Timestamp.from(Instant.now()));
				appInstallsRepository.save(newAppInstall);
				return true;
			}
			AppInstall existing  = existingAppInstalls.get(0);
			existing.setAuthToken(flockEvent.getToken());
			existing.setProcessed(false);
			existing.setInstallationTime(Timestamp.from(Instant.now()));
			appInstallsRepository.save(existing);
			return true;
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	
	/**
	 * In the event of app uninstall, updates the installation status concerned employee profile.
	 */
	@Override
	public void appUninstall(FlockEvent flockEvent) {
		System.out.println("uninstall");
		List<Employee> employees = employeeRepository.findByUserId(flockEvent.getUserId());
		if(!employees.isEmpty()) {
			Employee employee = employees.get(0);
			employee.setInstallationStatus(InstallationStatus.UNINSTALLED);
			employeeRepository.save(employee);
		}
	}
	
	
	/**
	 * Extracts the User ID from the flock event token and returns it. 
	 */
	@Override
	public String getUserIdFromEventToken(String token) {
		JWT jwt = (JWT) this.jwtVerifier.verify(token);
		return jwt.getClaim("userId").asString();
	}

	
	/*
	 * creates a new instance of flock TokenVerifier.
	 */
	@PostConstruct
	public void init() throws UnsupportedEncodingException {
		this.tokenVerifier=new TokenVerifier(appId,appSecret);
		this.jwtVerifier = JWT.require(Algorithm.HMAC256(appSecret)).build();
	}

	@Override
	public void sendMessage() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sends Flock Messages for Bday's and Work anniversaries.
	 */
	@Override
	public boolean sendGreeting(SpecialEvent event) {
		Encoder encoder = Base64.getEncoder();
		String encodedEventCode = encoder.encodeToString(event.getEventCode().getBytes());
		String imageUrl = domainAddress +"flock/greeting-cards/" +encodedEventCode;
		StringBuilder message = new StringBuilder();
		message.append(greetingPreNameMessage);
		message.append(" "+event.getEmployee().getFirstName()+" "+event.getEmployee().getLastName()+"'s ");
		if(event.getEventType().equals(EventType.BIRTHDAY)) {
			message.append("Birthday!! ");
		}else if(event.getEventType().equals(EventType.WORK_ANNIVERSARY)) {
			message.append("Work Anniversary! ");
		}
		message.append(greetingPostNameMessage);
		
		try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
			StringEntity imageEntity = buildImageEntity(imageUrl);
			HttpPost httpPost = new HttpPost(tClanHubflockWebHookUrl);
			JSONObject textJson = new JSONObject();
			textJson.put("text", message.toString());
			StringEntity textEntity = new StringEntity(textJson.toString());
			textEntity.setContentType("application/json");
			httpPost.setEntity(textEntity);
			HttpResponse textResponse = httpClient.execute(httpPost);
			if(textResponse.getStatusLine().getStatusCode() == 200) {
				httpPost.setEntity(imageEntity);
				HttpResponse imageResponse = httpClient.execute(httpPost);
				if(imageResponse.getStatusLine().getStatusCode() == 200) {
					return true;
				}
			}
			return false;
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * Builds an image entity JSON to post to flock.
	 */
	private StringEntity buildImageEntity(String imageUrl) throws UnsupportedEncodingException {
		JSONObject imageOriginalJson = new JSONObject();
		imageOriginalJson.put("src", imageUrl);
		JSONObject imageJson = new JSONObject();
		imageJson.put("original", imageOriginalJson);
		JSONObject viewsJson = new JSONObject();
		viewsJson.put("image", imageJson);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("views", viewsJson);
		JSONObject[] attachmentsList = new JSONObject[1];
		attachmentsList[0] = jsonObj;
		JSONObject imagePayloadJson = new JSONObject();
		imagePayloadJson.put("attachments", attachmentsList);
		StringEntity imageEntity = new StringEntity(imagePayloadJson.toString());
		imageEntity.setContentType("application/json");
		return imageEntity;
	}

	/**
	 * Fetches greeting card based on the event code.
	 */
	@Override
	public ResponseEntity<byte[]> fetchEventGreetingCard(String encodedEventCode) {
		try {
			Decoder decoder = Base64.getDecoder();
			String eventCode = new String(decoder.decode(encodedEventCode));
			SpecialEvent event = specialEventRepository.findByEventCode(eventCode);
			if(event!=null) {
				return ResponseEntity.status(HttpStatus.OK).body(event.getGreetingCard());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * Sends flock messages notifying users regarding upcoming events.
	 */
	@Override
	public void sendEventNotification(SpecialEvent event, Employee employee) {
		String eventURL = flockUiURL+"?"+"eventId="+event.getId();
		System.out.println(eventURL);
		StringBuilder message = new StringBuilder();
		message.append("<flockml>");
		message.append("Hey "+employee.getFirstName()+", ");
		message.append(event.getEmployee().getFirstName()+" "+event.getEmployee().getLastName()+"'s");
		if(event.getEventType().equals(EventType.BIRTHDAY)) {
			message.append(" birthday ");
		}else if(event.getEventType().equals(EventType.WORK_ANNIVERSARY)) {
			message.append(" Work Anniversary ");
		}
		message.append("is coming up. ");
		message.append("<action type =\"openWidget\" url=\""+eventURL+"\" desktopType=\"sidebar\" mobileType=\"modal\">Click here</action> to write a testimonial");
		message.append("</flockml>");
		Views views = new Views().flockml(message.toString());
		Attachment attachment = new Attachment().title("").description("").views(views);
		try {
			Chat.sendMessage(flockAppBotToken, employee.getUserId(),"Event Notification!", new SendMessageOptionalParams().attachments(Collections.singletonList(attachment)));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
	
	/**
	 * Fetches employee role based on the flock event token.
	 */
	@Override
	public ResponseEntity<?> getEmployeeRole(String eventToken) {
		try {
			String userId = this.getUserIdFromEventToken(eventToken);
			List<Employee> employees = employeeRepository.findByUserId(userId);
			HashMap<String, String> responseObj = new HashMap<>();
			if(employees.isEmpty()) {
				responseObj.put("Error", "No matching employee record found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObj);
			}
			Employee employee = employees.get(0);
			responseObj.put("role", employee.getRole());
			return ResponseEntity.status(HttpStatus.OK).body(responseObj);
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
}
