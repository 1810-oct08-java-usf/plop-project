package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.controllers.ProjectController;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.services.ProjectService;


@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTestSuite {//For testing the ProjectController class --Alonzo Muncy 2019/04/05

	@Mock
	Project project;
	
	@Mock 
	ProjectService projectService; 
	
	@InjectMocks
	ProjectController projectController;
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none(); 
		
	@Before
	public void setup() {
		project = new Project(); 
	}
	
	/**
	 * testDeleteById
	 */
	
	
	/**
	 * Test method for deleting by id 
	 * if the input project id is valid.
	 *  
	 * @author Bjorn Pedersen & Brandon Morris 190107-Java-Spark-USF
	 * 
	 */
	@Test
	public void testDeleteByIdIfIdValid() { 
		project.setId("47");
		when(projectService.findById("47")).thenReturn(project);
		
		when(projectService.deleteById("47")).thenReturn(true);
		
		assertEquals(true, projectController.deleteById("47"));
		
	}
	
	
	
	//testDeleteById
	
	/**
	 * Test for exception to be thrown when 
	 * project not found in database via ID. 
	 * 
	 * @author Bjorn Pedersen & Brandon Morris 190107-Java-Spark-USF
	 * 
	 */
	@Test
	public void testDeleteByIdIfNotFound() {
		
		when(projectService.findById("47")).thenReturn(null);
		
		exceptionRule.expect(ProjectNotFoundException.class);
		exceptionRule.expectMessage("ID entered cannot be found to delete this project");
		
		projectController.deleteById("47");
	}
	
	
	//testDeleteById
	
	//testHandleException
	
	
}


