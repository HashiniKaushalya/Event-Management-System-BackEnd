package com.moratuwa.events.repositories;

import com.moratuwa.events.models.Token;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TokenRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private Logger logger = LoggerFactory.getLogger(TokenRepository.class);

    public void createToken(Token token, long userId, String role) {
        String sql = "INSERT INTO access_tokens (token, user_id, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, token.getToken(), userId, role);
        logger.info("Token successfully created ");
    }

    public Long getIdForToken(String token, String role){
        String sql = "SELECT user_id FROM access_tokens WHERE token= ? AND role = ? ORDER BY create_time DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { token, role}, Long.class);
        } catch (EmptyResultDataAccessException e) {
        	logger.error("No code found");
            return null;
        }
    }

    public void deleteToken(String token, String role){
        String sql = "DELETE FROM access_tokens WHERE token = ? AND role = ?";
        jdbcTemplate.update(sql, token, role);
        logger.info("Token successfully deleted ");
    }
}
