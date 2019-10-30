package com.revature.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.ProjectService;

/**
 * This is an integration test whose purpose is to make sure the Server is responding to the most
 * common situations.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProjectController.class }, secure = false)
//@WithMockUser(roles="ADMIN")
public class CreateProjectIntegrationTest {

	private String uri = "/";

	@Autowired
	private MockMvc mockMvc;
	@Mock
	private Project mockProject;
	
	@MockBean
	private ProjectRepository repository;
	
	
	@MockBean
	private ProjectService mockProjectService;
  

  /**
   * This test was created to make sure that the user is going to perform validation for the
   * updateUser method, we provide valid fields for the AppUser, so we are expecting a successful
   * result in this case (OK: 200).
   *
   * @throws Exception
   */
  @Test
  @WithMockUser(roles = {"ADMIN"})
  public void createProject_ShouldReturnCreatedStatus() throws Exception {

    // Setting the mock request
    this.mockMvc
        .perform(
            post(uri)
                .param("name", "name")
                .param("batch", "batch")
                .param("trainer", "trainer")
                .param("groupMembers", new String[] {"m1"})
                .param("screenShots", new String[] {"a"})
                .param("zipLinks", new String[] {"A"})
                .param("description", "description")
                .param("techStack", "techStack")
                .param("status", "status")
                .param("dataModel", new String[] {"A"})
                .param("userId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)) // Set Multipart form data
        .andExpect(MockMvcResultMatchers.status().isCreated()); // expecting Created response
  }

  /**
   * This testing was created to test if the method is able to handle null values. In case a null
   * value is passed to the test this will throw an Exception and returns a bad request.
   *
   * @throws Exception
   */
  @Test
  @WithMockUser(roles = {"ADMIN"})
  public void createProject_ShouldReturnBadRequestStatus() throws Exception {
    String batch = null;

    //
    this.mockMvc
        .perform(
            post(uri)
                .param("name", "name")
                .param("batch", batch)
                .param("trainer", "trainer")
                .param("groupMembers", new String[] {"m1"})
                .param("screenShots", new String[] {"a"})
                .param("zipLinks", new String[] {"A"})
                .param("description", "description")
                .param("techStack", "techStack")
                .param("status", "status")
                .param("dataModel", new String[] {"A"})
                .param("userId", "1")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)) // Set Multipart form data
        .andExpect(MockMvcResultMatchers.status().isBadRequest()); // expecting Created response
  }
}
