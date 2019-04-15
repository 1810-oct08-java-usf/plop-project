package com.revature.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.revature.controllers.ProjectController;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.ProjectService;
/**
 * 
 * This class includes test cases for testing Project Controller's 
 * getProjectsByName();
 * @author Ankit Patel
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProjectController.class }, secure = false)
public class GetProjectsByNameTest {

		
		
		@Autowired
		private MockMvc mvc;
		
		@MockBean
		private ProjectService projectService;		
		
		@MockBean
		private ProjectRepository projectRepo;
		
		@MockBean
		private ProjectNotFoundException pnfe;
		
		@Mock
		Project project;
		
		private String uri = "/name/{name}";
			
		
		/**
		 * Tests whether the endpoint responds with OK or 200 status with valid request.
		 * 
		 * @throws Exception
		 * 
		 * @author Ankit Patel
		 */
		@Test
		public void testGetProjectsByName() throws Exception{
			
			String input = "ghostbusters";
			List<Project> projects = new ArrayList<>();

			when(projectService.findByName(input)).thenReturn(projects);
			
			this.mvc.perform(get(uri,input))
			.andExpect(status().isOk());
		}
		
		/**
		 * Tests the appropriate response to send with valid requests but project name
		 * doesn't exist.
		 * 
		 * @throws Exception
		 * 
		 * @author Ankit Patel
		 */
		@Test
		public void testGetNotFoundStatus() throws Exception{
			
			String input = "drocktnor";
	
			when(projectService.findByName(input)).thenThrow(pnfe);
			
			this.mvc.perform(get(uri,input))
			.andExpect(status().isNotFound());
					
		}	
		
}
