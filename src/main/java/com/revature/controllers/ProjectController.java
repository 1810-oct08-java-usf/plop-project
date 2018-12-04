package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;

@RestController
public class ProjectController {

	private Environment env;
	
	private ProjectRepository projectRepo;

	@Autowired
	public ProjectController(Environment env, ProjectRepository projectRepo) {
		this.env = env;
		this.projectRepo = projectRepo;
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getProjects(){
		// Get all projects
				System.out.println("Projects found with findAll()");
				System.out.println("-------------------------------");
				System.out.println(projectRepo.findAll());
				return projectRepo.findAll();
	}
}
