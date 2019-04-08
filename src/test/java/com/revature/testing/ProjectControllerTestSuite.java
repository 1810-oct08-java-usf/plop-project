package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import com.revature.controllers.ProjectController;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTestSuite {// For testing the ProjectController class --Alonzo Muncy 2019/04/05

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
	 * Test method for deleting by id if the input project id is valid.
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

	// testDeleteById
	// ------------------------------------------------------------------

	/**
	 * Test for exception to be thrown when project not found in database via ID.
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

	// testHandleException
	// --------------------------------------------------------------

	@Mock
	ProjectErrorResponse projectErrorResponse;

	@Mock
	ProjectNotFoundException projectNotFoundException;

	@Mock
	ProjectNotAddedException projectNotAddedException;

	/**
	 * Test for handleExceptions in the case that
	 * 
	 * @author Bjorn Pedersen & Brandon Morris (190107-Java-Spark-USF)
	 */
	/*
	 * @Test public void testHandleExceptionsWithProjectNotFoundException() {
	 * 
	 * when(projectController.handleExceptions(projectNotFoundException)).thenReturn
	 * (projectErrorResponse);
	 * 
	 * 
	 * }
	 * 
	 * @Test public void testHandleExceptionsWithProjectNotAddedException() {
	 * 
	 * when(projectContoller.handleExceptions(projectNotAddedException))
	 * 
	 * }
	 * 
	 * @Test public void testHandleExceptionsWithNull() {
	 * 
	 * }
	 */
	// ------------------------------------------------------------------------------------------

	// testAddProject
	// ---------------------------------------------------------------------------

	//	testAddProjectValidProject
	//	
	//	testAddProjcetNull

	/**
	 * Tests for behavior when a project is input validly 
	 * @author Bjorn Pedersen & Brandon Morris (190107-Java-Spark-USF)
	 */
	@Test
	public void testAddProjectValidProject() {
		//		when(projectController.addProject("name", "batch", "trainer",
		//				new ArrayList<String>(), new ArrayList<MultipartFile>(),
		//				new ArrayList<String>(), "description", "techStack", "status")).thenReturn(project);
		//				

		//TODO Yeah this has errors. But tis how it should be. Address this issue system-wide latter. 
		when(projectService.createProject(project)).then(project);
		
		// If the project passed in is the same as the project returned by the projectController's
		// add project method, return true. This is a basic check. Note that the methods in the
		// projectService are not pleasant. 
		assertEquals(project, projectController.addProject(project));
		
		verify(projectService.createProject(project));
		
	}

	/**
	 * This shall test the addProject method, 
	 * when invoked with the value null. 
	 * 
	 */
	@Test
	public void testAddProjectNull() {
		when(projectService.createProject(null)).then(null);
		
		
		assertEquals(null, projectController.addProject(null));
		
		verify(projectService.createProject(null));
		
	}

	// ------------------------------------------------------------------------------------------
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
	 */
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
	 */
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