package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;

@RestController
public class ProjectController {

	private Environment env;

	private ProjectService projectService;

	@Autowired
	public ProjectController(Environment env, ProjectService projectService) {
		this.env = env;
		this.projectService = projectService;
	}
	
	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Project> getAllProjects() {
        return projectService.findAllProjects();
    }
	
    @GetMapping(value="/id/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Project getProjectById(@PathVariable String id) {
        return projectService.findById(id);
    }
    
    @GetMapping(value="/name/{name}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Project> getProjectsByName(@PathVariable String name) {
        return projectService.findByName(name);
    }
    
    @GetMapping(value="/batch/{batch}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Project> getProjectsByBatch(@PathVariable String batch) {
        return projectService.findByBatch(batch);
    }
    
    @GetMapping(value="/status/{status}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Project> getProjectsByStatus(@PathVariable String status) {
        return projectService.findByStatus(status);
    }

	// Add new project
	
	@PostMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Project addProject(
		@RequestParam String name,
		@RequestParam String batch,
		// TODO should be retrieved from auth service
		@RequestParam String trainer,
		@RequestParam List<String> groupMembers,
		@RequestParam List<MultipartFile> screenShots,
		@RequestParam List<String> zipLinks,
		@RequestParam String description,
		@RequestParam String techStack,
		@RequestParam String status
	) {
		ProjectDTO projectDTO = new ProjectDTO();

		projectDTO.setName(name);
		projectDTO.setBatch(batch);
		projectDTO.setTrainer(trainer);
		projectDTO.setScreenShots(screenShots);
		projectDTO.setGroupMembers(groupMembers);
		projectDTO.setZipLinks(zipLinks);
		projectDTO.setDescription(description);
		projectDTO.setTechStack(techStack);
		projectDTO.setStatus(status);

		if (projectDTO.getBatch() == null)
			throw new ProjectNotAddedException("The 'batch' input cannot be null when adding project");
		if (projectDTO.getName() == null)
			throw new ProjectNotAddedException("The 'name' input cannot be null when adding project");
		if (projectDTO.getTechStack() == null)
			throw new ProjectNotAddedException("The 'tech stack' input cannot be null when adding project");

		return projectService.createProjectFromDTO(projectDTO);
	}

	// Delete by ID
	@DeleteMapping(value="/id/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Boolean deleteById(@PathVariable String id) {
		Project ID = projectService.findById(id);
		if (ID == null) {
			throw new ProjectNotFoundException("ID entered cannot be found to delete this project");
		}
		return projectService.deleteById(id);
	}

	// TODO should let you update screenshots and repositories
	// Update Project
	@PutMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Boolean updateProject(@RequestBody Project project, @PathVariable String id) {
		Project ID = projectService.findById(id);
		if (ID == null) {
			throw new ProjectNotFoundException("ID entered cannot be found to complete update.");
		}
		return projectService.updateProject(project, id);
	}

	// Exception Handler for Response Status Not found which is used for findById()
	// [/{id}] & deleteById() [delete/{id}]
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ProjectErrorResponse handleExceptions(ProjectNotFoundException pnfe) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(pnfe.getMessage());
		error.setTimmeStamp(System.currentTimeMillis());
		return error;
	}

	// Exception Handler for Response Status Bad Request which is used for
	// addProject() [/add]
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
