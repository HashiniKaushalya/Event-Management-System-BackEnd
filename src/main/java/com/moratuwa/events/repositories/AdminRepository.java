package com.moratuwa.events.repositories;

import com.moratuwa.events.models.Admin;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Repository
public class AdminRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(AdminRepository.class);

	public void createAdmin(Admin admin) {
		String sql = "INSERT INTO admins (first_name, last_name, email, password, faculty) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, admin.getFirstName(), admin.getLastName(), admin.getEmail(), admin.getPassword(),
				admin.getFaculty());
		logger.info("Admin Created successfully ");
	}

	public void activateAdmin(String email) {
		String sql = "UPDATE admins SET verified = ?";
		jdbcTemplate.update(sql, 1);
		logger.info("Admin Activated");
	}

	public Admin getAdminByEmail(String email) {
		String sql = "SELECT id, email, password FROM admins WHERE email= ? AND verified = 1";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { email }, new AdminMapper());
		} catch (EmptyResultDataAccessException e) {
			logger.error("No code found");
		}
		return null;
	}

	private static final class AdminMapper implements RowMapper<Admin> {
		public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
			Admin admin = new Admin();
			admin.setId(rs.getLong("id"));
			admin.setEmail(rs.getString("email"));
			admin.setPassword(rs.getString("password"));
			return admin;
		}
	}

}
