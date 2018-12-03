package com.revature;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.revature.models.AppUser;
import com.revature.models.Project;
import com.revature.repositorys.ProjectRepository;

@EnableEurekaClient
@SpringBootApplication
public class ProjectServiceApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(ProjectServiceApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		projectRepo.deleteAll();
//		
//		List<AppUser> trainer = new ArrayList<AppUser>();
//		trainer.add(new AppUser(1, "backend", "Unimportant", "ADMIN"));
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
//		
//		// Save projects into repository
//		projectRepo.save(new Project("RPM","Bunker Batch",
//				trainer,groupMembers,screenShots, zipLinks, "DESCRiPTION","Java/J2EE", 1));
//		
//		
//		List<AppUser> trainer2 = new ArrayList<AppUser>();
//		trainer2.add(new AppUser(1, "frontend", "Unimportant", "ADMIN"));
//		List<String> groupMembers2 = new ArrayList<String>();
//		groupMembers.add("Sahill");
//		groupMembers.add("Shawn");
//		groupMembers.add("Yuki");
//		groupMembers.add("Jeffly");
//		groupMembers.add("Ryan");
//		groupMembers.add("Andrew");
//		// Save projects into repository
//		projectRepo.save(new Project("RPM","Bunker Batch",
//				trainer2,groupMembers2,screenShots, zipLinks, "DESCRiPTION","Java/J2EE", 2));
//		
////		
////		// Get all projects
////		System.out.println("Projects found with findAll()");
////		System.out.println("-------------------------------");
////		for(Project project : projectRepo.findAll()) {
////			System.out.println(project);
////		}
////		System.out.println();
////		
////		// Get individual project
////		System.out.println("Project with the name backend");
////		System.out.println("-------------------------------");
////		System.out.println(projectRepo.findByName("backend"));
////		
////		// Get all in the same batch
////		System.out.println("Projects from the same batch");
////		System.out.println("-------------------------------");
////		for(Project project : projectRepo.findByBatch("Bunker Batch")) {
////			System.out.println(project);
////		}
//	
//	}
}
