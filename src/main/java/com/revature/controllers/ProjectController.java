package com.revature.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.AppUser;
import com.revature.models.Project;

@RestController
@RequestMapping("/")
public class ProjectController {

	private Environment env;

	public ProjectController(Environment env) {
		this.env = env;
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<Project> getProjects(){
		
		List<AppUser> trainer = new ArrayList<AppUser>();
		trainer.add(new AppUser(1, "omar", "Unimportant", "USER"));
		trainer.add(new AppUser(2, "admin", "Unimportant", "ADMIN"));
		List<String> groupMembers = new ArrayList<String>();
		groupMembers.add("Sadiki");
		groupMembers.add("Paul");
		groupMembers.add("Miles");
		groupMembers.add("Caleb");
		groupMembers.add("Derek");
		List<String> screenShots = new ArrayList<String>();
		screenShots.add("!");
		List<String> zipLinks = new ArrayList<String>();
		zipLinks.add("....///");
		zipLinks.add("//.....");
		return Arrays.asList(new Project("RPM","Bunker Batch",
				trainer,groupMembers,screenShots, zipLinks, "DESCRPTION","Java/J2EE", 1));
	}
}
