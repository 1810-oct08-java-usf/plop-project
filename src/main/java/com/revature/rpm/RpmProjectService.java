package com.revature.rpm;

import com.revature.rpm.entities.Project;
import com.revature.rpm.repositories.ProjectRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class RpmProjectService implements CommandLineRunner {

  private ProjectRepository projectRepo;

  @Autowired
  public RpmProjectService(ProjectRepository projectRepo) {
    this.projectRepo = projectRepo;
  }

  public static void main(String[] args) {
    SpringApplication.run(RpmProjectService.class, args);
  }

  /**
   * When the application launches, the MongoDB first deletes all entries and then creates and adds
   * 5 new entries.
   */
  @Override
  public void run(String... args) throws Exception {
    projectRepo.deleteAll();

    // Populates embedded MongoDB with mock data for testing and demo purposes
    String p1_name = "Revature Project Manager (RPM)";
    String p1_batch = "1810-Oct08-Java";
    String p1_trainer = "Wezley Singleton";
    int UserId = 1;

    List<String> p1_grpMembers =
        new ArrayList<>(
            Arrays.asList(
                new String[] {
                  "Sadiki", "Paul", "Miles", "Caleb",
                  "Derek", "Sahil", "Shawn", "Yuki",
                  "Jeffly", "Ryan", "Andrew"
                }));

    List<String> p1_screens = new ArrayList<String>();
    p1_screens.add("fake-screen-link");

    List<String> p1_zips = new ArrayList<String>();
    p1_zips.add("fake-zip-link");
    p1_zips.add("fake-zip-link");

    List<String> p1_dataModel = new ArrayList<String>();
    p1_dataModel.add("fake-data-model.sql");

    String p1_desc =
        "The Revature Project Management (RPM) system is a web application that can be used "
            + "to record metadata information regarding completed associate projects. Authenticated users "
            + "can submit project information, which is then approved by an administrator. Using repository "
            + "links provided during project submission, project codebases will be pulled and made available "
            + "for viewing. This application will provide administrators the ability to deploy the application "
            + "onto dynamically allocated cloud resources so that it can be evaluated in the state was in during "
            + "its original presentation.";

    String p1_stack = "Java/J2EE";
    String p1_status = "Approved";

    projectRepo.save(
        new Project.ProjectBuilder()
            .setName(p1_name)
            .setBatch(p1_batch)
            .setTrainer(p1_trainer)
            .setGroupMembers(p1_grpMembers)
            .setScreenShots(p1_screens)
            .setZipLinks(p1_zips)
            .setDataModel(p1_dataModel)
            .setDescription(p1_desc)
            .setTechStack(p1_stack)
            .setStatus(p1_status)
            .setUserId(UserId)
            .build());

    String p2_name = "Cannons and Goblins";
    String p2_batch = "190422-Java-USF";
    String p2_trainer = "Wezley Singleton";
    int userId = 2;

    List<String> p2_grpMembers =
        new ArrayList<>(Arrays.asList(new String[] {"Daniel", "Justin", "Aaron"}));

    List<String> p2_screens = new ArrayList<String>();
    p2_screens.add("fake-screen-link");

    List<String> p2_zips = new ArrayList<String>();
    p2_zips.add("fake-zip-link");
    p2_zips.add("fake-zip-link");

    List<String> p2_dataModel = new ArrayList<String>();
    p2_dataModel.add("fake-data-model.sql");

    String p2_desc =
        "Cannons And Goblins is a simple RPG simulator that can be used for competitive online "
            + "entertainment. Authenticated users can create and delete a multitude of character cards to be "
            + "played in “duels” against other users’ characters through a matchmaking system, as well as to "
            + "fight computer generated monster cards. A ranked score board can be viewed by all users and "
            + "lists the top characters in the system.";

    String p2_stack = "Java/J2EE";
    String p2_status = "Pending";

    projectRepo.save(
        new Project.ProjectBuilder()
            .setName(p2_name)
            .setBatch(p2_batch)
            .setTrainer(p2_trainer)
            .setGroupMembers(p2_grpMembers)
            .setScreenShots(p2_screens)
            .setZipLinks(p2_zips)
            .setDataModel(p2_dataModel)
            .setDescription(p2_desc)
            .setTechStack(p2_stack)
            .setStatus(p2_status)
            .setUserId(userId)
            .build());

    String p3_name = "XChange";
    String p3_batch = "1711-Nov13-Java";
    String p3_trainer = "Genesis Bonds";
    int userid = 1;

    List<String> p3_grpMembers =
        new ArrayList<>(Arrays.asList(new String[] {"Wezley", "Nahom", "Yosef", "Matt"}));

    List<String> p3_screens = new ArrayList<String>();
    p3_screens.add("fake-screen-link");

    List<String> p3_zips = new ArrayList<String>();
    p3_zips.add("fake-zip-link");
    p3_zips.add("fake-zip-link");

    List<String> p3_dataModel = new ArrayList<String>();
    p3_dataModel.add("fake-data-model.sql");

    String p3_desc =
        "XChange is a web application that aims to provide its users with a single place for "
            + "keeping track of financial assets. The first iteration of this application will focus on "
            + "stock assets. Users will be able to view recent news articles on stocks of their choice. Also, "
            + "users will be able to search for stocks, add/remove them from their watchlist, view charts of "
            + "historical price data. Additionally, XChange offers aspects akin to a social media application: "
            + "giving users the ability to comment on an asset, share ideas, and connect with other users.";

    String p3_stack = "Java/J2EE";
    String p3_status = "Approved";

    projectRepo.save(
        new Project.ProjectBuilder()
            .setName(p3_name)
            .setBatch(p3_batch)
            .setTrainer(p3_trainer)
            .setGroupMembers(p3_grpMembers)
            .setScreenShots(p3_screens)
            .setZipLinks(p3_zips)
            .setDataModel(p3_dataModel)
            .setDescription(p3_desc)
            .setTechStack(p3_stack)
            .setStatus(p3_status)
            .setUserId(userid)
            .build());

    String p4_name = "Quizzard";
    String p4_batch = "1805-May14-JavaScript";
    String p4_trainer = "Blake Kruppa";
    int userID = 2;

    List<String> p4_grpMembers =
        new ArrayList<>(Arrays.asList(new String[] {"Terrance", "John", "Nathan", "Kimberly"}));

    List<String> p4_screens = new ArrayList<String>();
    p4_screens.add("fake-screen-link");

    List<String> p4_zips = new ArrayList<String>();
    p4_zips.add("fake-zip-link");
    p4_zips.add("fake-zip-link");

    List<String> p4_dataModel = new ArrayList<String>();
    p4_dataModel.add("fake-data-model.sql");

    String p4_desc =
        "Quizzard is a fully featured quiz web application. Registered users are able to create "
            + "questions and quizzes and are able to test their skills by taking these quizzes. Quizzes have "
            + "tags that allow users to search for quizzes other users have made. Users are awarded points for "
            + "creating and taking quizzes. With these points users will be able to unlock in app features. If "
            + "a user finds a question to be wrong or inappropriate then they can report these questions.";

    String p4_stack = "NERD";
    String p4_status = "Approved";

    projectRepo.save(
        new Project.ProjectBuilder()
            .setName(p4_name)
            .setBatch(p4_batch)
            .setTrainer(p4_trainer)
            .setGroupMembers(p4_grpMembers)
            .setScreenShots(p4_screens)
            .setZipLinks(p4_zips)
            .setDataModel(p4_dataModel)
            .setDescription(p4_desc)
            .setTechStack(p4_stack)
            .setStatus(p4_status)
            .setUserId(userID)
            .build());

    String p5_name = "RideForce";
    String p5_batch = "1808-Aug13-NET";
    String p5_trainer = "Fred Belotte";

    List<String> p5_grpMembers =
        new ArrayList<>(Arrays.asList(new String[] {"Daniel", "Andrew", "Rebecca", "Bradley"}));

    List<String> p5_screens = new ArrayList<String>();
    p5_screens.add("fake-screen-link");

    List<String> p5_zips = new ArrayList<String>();
    p5_zips.add("fake-zip-link");
    p5_zips.add("fake-zip-link");

    List<String> p5_dataModel = new ArrayList<String>();
    p5_dataModel.add("fake-data-model.sql");

    String p5_desc = "An extremely detailed description.";

    String p5_stack = ".NET";
    String p5_status = "Denied";

    projectRepo.save(
        new Project.ProjectBuilder()
            .setName(p5_name)
            .setBatch(p5_batch)
            .setTrainer(p5_trainer)
            .setGroupMembers(p5_grpMembers)
            .setScreenShots(p5_screens)
            .setZipLinks(p5_zips)
            .setDataModel(p5_dataModel)
            .setDescription(p5_desc)
            .setTechStack(p5_stack)
            .setStatus(p5_status)
            .build());

    String p6_name = "Tuition Reimbursement Application";
    String p6_batch = "1905-nick-java";
    String p6_trainer = "Nick Jurczak";

    List<String> p6_grpMembers =
        new ArrayList<>(Arrays.asList(new String[] {"Zak", "Craig", "Rebecca", "Bradley"}));

    List<String> p6_screens = new ArrayList<String>();
    p6_screens.add("fake-screen-link");

    List<String> p6_zips = new ArrayList<String>();
    p6_zips.add("fake-zip-link");
    p6_zips.add("fake-zip-link");

    List<String> p6_dataModel = new ArrayList<String>();
    p6_dataModel.add("fake-data-model.sql");

    String p6_desc =
        "The Tuition Reimbursement System, TRMS, allows users to submit reimbursements for courses and training. The submitted reimbursement must be approved by that employee's supervisor, department head, and benefits coordinator. The benefits coordinator then reviews the grade received before finalizing the reimbursement.";

    String p6_stack = "Spring Boot, Angular, Restful API";
    String p6_status = "Denied";

    projectRepo.save(
        new Project.ProjectBuilder()
            .setName(p6_name)
            .setBatch(p6_batch)
            .setTrainer(p6_trainer)
            .setGroupMembers(p6_grpMembers)
            .setScreenShots(p6_screens)
            .setZipLinks(p6_zips)
            .setDataModel(p6_dataModel)
            .setDescription(p6_desc)
            .setTechStack(p6_stack)
            .setStatus(p6_status)
            .setUserId(UserId)
            .build());
  }
}
