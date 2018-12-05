package com.revature.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Project;
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
	@GetMapping("/")
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
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.OK)
	public Boolean addProject(@RequestBody Project project) {
		return 	projectService.addProject(project);
	}

	// Delete by ID
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Boolean deleteById(@PathVariable String id) {
		return projectService.deleteById(id);
	}

	// Update Project
	@PostMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	public Boolean updateProject(@RequestBody Project project) {
		return projectService.updateProject(project);
	}
	
	// Update Project
		@GetMapping("/{id}")
		@ResponseStatus(HttpStatus.OK)
		public Project updateProject(@PathVariable String id) {
			return projectService.findById(id);
			
		}
}
