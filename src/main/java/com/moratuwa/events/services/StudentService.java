package com.moratuwa.events.services;

import com.moratuwa.events.dto.LoginRequest;
import com.moratuwa.events.dto.RegisterRequest;
import com.moratuwa.events.models.Student;
import com.moratuwa.events.models.Token;
import com.moratuwa.events.repositories.StudentRepository;
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
public class StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	EmailService emailService;

	@Autowired
	VerifyService verifyService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TokenRepository tokenRepository;

	private Logger logger = LoggerFactory.getLogger(StudentService.class);

	public boolean createStudent(RegisterRequest registerRequest) {

		try {
			Student student = new Student();
			student.setFirstName(registerRequest.getFirstName());
			student.setLastName(registerRequest.getLastName());
			student.setEmail(registerRequest.getEmail());
			student.setContactNo(registerRequest.getContactNo());
			student.setFaculty(registerRequest.getFaculty());
			student.setIndexNo(registerRequest.getIndexNo());
			student.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			studentRepository.createStudent(student);

			String verificationCode = Code.getVerificationCode(6);
			verifyService.createCode(registerRequest.getEmail(), verificationCode, "student");

//            emailService.sendEmail(registerRequest.getEmail(), verificationCode);

		} catch (Exception e) {
			logger.error("Error creating student", e);
			return false;
		}

		return true;
	}

	public Token studentLogin(LoginRequest request) {

		try {
			Student student = studentRepository.getStudentByEmail(request.getEmail());
			if (student == null) {
				logger.error("Student is null");
				return null;
			}

			if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
				logger.error("Password not match");
				return null;
			}

			Token token = new Token();
			token.setToken(UUID.randomUUID().toString());
			tokenRepository.createToken(token, student.getId(), "student");
			return token;

		} catch (Exception e) {
			logger.error("Error Student login", e);
			return null;
		}
	}

	public long getStudentId(String token) {
		logger.info("Get Student id fr token " + token);
		Long id = tokenRepository.getIdForToken(token, "student");
		if (id == null) {
			return 0;
		}
		return id;
	}

	public void studentLogout(String token) {
		tokenRepository.deleteToken(token, "student");
	}
}
