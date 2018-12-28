package com.revature;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;

@EnableEurekaClient
@SpringBootApplication
public class ProjectServiceApplication implements CommandLineRunner {

	private ProjectRepository projectRepo;

	@Autowired
	public ProjectServiceApplication(ProjectRepository projectRepo) {
		this.projectRepo = projectRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProjectServiceApplication.class, args);
	}

	/*
	 * When the application launches, the MongoDB first deletes all entries and then
	 * creates and adds 5 new entries.
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 */
	@Override
	public void run(String... args) throws Exception {
		projectRepo.deleteAll();

		// Populates embedded MongoDB with mock data for testing and demo purposes
		List<String> group1 = new ArrayList<String>();
		group1.add("Sadiki");
		group1.add("Paul");
		group1.add("Miles");
		group1.add("Caleb");
		group1.add("Derek");
		group1.add("Sahil");
		group1.add("Shawn");
		group1.add("Yuki");
		group1.add("Jeffly");
		group1.add("Ryan");
		group1.add("Andrew");
		List<String> screenShots = new ArrayList<String>();
		screenShots.add("");
		List<String> zipLinks = new ArrayList<String>();
		zipLinks.add("");
		zipLinks.add("");

		// Save projects into repository
		projectRepo.save(new Project("Revature Package Manager (RPM)", "1810-oct08-java-usf", "Wezley Singleton",
				group1, screenShots, zipLinks, "An extremely detailed description", "Java/J2EE", "Approved"));

		List<String> group2 = new ArrayList<String>();
		group2.add("Barry");
		group2.add("Larry");
		group2.add("Cherry");
		group2.add("Harry");
		group2.add("Kerry");
		group2.add("Clarry");
		group2.add("Sherry");
		group2.add("Jerry");
		group2.add("Lorry");
		group2.add("Perry");
		group2.add("Terry");

		// Save projects into repository
		projectRepo.save(new Project("Dashboard 4.0", "1540-nov13-pega-usf", "August Duet", group2, screenShots,
				zipLinks, "An extremely detailed description", "PEGA", "Pending"));

		List<String> group3 = new ArrayList<String>();
		group3.add("Erin");
		group3.add("Henry");
		group3.add("Aaron");
		group3.add("Bucky");
		group3.add("Becky");
		group3.add("Sarah");
		group3.add("Beth");
		group3.add("Joe");
		group3.add("Vanessa");

		// Save projects into repository
		projectRepo.save(new Project("ASAP", "1630-mar4-javascriptmvc-usf", "Trevin Chester", group3, screenShots,
				zipLinks, "An extremely detailed description", "Javascript MVC", "Pending"));

		List<String> group4 = new ArrayList<String>();
		group4.add("Bartholomew");
		group4.add("Jessica");
		group4.add("Viktor");
		group4.add("Elliot");
		group4.add("Venus");
		group4.add("Ashley");
		group4.add("Beth");
		group4.add("Mike");
		group4.add("Matthew");

		// Save projects into repository
		projectRepo.save(new Project("Business Analytics", "1900-june30-net-usf", "Genesis Bond", group4, screenShots,
				zipLinks, "An extremely detailed description", ".Net", "Denied"));

		List<String> group5 = new ArrayList<String>();
		group5.add("Jacob");
		group5.add("Liam");
		group5.add("Samantha");
		group5.add("Alexandra");
		group5.add("Apollo");
		group5.add("Zues");
		group5.add("Persephone");
		group5.add("Hades");
		group5.add("Poseidon");

		// Save projects into repository
		projectRepo.save(new Project("Quizzing Game", "1900-Feb-19-net-usf", "Blake Kruppa", group5, screenShots,
				zipLinks, "An extremely detailed description", "React.js", "Approved"));

		// Get all projects
		System.out.println("Projects found with findAll()");
		System.out.println("-------------------------------");
		for (Project project : projectRepo.findAll()) {
			System.out.println(project);
		}

	}
	/*
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addExposedHeader("Authorization");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
	*/
}
