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

import com.team.proman.model.SprintModel;
import com.team.proman.model.db.Account;
import com.team.proman.model.db.Project;
import com.team.proman.model.db.Sprint;
import com.team.proman.model.enums.Status;
import com.team.proman.services.AccountService;
import com.team.proman.services.ProjectService;
import com.team.proman.services.SprintService;

@RestController
@RequestMapping("/api/sprint")
public class SprintController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private SprintService sprintService;

	/**
	 * Get all sprints.
	 * 
	 * @param projectId
	 * @param authentication
	 * @return sprint
	 */
	@GetMapping("/get/{projectId}")
	public ResponseEntity<Object> get(@PathVariable Long projectId, Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);

			List<Sprint> sprints = sprintService.selectByProjectId(projectId);

			return new ResponseEntity<>(sprints, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get sprint by id.
	 * 
	 * @param projectId
	 * @param sprintId
	 * @param authentication
	 * @return sprint
	 */
	@GetMapping("/get/{projectId}/{sprintId}")
	public ResponseEntity<Object> get(@PathVariable Long projectId, @PathVariable Long sprintId,
			Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);
			
			Project foundProject = projectService.findById(projectId);

			if (foundProject == null)
				return new ResponseEntity<>("There isn't a project with this id.", HttpStatus.NOT_FOUND);

			Sprint sprint = sprintService.findByProjectIdAndSprintId(projectId, sprintId);

			return new ResponseEntity<>(sprint, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Create a new sprint.
	 * 
	 * @param sprint
	 * @param bindingResult
	 * @param authentication
	 * @return created sprint
	 */
	@Validated
	@PostMapping("/create")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> create(@Valid @RequestBody SprintModel sprint, BindingResult bindingResult,
			Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);

			Sprint createdSprint = sprintService
					.create(sprint.getSprint(foundAccount.getCompany_id(), Status.CREATED, new Date(), new Date()));

			return new ResponseEntity<>(createdSprint, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update a sprint.
	 * 
	 * @param sprintId
	 * @param sprint
	 * @param bindingResult
	 * @param authentication
	 * @return created sprint
	 */
	@Validated
	@PutMapping("/update/{sprintId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> update(@PathVariable Long sprintId, @Valid @RequestBody SprintModel sprint,
			BindingResult bindingResult, Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);

			Sprint foundSprint = sprintService.findById(sprintId);

			if (foundSprint == null)
				return new ResponseEntity<>("There isn't a sprint with this id.", HttpStatus.NOT_FOUND);

			Sprint updatedSprint = sprintService.update(sprintId, sprint.getSprint(foundAccount.getCompany_id(),
					sprint.getStatus(), foundSprint.getCreated_date(), new Date()));

			return new ResponseEntity<>(updatedSprint, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete sprint by id.
	 * 
	 * @param sprintId
	 * @param authentication
	 * @return sprint
	 */
	@DeleteMapping("/delete/{sprintId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> delete(@PathVariable Long sprintId, Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>("There isn't an account with this id.", HttpStatus.NOT_FOUND);

			Sprint foundSprint = sprintService.findById(sprintId);

			if (foundSprint == null)
				return new ResponseEntity<>("There isn't a sprint with this id.", HttpStatus.NOT_FOUND);

			sprintService.deleteById(sprintId);

			return new ResponseEntity<>("Delete sprint successfully.", HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}
}
