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

import com.team.proman.model.ProjectModel;
import com.team.proman.model.db.Account;
import com.team.proman.model.db.Project;
import com.team.proman.model.db.Sprint;
import com.team.proman.model.db.Task;
import com.team.proman.services.AccountService;
import com.team.proman.services.ProjectService;
import com.team.proman.services.SprintService;
import com.team.proman.services.TaskService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private TaskService taskService;

	/**
	 * Get all projects.
	 * 
	 * @param authentication
	 * @return project
	 */
	@GetMapping("/get")
	public ResponseEntity<Object> get(Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			List<Project> projects = projectService.selectByCompanyId(foundAccount.getCompany_id());

			return new ResponseEntity<>(projects, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get project by id.
	 * 
	 * @param projectId
	 * @param authentication
	 * @return project
	 */
	@GetMapping("/get/{projectId}")
	public ResponseEntity<Object> get(@PathVariable Long projectId, Authentication authentication) {
		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Project project = projectService.findById(projectId);

			return new ResponseEntity<>(project, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Create a new project.
	 * 
	 * @param project
	 * @param bindingResult
	 * @param authentication
	 * @return created project
	 */
	@Validated
	@PostMapping("/create")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_MANAGER')")
	public ResponseEntity<Object> create(@Valid @RequestBody ProjectModel project, BindingResult bindingResult,
			Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

		Long id = Long.parseLong(authentication.getName());

		try {
			Account foundAccount = accountService.findById(id);

			if (foundAccount == null)
				return new ResponseEntity<>(new String[] { "There isn't an account with this id." },
						HttpStatus.NOT_FOUND);

			Project createdProject = projectService
					.create(project.getProject(foundAccount.getCompany_id(), new Date(), new Date()));

			return new ResponseEntity<>(createdProject, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Update a project.
	 * 
	 * @param projectId
	 * @param project
	 * @param bindingResult
	 * @param authentication
	 * @return created project
	 */
	@Validated
	@PutMapping("/update/{projectId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_MANAGER')")
	public ResponseEntity<Object> update(@PathVariable Long projectId, @Valid @RequestBody ProjectModel project,
			BindingResult bindingResult, Authentication authentication) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);

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

			Project updatedProject = projectService.update(projectId,
					project.getProject(foundAccount.getCompany_id(), foundProject.getCreated_date(), new Date()));

			return new ResponseEntity<>(updatedProject, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete project by id.
	 * 
	 * @param projectId
	 * @param authentication
	 * @return project
	 */
	@DeleteMapping("/delete/{projectId}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_MANAGER')")
	public ResponseEntity<Object> delete(@PathVariable Long projectId, Authentication authentication) {
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

			List<Sprint> foundSprints = sprintService.selectByProjectId(foundProject.getId());

			for (Sprint sprint : foundSprints) {
				List<Task> foundTasks = taskService.selectByProjectIdAndSprintId(foundProject.getId(), sprint.getId());

				for (Task task : foundTasks)
					taskService.deleteById(task.getId());

				sprintService.deleteById(sprint.getId());
			}

			projectService.deleteById(projectId);

			return new ResponseEntity<>(new String[] { "Delete project successfully." }, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}
	}
}
