package com.moratuwa.events.repositories;

import com.moratuwa.events.models.Student;
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
public class StudentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private Logger logger = LoggerFactory.getLogger(StudentRepository.class);

    public void createStudent(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, email, password, faculty, index_no, contact_no) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(), student.getEmail(), student.getPassword(), student.getFaculty(), student.getIndexNo(), student.getContactNo());
        logger.info("Student Created successfully ");
    }

    public void activateStudent(String email){
        String sql = "UPDATE students SET verified = ?";
        jdbcTemplate.update(sql, 1);
        logger.info("Student Activated");
    }

    public Student getStudentByEmail(String email){
        String sql = "SELECT id, email, password FROM students WHERE email= ? AND verified = 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] {email}, new StudentMapper());
        } catch (EmptyResultDataAccessException e) {
        	logger.error("No code found");
        }
        return null;
    }

    private static final class StudentMapper implements RowMapper<Student> {
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(rs.getLong("id"));
            student.setEmail(rs.getString("email"));
            student.setPassword(rs.getString("password"));
            return student;
        }
    }
}