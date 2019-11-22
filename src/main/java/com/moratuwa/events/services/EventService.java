package com.moratuwa.events.services;

import com.moratuwa.events.dto.ApproveRequest;
import com.moratuwa.events.dto.EventRequest;
import com.moratuwa.events.dto.EventResponse;
import com.moratuwa.events.models.Event;
import com.moratuwa.events.models.EventType;
import com.moratuwa.events.models.Venue;
import com.moratuwa.events.repositories.EventRepository;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EventService {

	@Autowired
	EventRepository eventRepository;

	private Logger logger = LoggerFactory.getLogger(EventService.class);

	public String createEvent(EventRequest eventRequest, long studentId) {

		try {
			if (eventRepository.isEventByDateAndVenue(eventRequest.getEventDate(), eventRequest.getVenue())) {
				return "Already Event booked for selected Venue on the Day ";
			}

			Event event = new Event();
			event.setStudentId(studentId);
			event.setEventName(eventRequest.getName());
			event.setEventTypeId(eventRequest.getEventType());
			event.setVenueId(eventRequest.getVenue());
			event.setEquipment(eventRequest.getEquipment());
			event.setEventDate(eventRequest.getEventDate());
			event.setEventTime(eventRequest.getEventTime());
			event.setPermissionFile(eventRequest.getPermissionFile());

			eventRepository.createEvent(event);

			return null;
		} catch (Exception e) {
			logger.error("Error creating Event", e);
			return "Error creating event, Please Try again with valid values";
		}
	}

	public boolean approveOrRejectEvent(ApproveRequest request) {
		try {
			eventRepository.updateEventStatus(request.getEventId(), request.getStatus());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<EventResponse> getAllEvents() {
		return eventRepository.getAllEvents();
	}

	// helpers attributes for event creation
	public List<EventType> getEventTypes() {
		return eventRepository.getEventTypes();
	}

	public List<Venue> getVenues() {
		return eventRepository.getVenues();
	}

}
