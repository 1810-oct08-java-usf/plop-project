package com.revature.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;

/**
 * Testing suite for the Project Controller class
 * 
 * @author Alonzo, Bjorn Pedersen, Brandon Morris, Kamaria DeRamu, and Tracy
 *         Cummings (190107-Java-Spark-USF)
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTestSuite {

	@Mock
	Project project;

	@Mock
	ProjectDTO projectDTO;

	@Mock
	ProjectService projectService;

	@InjectMocks
	ProjectController projectController;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	/**
	 * Method to run before every test to initialize dependencies.
	 * 
	 * @author Bjorn Pedersen & Brandon Morris (190107-Java-Spark-USF)
	 */

	@Before
	public void setup() {
		project = new Project.ProjectBuilder().build();
		projectController = new ProjectController(projectService);
		projectDTO = new ProjectDTO.ProjectDTOBuilder().setBatch("Cabbage").setName("Kids")
				.setTechStack("Light, Water, Soil").build();
	}

	/**
	 * Test if addProject works when passed valid project.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */

	@Test
	public void testAddProjectIfProjectValid() {

		when(projectService.createProjectFromDTO(projectDTO)).thenReturn(project);

		projectController.addProject(projectDTO.getName(),projectDTO.getBatch(),projectDTO.getTrainer(),
				projectDTO.getGroupMembers(),projectDTO.getScreenShots(),projectDTO.getZipLinks(),
				projectDTO.getDescription(),projectDTO.getTechStack(),projectDTO.getStatus(),projectDTO.getDataModel(),
				projectDTO.getUserId());

		verify(projectService).createProjectFromDTO(projectDTO);

	}


	/**
	 * Test if addProject throws appropriate exception if passed projectDTO with no
	 * batch info.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */

	@Test
	public void testAddProjectIfNoBatchGiven() {

		projectDTO.setBatch(null);

		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'batch' input cannot be empty when adding project");

		projectController.addProject(projectDTO.getName(),projectDTO.getBatch(),projectDTO.getTrainer(),
				projectDTO.getGroupMembers(),projectDTO.getScreenShots(),projectDTO.getZipLinks(),
				projectDTO.getDescription(),projectDTO.getTechStack(),projectDTO.getStatus(),projectDTO.getDataModel(),
				projectDTO.getUserId());
	}

	/**
	 * Test if addProject throws appropriate exception if passed projectDTO with no
	 * project name.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */

	@Test
	public void testAddProjectIfNoNameGiven() {

		projectDTO.setName(null);

		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'name' input cannot be empty when adding project");
		
		projectController.addProject(projectDTO.getName(),projectDTO.getBatch(),projectDTO.getTrainer(),
				projectDTO.getGroupMembers(),projectDTO.getScreenShots(),projectDTO.getZipLinks(),
				projectDTO.getDescription(),projectDTO.getTechStack(),projectDTO.getStatus(),projectDTO.getDataModel(),
				projectDTO.getUserId());
	}

	/**
	 * Test if addProject throws appropriate exception if passed projectDTO with no
	 * tech stack info.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */

	@Test
	public void testAddProjectIfNoTechStackGiven() {

		projectDTO.setTechStack(null);

		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'tech stack' input cannot be empty when adding project");
		
		projectController.addProject(projectDTO.getName(),projectDTO.getBatch(),projectDTO.getTrainer(),
				projectDTO.getGroupMembers(),projectDTO.getScreenShots(),projectDTO.getZipLinks(),
				projectDTO.getDescription(),projectDTO.getTechStack(),projectDTO.getStatus(),projectDTO.getDataModel(),
				projectDTO.getUserId());
	}

	/**
	 * Test Delete by id Test method for deleting by id if the input project id is
	 * valid.
	 * 
	 * @author Bjorn Pedersen & Brandon Morris (190107-Java-Spark-USF)
	 * 
	 */
	@Test
	public void testDeleteByIdIfIdValid() {
		project.setId("47");
		when(projectService.findById("47")).thenReturn(project);

		when(projectService.deleteById("47")).thenReturn(true);

		assertEquals(true, projectController.deleteById("47"));

	}

	// testDeleteById
	// ------------------------------------------------------------------

	/**
	 * Test for exception to be thrown when project not found in database via ID.
	 * 
	 * @author Bjorn Pedersen & Brandon Morris (190107-Java-Spark-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 */
	@Test
	public void testDeleteByIdIfNotFound() {

		when(projectService.findById("47")).thenReturn(null);

		exceptionRule.expect(ProjectNotFoundException.class);
		exceptionRule.expectMessage("Project with id: 47, cannot be found to delete this project.");

		projectController.deleteById("47");
	}

	/**
	 * Test for handleExceptions in the case that
	 * 
	 * @author Bjorn Pedersen & Brandon Morris (190107-Java-Spark-USF)
	 */

	@Mock
	ProjectErrorResponse projectErrorResponse;

	@Mock
	ProjectNotFoundException projectNotFoundException;

	@Mock
	ProjectNotAddedException projectNotAddedException;

	// --------------------------------------------------------------------------------------------

	/**
	 * Test for returning a non-empty list of projects.
	 * 
	 * @author Kamaria DeRamus & Bjorn Pedersen (190107-Java-Spark-USF)
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 * 
	 */

	@Test
	public void testGetAllProjectsMoreThanOneProject() {
		Project project1 = new Project.ProjectBuilder().build();
		Project project2 = new Project.ProjectBuilder().build();
		List<Project> projectList = new ArrayList<>();

		projectList.add(project1);
		projectList.add(project2);

		when(projectService.findAllProjects()).thenReturn(projectList);

		assertEquals(projectList, projectController.getAllProjects());
	}

	// --------------------------------------------------------------------------

	/**
	 * Test for returning list with only one project.
	 * 
	 * @author Kamaria DeRamus & Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetAllProjectsOnlyOneProject() {
		List<Project> projectList2 = new ArrayList<>();
		projectList2.add(project);

		when(projectService.findAllProjects()).thenReturn(projectList2);

		assertEquals(projectList2, projectController.getAllProjects());
	}

	// --------------------------------------------------------------------------
	/**
	 * Test for returning a list of projects by valid name
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */

	@Test
	public void testGetProjectsByNameIfValidName() {
		Project project3 = new Project.ProjectBuilder().build();
		Project project4 = new Project.ProjectBuilder().build();

		project3.setName("Kamaria");
		project4.setName("Kamaria");

		List<Project> projectList3 = new ArrayList<>();
		projectList3.add(project3);
		projectList3.add(project4);

		when(projectService.findByName("Kamaria")).thenReturn(projectList3);
		assertEquals(projectList3, projectController.getProjectsByName("Kamaria"));

	}

	/**
	 * Test for returning a list of projects by valid batch
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */

	@Test
	public void testGetProjetsByBatchIfValid() {
		Project project3 = new Project.ProjectBuilder().build();
		Project project4 = new Project.ProjectBuilder().build();

		project3.setBatch("Wezley");
		project4.setBatch("Wezley");

		List<Project> projectList3 = new ArrayList<>();
		projectList3.add(project3);
		projectList3.add(project4);

		when(projectService.findByBatch("Wezley")).thenReturn(projectList3);
		assertEquals(projectList3, projectController.getProjectsByBatch("Wezley"));
	}

	
	/**
	 * Test for returning a list of projects by status
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */

	@Test
	public void testGetProjectsByStatusIfValid() {
		Project project5 = new Project.ProjectBuilder().build();
		project5.setStatus("Approved");

		List<Project> projectList = new ArrayList<>();
		projectList.add(project5);

		when(projectService.findByStatus("Approved")).thenReturn(projectList);
		assertEquals(projectList, projectController.getProjectsByStatus("Approved"));

	}


	/**
	 * Test for returning a project by valid id
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */

	@Test
	public void testGetProjectByValidId() {
		project.setId("3");
		when(projectService.findById("3")).thenReturn(project);
		assertEquals(project, projectController.getProjectById("3"));
	}


}