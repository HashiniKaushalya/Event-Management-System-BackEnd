package com.moratuwa.events.controllers;

import com.moratuwa.events.dto.LoginRequest;
import com.moratuwa.events.dto.RegisterRequest;
import com.moratuwa.events.dto.VerifyRequest;
import com.moratuwa.events.models.Token;
import com.moratuwa.events.services.AdminService;
import com.moratuwa.events.services.VerifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@Autowired
	VerifyService verifyService;

	@PostMapping(value = "/register")
	public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest) {
		boolean isCreated = adminService.createAdmin(registerRequest);

		if (!isCreated) {
			return new ResponseEntity<>("Error creating admin please try again", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Successfully Created, Please check your email for verification", HttpStatus.OK);
	}

	@PostMapping(value = "/verify")
	public ResponseEntity<?> registerAdminVerification(@RequestBody VerifyRequest verifyRequest) {

		boolean isActivated = verifyService.verify(verifyRequest.getEmail(), verifyRequest.getCode(), "admin");

		if (!isActivated) {
			return new ResponseEntity<>("Invalid Token or Token not found", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Activation success", HttpStatus.OK);

	}

	@PostMapping(value = "/login")
	public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {

		Token token = adminService.adminLogin(loginRequest);

		if (token == null) {
			return new ResponseEntity<>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@PostMapping(value = "/logout")
	public ResponseEntity<?> adminLogout(@RequestHeader(value = "x-token", defaultValue = "") String token) {

		if (StringUtils.isEmpty(token)) {
			return new ResponseEntity<>("Error logout please try again", HttpStatus.BAD_REQUEST);
		}

		adminService.adminLogout(token);
		return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
	}

}
