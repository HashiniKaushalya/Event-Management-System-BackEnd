package com.moratuwa.events.services;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	private Logger logger = LoggerFactory.getLogger(EmailService.class);

	public void sendEmail(String email, String code) {

		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);

			msg.setSubject("Verification Code");
			msg.setText("Your Verification Code: " + code);

			javaMailSender.send(msg);

		} catch (Exception e) {
			logger.error("Error sending email", e);
		}

	}
}
