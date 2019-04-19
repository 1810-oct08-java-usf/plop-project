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

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProjectController.class }, secure = false)
public class TestGetAllProjects {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private Project mockProject;
	
	@MockBean
	private ProjectService mockProjectService;
	
	@MockBean
	private ProjectRepository projectRepository;
	
	/**
	 * This method will test that our context successfully loads.
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * 
	 * @author Marco Van Rhyn (190107-Java-Spark-USF)
	 */
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}
	
	/**
	 * This method will test that findAllProjects returns a list of projects and is not null
	 *
	 * @throws Exception
	 * 
	 * @author Marco Van Rhyn (190107-java-spark-usf)
	 * 
	 */
	@Test
	public void testGetAllProjects() throws Exception {
		
		String uri = "/";
		
		List<Project> projects = new ArrayList<>();
		when(mockProjectService.findAllProjects()).thenReturn(projects);
		
		this.mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
		
	}

}
