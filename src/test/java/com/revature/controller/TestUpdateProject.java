package com.revature.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.controllers.ProjectController;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.ProjectService;

/**
 * Class containing MockMVC tests for the update project method in the project
 * controller.
 * 
 * @author Jose Rivera (190107-Java-Spark-USF)
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProjectController.class }, secure = false)
public class TestUpdateProject {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private Project mockProject;

	@MockBean
	private ProjectService mockProjectService;

	@MockBean
	private ProjectRepository mockProjectRepository;

	private static final String URI = "/";

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	/**
	 * This method is going to test if our context loads and is not null.
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}

	/**
	 * This method will test updating a project through the controller. The
	 * controller will accept a PUT request with an id to update the proper project.
	 * The project that will be updated will be passed through the request body.
	 * 
	 * @throws Exception: If the test fails, an exception will be thrown.
	 * 
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 * @author Jaitee Pitts (190107-Java-Spark-USF)
	 */
	@Test
	public void testUpdateProject() throws Exception {

		// An id to use to check for a project
		String id = "1";

		// The expected result from the controller
		String expectedResult = "true";

		/*
		 * When our ProjectService.findById() is invoked, we tell it to return a mock
		 * project and a mock status so our get request can return a proper result 
		 * as if it was not mocked.
		 */
		when(mockProjectService.findById(id)).thenReturn(mockProject);
		when(mockProject.getStatus()).thenReturn("pending");
		

		/*
		 * Create a new Project and convert it into JSON to pass as the request body using Object
		 * Mapper. Currently produces an error.
		 */
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		Project proj = new Project("name","batch","trainer", new ArrayList<String>(), new ArrayList<String>(),new ArrayList<String>(),"description","techstack","approved");
		String requestJson = ow.writeValueAsString(proj);
		
		//when it calls the service return true
		when(mockProjectService.updateProject(proj, id)).thenReturn(true);

		/*
		 * Test our PUT mapping for updateProject() and check if the status is OK ( 200
		 * ) and the expected result is returned.
		 */
		this.mockMvc.perform(put(URI + id).contentType( MediaType.APPLICATION_JSON_VALUE).content(requestJson))
				.andExpect(status().isOk()).andExpect(content().string(expectedResult));

	}
}
