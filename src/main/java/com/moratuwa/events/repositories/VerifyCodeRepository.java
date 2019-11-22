package com.moratuwa.events.repositories;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class VerifyCodeRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(VerifyCodeRepository.class);

	public void createCode(String email, String code, String userType) {
		String sql = "INSERT INTO verification_code (email, code, user_type) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, email, code, userType);
		logger.info("Verify Code create successfully");
	}

	public String getCode(String email, String userType) {
		String sql = "SELECT code FROM verification_code WHERE email= ? AND user_type = ? ORDER BY create_time DESC LIMIT 1";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { email, userType }, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error("No code found");
		}
		return null;
	}
}
