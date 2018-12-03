package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Project;
import com.revature.repositorys.ProjectRepository;

@RestController
@RequestMapping("/")
public class ProjectController {

	private Environment env;
	private ProjectRepository projectRepo;

	@Autowired
	public ProjectController(Environment env, ProjectRepository projectRepo) {
		this.env = env;
		this.projectRepo = projectRepo;
	}

//	@GetMapping("/projects")
//	@ResponseStatus(HttpStatus.OK)
//	public List<Project> getProjects(){
//		
//		List<AppUser> trainer = new ArrayList<AppUser>();
//		trainer.add(new AppUser(1, "omar", "Unimportant", "USER"));
//		trainer.add(new AppUser(2, "admin", "Unimportant", "ADMIN"));
//		List<String> groupMembers = new ArrayList<String>();
//		groupMembers.add("Sadiki");
//		groupMembers.add("Paul");
//		groupMembers.add("Miles");
//		groupMembers.add("Caleb");
//		groupMembers.add("Derek");
//		List<String> screenShots = new ArrayList<String>();
//		screenShots.add("!");
//		List<String> zipLinks = new ArrayList<String>();
//		zipLinks.add("....///");
//		zipLinks.add("//.....");
//		return Arrays.asList(new Project("RPM","Bunker Batch",
//				trainer,groupMembers,screenShots, zipLinks, "DESCRPTION","Java/J2EE", 1));
//	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getAllProjects() {
		// Get all projects
		System.out.println("Projects found with findAll()");
		System.out.println("-------------------------------");
		for (Project project : projectRepo.findAll()) {
			System.out.println("Projects: " + project);
		}
		System.out.println(projectRepo.findAll());
		return projectRepo.findAll();
	}

//	@GetMapping("/projects")
//	@ResponseStatus(HttpStatus.OK)
//	public Project getThisProject() {
//		return projectRepo.
//	}
//	
//	@RequestMapping(value="/{id}", method= RequestMethod.GET)
//	public Project getProjectById(@PathVariable("id") ObjectId id) {
//		return projectRepo.findById(id);
//	}
}
