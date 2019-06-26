package com.revature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

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
		String p1_name = "Revature Project Manager (RPM)";
		String p1_batch = "1810-Oct08-Java";
		String p1_trainer = "Wezley Singleton";
	
		List<String> p1_grpMembers = new ArrayList<>(
			Arrays.asList(new String[] {
				"Sadiki", "Paul", "Miles", "Caleb", 
				"Derek", "Sahil", "Shawn", "Yuki", 
				"Jeffly", "Ryan", "Andrew"		
			})
		);
		
		List<String> p1_screens = new ArrayList<String>();
		p1_screens.add("fake-screen-link");
		
		List<String> p1_zips = new ArrayList<String>();
		p1_zips.add("fake-zip-link");
		p1_zips.add("fake-zip-link");
		
		List<String> p1_dataModel= new ArrayList<String>();
		p1_dataModel.add("fake-data-model.sql");
		
		String p1_desc = "The Revature Project Management (RPM) system is a web application that can be used "
				+ "to record metadata information regarding completed associate projects. Authenticated users "
				+ "can submit project information, which is then approved by an administrator. Using repository "
				+ "links provided during project submission, project codebases will be pulled and made available "
				+ "for viewing. This application will provide administrators the ability to deploy the application "
				+ "onto dynamically allocated cloud resources so that it can be evaluated in the state was in during "
				+ "its original presentation.";
		
		String p1_stack = "Java/J2EE";
		String p1_status = "Approved";

		projectRepo.save(new Project(p1_name, p1_batch, p1_trainer, p1_grpMembers, p1_screens, 
				p1_zips, p1_dataModel, p1_desc, p1_stack, p1_status));

		String p2_name = "Cannons and Goblins";
		String p2_batch = "190422-Java-USF";
		String p2_trainer = "Wezley Singleton";
	
		List<String> p2_grpMembers = new ArrayList<>(Arrays.asList(new String[] {"Daniel", "Justin", "Aaron"}));
		
		List<String> p2_screens = new ArrayList<String>();
		p2_screens.add("fake-screen-link");
		
		List<String> p2_zips = new ArrayList<String>();
		p2_zips.add("fake-zip-link");
		p2_zips.add("fake-zip-link");
		
		List<String> p2_dataModel= new ArrayList<String>();
		p2_dataModel.add("fake-data-model.sql");
		
		String p2_desc = "Cannons And Goblins is a simple RPG simulator that can be used for competitive online "
				+ "entertainment. Authenticated users can create and delete a multitude of character cards to be "
				+ "played in “duels” against other users’ characters through a matchmaking system, as well as to "
				+ "fight computer generated monster cards. A ranked score board can be viewed by all users and "
				+ "lists the top characters in the system.";
		
		String p2_stack = "Java/J2EE";
		String p2_status = "Pending";
		
		projectRepo.save(new Project(p2_name, p2_batch, p2_trainer, p2_grpMembers, p2_screens, 
				p2_zips, p2_dataModel, p2_desc, p2_stack, p2_status));
		
		String p3_name = "XChange";
		String p3_batch = "1711-Nov13-Java";
		String p3_trainer = "Genesis Bonds";
	
		List<String> p3_grpMembers = new ArrayList<>(Arrays.asList(new String[] {"Wezley", "Nahom", "Yosef", "Matt"}));
		
		List<String> p3_screens = new ArrayList<String>();
		p3_screens.add("fake-screen-link");
		
		List<String> p3_zips = new ArrayList<String>();
		p3_zips.add("fake-zip-link");
		p3_zips.add("fake-zip-link");
		
		List<String> p3_dataModel= new ArrayList<String>();
		p3_dataModel.add("fake-data-model.sql");
		
		String p3_desc = "XChange is a web application that aims to provide its users with a single place for "
				+ "keeping track of financial assets. The first iteration of this application will focus on "
				+ "stock assets. Users will be able to view recent news articles on stocks of their choice. Also, "
				+ "users will be able to search for stocks, add/remove them from their watchlist, view charts of "
				+ "historical price data. Additionally, XChange offers aspects akin to a social media application: "
				+ "giving users the ability to comment on an asset, share ideas, and connect with other users.";
		
		String p3_stack = "Java/J2EE";
		String p3_status = "Approved";

		projectRepo.save(new Project(p3_name, p3_batch, p3_trainer, p3_grpMembers, p3_screens, 
				p3_zips, p3_dataModel, p3_desc, p3_stack, p3_status));
		
		String p4_name = "Quizzard";
		String p4_batch = "1805-May14-JavaScript";
		String p4_trainer = "Blake Kruppa";
	
		List<String> p4_grpMembers = new ArrayList<>(Arrays.asList(new String[] {"Terrance", "John", "Nathan", "Kimberly"}));
		
		List<String> p4_screens = new ArrayList<String>();
		p4_screens.add("fake-screen-link");
		
		List<String> p4_zips = new ArrayList<String>();
		p4_zips.add("fake-zip-link");
		p4_zips.add("fake-zip-link");
		
		List<String> p4_dataModel= new ArrayList<String>();
		p4_dataModel.add("fake-data-model.sql");
		
		String p4_desc = "Quizzard is a fully featured quiz web application. Registered users are able to create "
				+ "questions and quizzes and are able to test their skills by taking these quizzes. Quizzes have "
				+ "tags that allow users to search for quizzes other users have made. Users are awarded points for "
				+ "creating and taking quizzes. With these points users will be able to unlock in app features. If "
				+ "a user finds a question to be wrong or inappropriate then they can report these questions.";
		
		String p4_stack = "NERD";
		String p4_status = "Approved";

		projectRepo.save(new Project(p4_name, p4_batch, p4_trainer, p4_grpMembers, p4_screens, 
				p4_zips, p4_dataModel, p4_desc, p4_stack, p4_status));
		
		
		String p5_name = "RideForce";
		String p5_batch = "1808-Aug13-NET";
		String p5_trainer = "Fred Belotte";
	
		List<String> p5_grpMembers = new ArrayList<>(Arrays.asList(new String[] {"Daniel", "Andrew", "Rebecca", "Bradley"}));
		
		List<String> p5_screens = new ArrayList<String>();
		p5_screens.add("fake-screen-link");
		
		List<String> p5_zips = new ArrayList<String>();
		p5_zips.add("fake-zip-link");
		p5_zips.add("fake-zip-link");
		
		List<String> p5_dataModel= new ArrayList<String>();
		p5_dataModel.add("fake-data-model.sql");
		
		String p5_desc = "An extremely detailed description.";
		
		String p5_stack = ".NET";
		String p5_status = "Denied";

		projectRepo.save(new Project(p5_name, p5_batch, p5_trainer, p5_grpMembers, p5_screens, 
				p5_zips, p5_dataModel, p5_desc, p5_stack, p5_status));
		
	}
	
}
