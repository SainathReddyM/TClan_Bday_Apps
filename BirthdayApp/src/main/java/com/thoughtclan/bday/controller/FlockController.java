package com.thoughtclan.bday.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtclan.bday.dto.FlockEvent;
import com.thoughtclan.bday.service.FlockService;


@RestController
@CrossOrigin
@RequestMapping("/flock")
public class FlockController {
	
	@Autowired
	private FlockService flockService;
	
	/**
	 * Returns greeting card based on event code of the event.
	 * @param eventCode
	 * @return
	 */
	@GetMapping(value="/greeting-cards/{eventCode}", produces = MediaType.IMAGE_PNG_VALUE)
	private ResponseEntity<byte[]> fetchGreetingCard(@PathVariable("eventCode") String eventCode){
		return flockService.fetchEventGreetingCard(eventCode);
	}
	
	
	/**
	 * Returns employee role based on flock event token.
	 * @param eventToken
	 * @return
	 */
	@GetMapping("/employee-role")
	private ResponseEntity<?> getEmployeeRole(@RequestHeader("X-Flock-Event-Token") String eventToken){
		return flockService.getEmployeeRole(eventToken);
	}
	
	
//	@GetMapping(value="/hi", produces = MediaType.IMAGE_PNG_VALUE)
//	private ResponseEntity<byte[]>  sendMessage() {
//		try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()){
//			HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/generate-card");
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("for", "Sainath");
//			String message = "Happy Birthday to you. Hope you have a great day. You are really very wonderful person. It's great to have known you. Happy Birthday to you!!!. Birthday Wishes!";
//			jsonObj.put("eventType", EventType.BIRTHDAY.toString());
//			jsonObj.put("sainath", message);
//			jsonObj.put("Akhil", message);
//			jsonObj.put("Balaji", message);
//			jsonObj.put("Yerri", message);
//			jsonObj.put("Basser", message);
//			jsonObj.put("aditya", message);
//			StringEntity entity = new StringEntity(jsonObj.toString());
//			entity.setContentType("application/json");
//			httpPost.setEntity(entity);
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpEntity respEntity = response.getEntity();
//			if (respEntity != null) {
//		       InputStream inStream = respEntity.getContent();
//		       String imageString = IOUtils.toString(inStream, "UTF-8");
//		       byte[] imageBytes = DatatypeConverter.parseBase64Binary(imageString);
////		       BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
////		       File outputfile = new File("myImage.png");
////		       ImageIO.write(image, "png", outputfile);
//		       return ResponseEntity.status(HttpStatus.OK).body(imageBytes);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//	}
	
	/**
	 * Handles new app installs and uninstalls in flock.
	 * @param flockEvent
	 * @param eventToken
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/events")
	private ResponseEntity<HttpStatus> flockEvents(@RequestBody FlockEvent flockEvent, @RequestHeader("X-Flock-Event-Token") String eventToken) throws Exception{
		System.out.println(eventToken);
		if(flockEvent.getName().equals("app.install")) {
				if(flockService.appInstall(flockEvent)) {
					return ResponseEntity.status(HttpStatus.OK).body(null);
				}
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
			if(flockEvent.getName().equals("app.uninstall")) {
				flockService.appUninstall(flockEvent);
				return ResponseEntity.status(HttpStatus.OK).body(null);
			}	
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
}
