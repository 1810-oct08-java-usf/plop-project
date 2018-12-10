package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectErrorResponse;
import com.revature.service.ProjectService;

@RestController
public class ProjectController {

	private Environment env;

	private ProjectService projectService;

	@Autowired
	public ProjectController(Environment env, ProjectService projectService) {
		this.env = env;
		this.projectService = projectService;
	}

	// Get all projects
	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getSpecifiedProjects(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "batch", required = false) String batch,
			@RequestParam(value = "fullName", required = false) String fullName,
			@RequestParam(value = "techStack", required = false) String techStack,
			@RequestParam(value = "status", required = false) String status) {

		if (name != null) {
			System.out.println(name);
			return projectService.findByName(name);
		} else if (batch != null) {
			return projectService.findByBatch(batch);
		} else if (fullName != null) {
			return projectService.findByFullName(fullName);
		} else if (techStack != null) {
			return projectService.findByTechStack(techStack);
		} else if (status != null) {
			return projectService.findByStatus(status);
		} else {
			return projectService.findAllProjects();
		}

	}

	// Add new project
	@PostMapping("projects/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Boolean addProject(@RequestBody Project project) {
		if (project.getBatch() == null)
			throw new ProjectNotAddedException("The 'batch' input cannot be null when adding project");
		if (project.getName() == null)
			throw new ProjectNotAddedException("The 'name' input cannot be null when adding project");
		if (project.getTechStack() == null)
			throw new ProjectNotAddedException("The 'tech stack' input cannot be null when adding project");
		return projectService.addProject(project);
	}

	// Delete by ID
	@DeleteMapping("projects/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Boolean deleteById(@PathVariable String id) {
		Project ID = projectService.findById(id);
		if (ID == null) {
			throw new ProjectNotFoundException("ID entered cannot be found to delete this project");
		}
		return projectService.deleteById(id);
	}

	// Update Project
	@PutMapping("projects/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Boolean updateProject(@RequestBody Project project, @PathVariable String id) {
		return projectService.updateProject(project, id);
	}

	// Find By ID
	@GetMapping("projects/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Project updateProject(@PathVariable String id) {
		Project ID = projectService.findById(id);
			if (ID == null) {
				throw new ProjectNotFoundException("ID entered cannot be found");
			}
			return projectService.findById(id);
		}
	
	//Exception Handler for Response Status Not found which is  used for findById() [/{id}] & deleteById() [delete/{id}]
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ProjectErrorResponse handleExceptions(ProjectNotFoundException pnfe) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(pnfe.getMessage());
		error.setTimmeStamp(System.currentTimeMillis());
		return error;
	}
	//Exception Handler for Response Status Bad Request which is  used for addProject() [/add]
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ProjectErrorResponse handleExceptions(ProjectNotAddedException pnae) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(pnae.getMessage());
		error.setTimmeStamp(System.currentTimeMillis());
		return error;
	}
}
