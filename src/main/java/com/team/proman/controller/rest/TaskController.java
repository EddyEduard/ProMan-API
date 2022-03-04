package com.team.proman.controller.rest;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.proman.model.TaskModel;
import com.team.proman.model.db.Account;
import com.team.proman.model.db.Project;
import com.team.proman.model.db.Sprint;
import com.team.proman.model.db.Task;
import com.team.proman.model.enums.Status;
import com.team.proman.services.AccountService;
import com.team.proman.services.ProjectService;
import com.team.proman.services.SprintService;
import com.team.proman.services.TaskService;

@RestController
@RequestMapping("/api/task")
public class TaskController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private TaskService taskService;

	/**
	 * Get all tasks.
	 * 
	 * @param projectId
	 * @param authentication
	 * @return task
	 */
	@GetMapping("/get/{projectId}/{sprintId}")
	public ResponseEntity<Object> get(@PathVariable Long projectId, @PathVariable Long sprintId,
			Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			List<Task> tasks = taskService.selectByProjectIdAndSprintId(projectId, sprintId);

			return new ResponseEntity<>(tasks, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get task by id.
	 * 
	 * @param projectId
	 * @param sprintId
	 * @param taskId
	 * @param authentication
	 * @return task
	 */
	@GetMapping("/get/{projectId}/{sprintId}/{taskId}")
	public ResponseEntity<Object> get(@PathVariable Long projectId, @PathVariable Long sprintId,
			@PathVariable Long taskId, Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Project foundProject = projectService.findById(projectId);

			if (foundProject == null)
				return new ResponseEntity<>(new String[] { "There isn't a project with this id." },
						HttpStatus.NOT_FOUND);

			Sprint foundSprint = sprintService.findById(sprintId);

			if (foundSprint == null)
				return new ResponseEntity<>(new String[] { "There isn't a project with this id." },
						HttpStatus.NOT_FOUND);

			Task task = taskService.findByProjectIdAndSprintIdAndTaskId(projectId, sprintId, taskId);

			return new ResponseEntity<>(task, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Create a new task.
	 * 
	 * @param task
	 * @param bindingResult
	 * @param authentication
	 * @return created task
	 */
	@Validated
	@PostMapping("/create")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_MANAGER')")
	public ResponseEntity<Object> create(@Valid @RequestBody TaskModel task, BindingResult bindingResult,
			Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Task createdTask = taskService
					.create(task.getTask(foundAccount.getCompany_id(), Status.TODO, new Date(), new Date()));

			return new ResponseEntity<>(createdTask, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update a task.
	 * 
	 * @param taskId
	 * @param task
	 * @param bindingResult
	 * @param authentication
	 * @return created task
	 */
	@Validated
	@PutMapping("/update/{taskId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_MANAGER', 'ROLE_SPECIALIST')")
	public ResponseEntity<Object> update(@PathVariable Long taskId, @Valid @RequestBody TaskModel task,
			BindingResult bindingResult, Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Task foundTask = taskService.findById(taskId);

			if (foundTask == null)
				return new ResponseEntity<>(new String[] { "There isn't a task with this id." }, HttpStatus.NOT_FOUND);

			Task updatedTask = taskService.update(taskId, task.getTask(foundAccount.getCompany_id(), task.getStatus(),
					foundTask.getCreated_date(), new Date()));

			return new ResponseEntity<>(updatedTask, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete task by id.
	 * 
	 * @param taskId
	 * @param authentication
	 * @return task
	 */
	@DeleteMapping("/delete/{taskId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_MANAGER')")
	public ResponseEntity<Object> delete(@PathVariable Long taskId, Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Task foundTask = taskService.findById(taskId);

			if (foundTask == null)
				return new ResponseEntity<>(new String[] { "There isn't a task with this id." }, HttpStatus.NOT_FOUND);

			taskService.deleteById(taskId);

			return new ResponseEntity<>(new String[] { "Delete task successfully." }, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}
}
