package com.revature.unitTests.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.revature.rpm.controllers.ProjectController;
import com.revature.rpm.entities.Project;
import com.revature.rpm.repositories.ProjectRepository;
import com.revature.rpm.services.ProjectService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This class is responsible for testing the getProjectsByBatch() method in the
 * ProjectController.class
 */
@RunWith(SpringRunner.class)
@WebMvcTest(
    controllers = {ProjectController.class},
    secure = false)
public class TestGetProjectsByBatch {

  @Autowired private MockMvc mockMvc;

  @Mock private Project mockProject;

  @MockBean private ProjectService mockProjectService;

  @MockBean private ProjectRepository projectRepository;

  @Rule public ExpectedException exceptionRule = ExpectedException.none();

  /**
   * This method is going to test if our context loads and is not null.
   *
   * @throws Exception: If the context fails to load or is null, an exception will be thrown.
   */
  @Test
  public void testContextLoads() throws Exception {
    assertThat(this.mockMvc).isNotNull();
  }

  // /**
  //  * This method is going to test if projects will be returned by
  //  * getProjectsByBatch() if batch name is valid.
  //  *
  //  * @throws Exception
  //  */
  // @Test
  // public void testGetProjectsByBatchIfBatchNameIsValid() throws Exception {

  //   String uri = "/batch/";
  //   String pathVariable = "190422-Java-USF";

  //   String p2_name = "Cannons and Goblins";
  //   String p2_batch = "190422-Java-USF";
  //   String p2_trainer = "Wezley Singleton";

  //   List<String> p2_grpMembers = new ArrayList<>(Arrays.asList(new String[] { "Daniel", "Justin",
  // "Aaron" }));

  //   List<String> p2_screens = new ArrayList<String>();
  //   p2_screens.add("fake-screen-link");

  //   List<String> p2_zips = new ArrayList<String>();
  //   p2_zips.add("fake-zip-link");
  //   p2_zips.add("fake-zip-link");

  //   List<String> p2_dataModel = new ArrayList<String>();
  //   p2_dataModel.add("fake-data-model.sql");

  //   String p2_desc = "Cannons And Goblins is a simple RPG simulator that can be used for
  // competitive online "
  //       + "entertainment. Authenticated users can create and delete a multitude of character
  // cards to be "
  //       + "played in “duels” against other users’ characters through a matchmaking system, as
  // well as to "
  //       + "fight computer generated monster cards. A ranked score board can be viewed by all
  // users and "
  //       + "lists the top characters in the system.";

  //   String p2_stack = "Java/J2EE";
  //   String p2_status = "Pending";

  //   Project cannons = new
  // Project.ProjectBuilder().setName(p2_name).setBatch(p2_batch).setTrainer(p2_trainer)
  //
  // .setGroupMembers(p2_grpMembers).setScreenShots(p2_screens).setZipLinks(p2_zips).setDataModel(p2_dataModel)
  //       .setDescription(p2_desc).setTechStack(p2_stack).setStatus(p2_status).build();

  //   List<Project> projects = new ArrayList<>();
  //   projects.add(cannons);
  //   when(mockProjectService.findByBatch(pathVariable)).thenReturn(projects);

  //   this.mockMvc.perform(get(uri + pathVariable)).andExpect(status().isOk())
  //       .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  // }

  // /**
  //  * This method is going to test if projects will not be returned by
  //  * getProjectsByBatch() if batch name is invalid.
  //  *
  //  * @throws Exception
  //  */
  // @Test
  // public void testGetProjectsByBatchIfBatchNameIsNotValid() throws Exception {

  //   String uri = "/batch/";
  //   String pathVariable = "190107-javascript-sparky-uga";

  //   when(mockProjectService.findByBatch(pathVariable)).thenReturn(null);

  //   mockMvc.perform(get(uri + pathVariable)).andExpect(status().isNotFound());
  // }

  /**
   * This method is going to test if projects will not be returned by getProjectsByBatch() if batch
   * name is empty.
   *
   * @throws Exception
   */
  @Test
  public void testGetProjectByBatchIfBatchNameIsEmpty() throws Exception {

    String uri = "/batch/";

    this.mockMvc.perform(get(uri)).andExpect(status().isMethodNotAllowed());
  }
}
