package com.moratuwa.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

	private String name;

	private Integer eventType;

	private Date eventDate;

	private Time eventTime;

	private Integer venue;

	private String equipment;

	private String permissionFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Time getEventTime() {
		return eventTime;
	}

	public void setEventTime(Time eventTime) {
		this.eventTime = eventTime;
	}

	public Integer getVenue() {
		return venue;
	}

	public void setVenue(Integer venue) {
		this.venue = venue;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getPermissionFile() {
		return permissionFile;
	}

	public void setPermissionFile(String permissionFile) {
		this.permissionFile = permissionFile;
	}

}