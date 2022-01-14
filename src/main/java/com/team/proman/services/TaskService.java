package com.team.proman.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.team.proman.model.db.Task;
import com.team.proman.repositories.TaskRepository;

@Service("taskService")
public class TaskService {
	private final TaskRepository taskRepository;

	/**
	 * @param taskRepository
	 */
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	/**
	 * Select tasks by project id.
	 * 
	 * @param projectId
	 * @return selected tasks
	 */
	public List<Task> selectByProjectIdAndSprintId(Long projectId, Long sprintId) {
		List<Task> selectTasks = new ArrayList<Task>();
		Iterator<Task> tasks = this.taskRepository.findAll().iterator();

		while (tasks.hasNext()) {
			Task task = tasks.next();
			if (task.getProject_id().equals(projectId) && task.getSprint_id().equals(sprintId)) {
				selectTasks.add(task);
			}
		}

		return selectTasks;
	}

	/**
	 * Find a task by project id and his id.
	 * 
	 * @param projectId
	 * @param sprintId
	 * @param taskId
	 * @return selected tasks
	 */
	public Task findByProjectIdAndSprintIdAndTaskId(Long projectId, Long sprintId, Long taskId) {
		Iterator<Task> tasks = this.taskRepository.findAll().iterator();

		while (tasks.hasNext()) {
			Task task = tasks.next();
			if (task.getProject_id().equals(projectId) && task.getSprint_id().equals(sprintId)
					&& task.getId().equals(taskId)) {
				return task;
			}
		}

		return null;
	}

	/**
	 * Find a task by id.
	 * 
	 * @param id
	 * @return found task
	 */
	public Task findById(Long id) {
		return taskRepository.findById(id).get();
	}

	/**
	 * Create a new task.
	 * 
	 * @param task
	 * @return created task
	 */
	public Task create(Task task) {
		return this.taskRepository.save(task);
	}

	/**
	 * Update a task by id.
	 * 
	 * @param id
	 * @param task
	 * @return updated task
	 */
	public Task update(Long id, Task task) {
		Task foundTask = findById(id);
		foundTask.setName(task.getName());
		foundTask.setDescription(task.getDescription());
		foundTask.setPriority(task.getPriority());
		foundTask.setStatus(task.getStatus());
		foundTask.setCreated_date(task.getCreated_date());
		foundTask.setUpdated_date(task.getUpdated_date());

		return taskRepository.save(foundTask);
	}

	/**
	 * Delete a task by id.
	 * 
	 * @param id
	 */
	public void deleteById(Long id) {
		Iterator<Task> tasks = taskRepository.findAll().iterator();

		while (tasks.hasNext()) {
			Task task = tasks.next();
			if (task.getId().equals(id)) {
				taskRepository.delete(task);
			}
		}
	}
}
