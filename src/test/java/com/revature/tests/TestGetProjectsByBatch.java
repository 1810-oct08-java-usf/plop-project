package com.revature.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.controllers.ProjectController;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.ProjectService;

/**
 *This class is responsible for testing the getProjectsByBatch() method in
 *the ProjectController.class
 * 
 * @author Christopher Shanor (190107-java-spark-usf)
 * @author Jose Rivera (190107-java-spark-usf)
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProjectController.class }, secure = false)
public class TestGetProjectsByBatch {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private Project mockProject;
	
	@MockBean
	private ProjectService mockProjectService;
	
	@MockBean
	private ProjectRepository projectRepository;
	
	/**
	 * This method is going to test if our context loads and is not null.
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * 
	 * @author Christopher Shanor (190107-Java-Spark-USF)
	 * @author Jose Rivera (190107-Java-Spark-USF)
	 */
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}
	
	/**
	 * This method is going to test if projects will be returned by getProjectsByBatch() if batch name is valid.
	 *
	 * @throws Exception
	 * 
	 * @author Christopher Shanor (190107-java-spark-usf)
	 * @author Jose Rivera (190107-java-spark-usf)
	 * 
	 */
	@Test
	public void testGetProjectsByBatchIfBatchNameIsValid() throws Exception {
		
		String uri = "/batch/";
		String pathVariable = "190107-java-spark-usf";
		
		List<Project> projects = new ArrayList<>();
		when(mockProjectService.findByBatch(pathVariable)).thenReturn(projects);
		
		this.mockMvc.perform(get(uri + pathVariable)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
		
	}
	
	/**
	 * This method is going to test if projects will not be returned by getProjectsByBatch() if batch name is invalid.
	 *
	 * @throws Exception
	 * 
	 * @author Christopher Shanor (190107-java-spark-usf)
	 * @author Jose Rivera (190107-java-spark-usf)
	 * 
	 */
	@Test
	public void testGetProjectsByBatchIfBatchNameIsNotValid() throws Exception  {
		
		String uri = "/batch/";
		String pathVariable = "190107-javascript-sparky-uga";
		
		when(mockProjectService.findByBatch(pathVariable)).thenReturn(null);
		
		this.mockMvc.perform(get(uri + pathVariable)).andExpect(status().isOk()).andExpect(content().string(""));
	}
	
	/**
	 * This method is going to test if projects will not be returned by getProjectsByBatch() if batch name is empty.
	 *
	 * @throws Exception
	 * 
	 * @author Christopher Shanor (190107-java-spark-usf)
	 * @author Jose Rivera (190107-java-spark-usf)
	 * 
	 */
	@Test
	public void testGetProjectByBatchIfBatchNameIsEmpty() throws Exception {
		
		String uri = "batch/";
		String pathVariable = "";
		
		when(mockProjectService.findByBatch(pathVariable)).thenReturn(null);
		
		this.mockMvc.perform(get(uri)).andExpect(status().isNotFound());
	}
}
