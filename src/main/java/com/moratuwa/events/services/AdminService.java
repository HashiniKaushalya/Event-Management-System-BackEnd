package com.moratuwa.events.services;

import com.moratuwa.events.dto.LoginRequest;
import com.moratuwa.events.dto.RegisterRequest;
import com.moratuwa.events.models.Admin;
import com.moratuwa.events.models.Token;
import com.moratuwa.events.repositories.AdminRepository;
import com.moratuwa.events.repositories.TokenRepository;
import com.moratuwa.events.utils.Code;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AdminService {

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	VerifyService verifyService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TokenRepository tokenRepository;

	private Logger logger = LoggerFactory.getLogger(AdminService.class);

	public boolean createAdmin(RegisterRequest registerRequest) {

		try {
			Admin admin = new Admin();
			admin.setFirstName(registerRequest.getFirstName());
			admin.setLastName(registerRequest.getLastName());
			admin.setEmail(registerRequest.getEmail());
			admin.setFaculty(registerRequest.getFaculty());
			admin.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			adminRepository.createAdmin(admin);

			String verificationCode = Code.getVerificationCode(6);
			verifyService.createCode(registerRequest.getEmail(), verificationCode, "admin");

//            emailService.sendEmail(registerRequest.getEmail(), verificationCode);

		} catch (Exception e) {
			logger.error("Error creating admin", e);
			return false;
		}

		return true;
	}

	public Token adminLogin(LoginRequest request) {

		try {
			Admin admin = adminRepository.getAdminByEmail(request.getEmail());
			if (admin == null) {
				logger.error("Admin is null");
				return null;
			}

			if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
				logger.error("Admin Password not match");
				return null;
			}

			Token token = new Token();
			token.setToken(UUID.randomUUID().toString());
			tokenRepository.createToken(token, admin.getId(), "admin");
			return token;

		} catch (Exception e) {
			logger.error("Error Admin login", e);
			return null;
		}
	}

	public long getAdminId(String token) {
		logger.info("Get Admin id fr token " + token);
		Long id = tokenRepository.getIdForToken(token, "admin");
		if (id == null) {
			return 0;
		}
		return id;
	}

	public void adminLogout(String token) {
		tokenRepository.deleteToken(token, "admin");
	}
}
