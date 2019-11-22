package com.moratuwa.events.controllers;

import com.moratuwa.events.dto.LoginRequest;
import com.moratuwa.events.dto.RegisterRequest;
import com.moratuwa.events.dto.VerifyRequest;
import com.moratuwa.events.models.Token;
import com.moratuwa.events.services.StudentService;
import com.moratuwa.events.services.VerifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	StudentService studentService;

	@Autowired
	VerifyService verifyService;

	@PostMapping(value = "/register")
	public ResponseEntity<?> registerStudent(@RequestBody RegisterRequest registerRequest) {

		boolean isCreated = studentService.createStudent(registerRequest);

		if (!isCreated) {
			return new ResponseEntity<>("Error creating student please try again", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Successfully Created, Please check your email for verification", HttpStatus.OK);
	}

	@PostMapping(value = "/verify")
	public ResponseEntity<?> registerStudentVerification(@RequestBody VerifyRequest verifyRequest) {

		boolean isActivated = verifyService.verify(verifyRequest.getEmail(), verifyRequest.getCode(), "user");
		if (!isActivated) {
			return new ResponseEntity<>("Invalid Token or Token not found", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Activation success", HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<?> studentAuth(@RequestBody LoginRequest loginRequest) {

		Token token = studentService.studentLogin(loginRequest);

		if (token == null) {
			return new ResponseEntity<>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@PostMapping(value = "/logout")
	public ResponseEntity<?> studentLogout(@RequestHeader(value = "x-token", defaultValue = "") String token) {
		if (StringUtils.isEmpty(token)) {
			return new ResponseEntity<>("Error logout please try again", HttpStatus.BAD_REQUEST);
		}

		studentService.studentLogout(token);
		return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
	}

}
