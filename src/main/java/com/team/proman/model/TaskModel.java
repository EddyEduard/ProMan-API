package com.team.proman.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.team.proman.model.db.Task;
import com.team.proman.model.enums.Priority;
import com.team.proman.model.enums.Status;

public class TaskModel {
	private Long projectId;
	
	private Long sprintId;

	@NotBlank(message = "The name is required.")
	@NotNull(message = "The name cannot be null.")
	@NotEmpty(message = "The name cannot be empty.")
	@Size(min = 6, max = 50, message = "The length of name must be between 6 and 250 characters.")
	private String name;

	private String description;

	private Priority priority;

	private Status status;

	public Task getTask(Long companyId, Status status, Date created_date, Date updated_date) {
		return new Task(companyId, this.sprintId, this.projectId, this.name, this.description, this.priority, status, created_date,
				updated_date);
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the sprintId
	 */
	public Long getSprintId() {
		return sprintId;
	}

	/**
	 * @param sprintId the sprintId to set
	 */
	public void setSprintId(Long sprintId) {
		this.sprintId = sprintId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
}
