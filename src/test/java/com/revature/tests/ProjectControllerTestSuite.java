package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.controllers.ProjectController;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;
/**
 *  Testing suite for the Project Controller class
 * @author Alonzo, Bjorn Pedersen, Brandon Morris, Kamaria DeRamu, and Tracy Cummings (190107-Java-Spark-USF)
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
		project = new Project();
		projectController = new ProjectController(projectService);
		projectDTO = new ProjectDTO();
		projectDTO.setBatch("Cabbage");
		projectDTO.setName("Kids");
		projectDTO.setTechStack("Light, Water, Soil");
	}

	/**
	 * Test if addProject works when passed valid project.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testAddProjectIfProjectValid() {
		
		when(projectService.createProjectFromDTO(projectDTO)).thenReturn(project);
		projectController.addProject(projectDTO);
		verify(projectService).createProjectFromDTO(projectDTO);
		
	}
	
	/**
	 * Test if addProject throws appropriate exception if passed null.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testAddProjectIfPassedNull() {
		
		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("No Project Data Submitted");
		
		projectController.addProject(null);
	}
	
	/**
	 * Test if addProject throws appropriate exception if 
	 * passed projectDTO with no batch info.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testAddProjectIfNoBatchGiven() {
		
		projectDTO.setBatch(null);		
		
		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'batch' input cannot be null when adding project");
		
		projectController.addProject(projectDTO);
	}
	
	
	/**
	 * Test if addProject throws appropriate exception if 
	 * passed projectDTO with no project name.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testAddProjectIfNoNameGiven() {
		
		projectDTO.setName(null);		
		
		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'name' input cannot be null when adding project");
		
		projectController.addProject(projectDTO);
	}
	
	/**
	 * Test if addProject throws appropriate exception if 
	 * passed projectDTO with no tech stack info.
	 * 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testAddProjectIfNoTechStackGiven() {
		
		projectDTO.setTechStack(null);		
		
		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'tech stack' input cannot be null when adding project");
		
		projectController.addProject(projectDTO);
	}
	
	/**
	 * Test Delete by id
	 * Test method for deleting by id if the input project id is valid.
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

	//--------------------------------------------------------------------------------------------
	
	/**
	 * Test for returning a non-empty list of projects.
	 * @author Kamaria DeRamus & Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testGetAllProjectsMoreThanOneProject() {
		Project project1 = new Project();
		Project project2 = new Project();
		List<Project> projectList = new ArrayList<>();
		
		projectList.add(project1);
		projectList.add(project2);
		
		when(projectService.findAllProjects()).thenReturn(projectList);
		
		assertEquals(new ArrayList<>(), projectController.getAllProjects());
		
		verify(projectService).findAllProjects();
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * Test for returning list with only one project.
	 * @author Kamaria DeRamus & Bjorn Pedersen (190107-Java-Spark-USF)
	 */
	@Test
	public void testGetAllProjectsOnlyOneProject() {
		List<Project> projectList2 = new ArrayList<>();
		projectList2.add(project);
		
		when(projectService.findAllProjects()).thenReturn(projectList2);
		
		assertEquals(projectList2, projectController.getAllProjects());
	}
	
	
	//--------------------------------------------------------------------------
	/**
	 * Test for returning a list of projects by valid name
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testGetProjectsByNameIfValidName() {
		Project project3 = new Project();
		Project project4 = new Project();
		
		project3.setName("Kamaria");
		project4.setName("Kamaria");
		
		List<Project> projectList3 = new ArrayList<>();
		projectList3.add(project3);
		projectList3.add(project4);
		
		when(projectService.findByName("Kamaria")).thenReturn(projectList3);
		assertEquals(projectList3, projectController.getProjectsByName("Kamaria"));
	
}
	
	//---------------------------------------------------------------------------
	
	/**
	 * Test for returning a list of projects where name is not found
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test (expected = ProjectNotFoundException.class)
	public void testGetProjectsByNameIfNameNotFound() {

		when(projectService.findByName("Kamaria")).thenReturn(null);
		
		projectController.getProjectsByName("Kamaria");
	}
	
	//--------------------------------------------------------------------------
	
	/**
	 * Test for returning a list of projects by valid batch
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testGetProjetsByBatchIfValid() {
		Project project3 = new Project();
		Project project4 = new Project();
		
		project3.setBatch("Wezley");
		project4.setBatch("Wezley");
		
		List<Project> projectList3 = new ArrayList<>();
		projectList3.add(project3);
		projectList3.add(project4);
		
		when(projectService.findByBatch("Wezley")).thenReturn(projectList3);
		assertEquals(projectList3, projectController.getProjectsByBatch("Wezley"));
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Test for returning a list of projects where batch name is not found
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test (expected = ProjectNotFoundException.class)
	public void testGetProjectsByBatchIfBatchNotFound() {
		when(projectService.findByBatch("Wezley")).thenReturn(null);

		projectController.getProjectsByBatch("Wezley");
		
	}

	// ------------------------------------------------------------------------------------------
	/**
	 * Test for returning a list of projects by status
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Test
	public void testGetProjectsByStatusIfValid() {
		Project project5 = new Project();
		project5.setStatus("Approved");
		
		List<Project> projectList = new ArrayList<>();
		projectList.add(project5);
		
		when(projectService.findByStatus("Approved")).thenReturn(projectList);
		assertEquals(projectList, projectController.getProjectsByStatus("Approved"));
		
	}
	
	//--------------------------------------------------------------------------
	/**
	 * Test for returning a list of projects where status is not found
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testGetProjectsByStatusIfStatusNotFound() {
		when(projectService.findByStatus("Approved")).thenReturn(null);
		
		exceptionRule.expect(ProjectNotFoundException.class);
		exceptionRule.expectMessage("Status entered cannot be found to return these projects");
		
		projectController.getProjectsByStatus("Approved");
	}
	
	
	//-------------------------------------------------------------------------------
	
	
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
	
	//------------------------------------------------------------------------------
	
	/**
	 * Test for returning a project if id not found
	 * 
	 * @author Kamaria DeRamus (190107-Java-Spark-USF)
	 */
	
	@Ignore //incomplete
	@Test
	public void testGetProjectByIdIfIdNotFound() {
		when(projectService.findById("3")).thenReturn(null);

		exceptionRule.expect(ProjectNotFoundException.class);
		exceptionRule.expectMessage("ID entered cannot be found to return this project");

		projectController.getProjectById("3");
		
	}
	
	//----------------------------------------------------------------------------------
	
	@InjectMocks
	private ProjectController classUnderTest;

	@Mock
	private Project mockProjectOld;

	@Mock
	private Project mockProjectNew;

	@Mock
	private ProjectService mockProjectService;

	/**
	 * Tests the Project not found condition.
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test(expected = ProjectNotFoundException.class)
	public void testProjectNotFoundException() {
		when(mockProjectService.findById("0")).thenReturn(null);
		classUnderTest.updateProject(mockProjectNew, "0");
	}

	/**
	 * Tests the set pending functionality. If the old project is approved and the
	 * new project is pending, then it should set the old project and set the new
	 * project to pending. We check this by verifying that the proper commands are
	 * being run as we are not testing the Project class.
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testSetPending() {
		when(mockProjectService.findById("0")).thenReturn(mockProjectOld);
		when(mockProjectOld.getStatus()).thenReturn("approved");
		when(mockProjectNew.getStatus()).thenReturn("pending");
		when(mockProjectService.updateProject(mockProjectNew, "0")).thenReturn(true);
		classUnderTest.updateProject(mockProjectNew, "0");
		verify(mockProjectNew, times(1)).setStatus("pending");
		verify(mockProjectNew, times(1)).setOldProject(mockProjectOld);
	}

	/**
	 * This tests if the project status is denied and there is a null old project.
	 * We do this by verifying that it did not go down either of the other two
	 * branches.
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testDeniedNull() {
		when(mockProjectService.findById("0")).thenReturn(mockProjectOld);
		when(mockProjectOld.getStatus()).thenReturn("approved");
		when(mockProjectNew.getStatus()).thenReturn("denied");
		when(mockProjectOld.getOldProject()).thenReturn(null);
		when(mockProjectService.updateProject(mockProjectNew, "0")).thenReturn(true);
		classUnderTest.updateProject(mockProjectNew, "0");
		verify(mockProjectOld, times(1)).getOldProject();
		verify(mockProjectNew, times(0)).setOldProject(mockProjectOld);
	}

	/**
	 * This tests the condition where the old project of the backend is approved but
	 * the current project is denied. we return the mock project old as it has the
	 * getStatus return that we need. we verify this by checking if the
	 * getOldProject of the branch is called the right number of times.
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testDeniedApproved() {
		when(mockProjectService.findById("0")).thenReturn(mockProjectOld);
		when(mockProjectOld.getStatus()).thenReturn("approved");
		when(mockProjectNew.getStatus()).thenReturn("denied");
		when(mockProjectOld.getOldProject()).thenReturn(mockProjectOld);
		when(mockProjectService.updateProject(mockProjectOld, "0")).thenReturn(true);
		classUnderTest.updateProject(mockProjectNew, "0");
		verify(mockProjectOld, times(3)).getOldProject();
		verify(mockProjectNew, times(0)).setOldProject(mockProjectOld);
	}

	/**
	 * This tests if the project status is denied and the old project is in some
	 * other condition. We do this by verifying that it did not go down either of
	 * the other two branches.
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testDeniedOther() {
		when(mockProjectService.findById("0")).thenReturn(mockProjectOld);
		when(mockProjectNew.getStatus()).thenReturn("denied");
		when(mockProjectOld.getStatus()).thenReturn("pending");
		when(mockProjectOld.getOldProject()).thenReturn(mockProjectOld);
		when(mockProjectService.updateProject(mockProjectNew, "0")).thenReturn(true);
		classUnderTest.updateProject(mockProjectNew, "0");
		verify(mockProjectOld, times(2)).getOldProject();
		verify(mockProjectNew, times(1)).setOldProject(mockProjectOld);
	}

	/**
	 * This tests the normal functionality of the update project function 
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Ignore //Incomplete
	@Test
	public void testNormal() {
		when(mockProjectService.findById("0")).thenReturn(mockProjectOld);
		when(mockProjectNew.getStatus()).thenReturn("pending");
		when(mockProjectOld.getStatus()).thenReturn("pending");
		when(mockProjectService.updateProject(mockProjectNew, "0")).thenReturn(true);
		classUnderTest.updateProject(mockProjectNew, "0");
		verify(mockProjectOld, times(0)).getOldProject();
		verify(mockProjectNew, times(0)).setOldProject(mockProjectOld);
	}
	


}