package com.moratuwa.events.controllers;

import com.moratuwa.events.dto.ApproveRequest;
import com.moratuwa.events.dto.EventRequest;
import com.moratuwa.events.services.AdminService;
import com.moratuwa.events.services.EventService;
import com.moratuwa.events.services.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventController {

	@Autowired
	EventService eventService;

	@Autowired
	StudentService studentService;

	@Autowired
	AdminService adminService;

	private Logger logger = LoggerFactory.getLogger(EventController.class);

	@PostMapping(value = "/create")
	public ResponseEntity<?> eventCreate(@RequestHeader(value = "x-token", defaultValue = "") String token,
			@RequestBody EventRequest eventRequest) {

		long studentId = permissionCheck(token, "student");
		if (studentId == 0) {
			return new ResponseEntity<>("Permission Denied", HttpStatus.FORBIDDEN);
		}

		logger.info(eventRequest.toString());

		String message = eventService.createEvent(eventRequest, studentId);
		if (message != null) {
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

		return new ResponseEntity<>("Event created successfully", HttpStatus.OK);
	}

	@GetMapping(value = "/all")
	public ResponseEntity<?> eventCreate() {
		return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
	}

	@PostMapping(value = "/status")
	public ResponseEntity<?> approveEvent(@RequestHeader(value = "x-token", defaultValue = "") String token,
			@RequestBody ApproveRequest request) {

		long adminId = permissionCheck(token, "admin");
		if (adminId == 0) {
			return new ResponseEntity<>("Permission Denied", HttpStatus.FORBIDDEN);
		}

		if (!(request.getStatus().equalsIgnoreCase("approved") || request.getStatus().equalsIgnoreCase("rejected"))) {
			return new ResponseEntity<>("Status value mismatch (approved or rejected)", HttpStatus.OK);
		}

		eventService.approveOrRejectEvent(request);
		return new ResponseEntity<>("Event Updated successfully", HttpStatus.OK);
	}

	private long permissionCheck(String token, String userType) {
		if (StringUtils.isEmpty(token)) {
			return 0;
		}
		if (userType.equalsIgnoreCase("student")) {
			return studentService.getStudentId(token);
		} else {
			return adminService.getAdminId(token);
		}
	}

}