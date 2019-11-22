package com.moratuwa.events.repositories;

import com.moratuwa.events.dto.EventResponse;
import com.moratuwa.events.dto.StudentResponse;
import com.moratuwa.events.models.Event;
import com.moratuwa.events.models.EventType;
import com.moratuwa.events.models.Venue;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class EventRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Logger logger = LoggerFactory.getLogger(EventRepository.class);

    public void createEvent(Event event){
        String sql = "INSERT INTO events (name, event_date, event_time, student_id, event_type_id, venue_id, permission_file) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, event.getEventName(), event.getEventDate(), event.getEventTime(), event.getStudentId(), event.getEventTypeId(), event.getVenueId(), event.getPermissionFile());
        logger.info("Event Created successfully ");
    }


    public List<EventResponse> getAllEvents(){
        String sql = "SELECT e.id, e.name, e.event_date, e.event_time, e.permission_file, e.status, v.name as venue_name, et.name as event_type, st.first_name, st.last_name  FROM events as e INNER JOIN venues as v ON e.venue_id=v.id INNER JOIN event_types as et ON  e.event_type_id=et.id INNER JOIN students as st ON e.student_id = st.id";
        return jdbcTemplate.query(sql, new EventResponseMapper());
    }

    public void updateEventStatus(long eventId, String status){
        String sql = "UPDATE events SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, eventId);
        logger.info("Event Status updated");
    }

    // helpers attributes for event creation
    public List<EventType> getEventTypes(){
        String sql = "SELECT id, name FROM event_types";
        return jdbcTemplate.query(sql, new EventTypeMapper());
    }


    public List<Venue> getVenues(){
        String sql = "SELECT id, name FROM venues";
        return jdbcTemplate.query(sql, new VenueMapper());
    }

    public boolean isEventByDateAndVenue(Date eventDate, Integer venueId){
        String sql = "SELECT 1 FROM events WHERE event_date= ? AND venue_id = ? AND status != 'rejected'";
        try {
            jdbcTemplate.queryForObject(sql, new Object[] { eventDate, venueId},  Integer.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
        	logger.error("No code found");
            return false;
        }
    }

    private static final class EventTypeMapper implements RowMapper<EventType> {
        public EventType mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventType eventType = new EventType();
            eventType.setId(rs.getInt("id"));
            eventType.setName(rs.getString("name"));
            return eventType;
        }
    }


    private static final class VenueMapper implements RowMapper<Venue> {
        public Venue mapRow(ResultSet rs, int rowNum) throws SQLException {
            Venue venue = new Venue();
            venue.setId(rs.getInt("id"));
            venue.setName(rs.getString("name"));
            return venue;
        }
    }

    private static final class EventResponseMapper implements RowMapper<EventResponse> {
        public EventResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventResponse event = new EventResponse();
            event.setId(rs.getLong("id"));
            event.setEventName(rs.getString("name"));
            event.setEventDate(rs.getDate("event_date"));
            event.setEventTime(rs.getTime("event_time"));
            event.setPermissionFile(rs.getString("permission_file"));
            event.setVenue(rs.getString("venue_name"));
            event.setEventType(rs.getString("event_type"));
            event.setStatus(rs.getString("status"));
            StudentResponse studentResponse = new StudentResponse();
            studentResponse.setFirstName(rs.getString("first_name"));
            studentResponse.setLastName(rs.getString("last_name"));
            event.setCreatedBy(studentResponse);
            return event;
        }
    }

}