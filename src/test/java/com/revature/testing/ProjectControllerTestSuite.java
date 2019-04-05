package com.revature.testing;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.controllers.ProjectController;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.services.ProjectService;

/*
 * This Class tests the ProjectController
 * 
 * @author Alonzo Muncy (190107-Java-Spark-USF)
 */

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTestSuite {
	
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
	 * Tests the set pending functionality. If the old project is approved and the new project is pending, then it should set the old project and set the new project to pending. We check this by verifying that the proper commands are being run as we are not testing the Project class. 
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
	 * This tests if the project status is denied and there is a null old project. We do this by verifying that it did not go down either of the other two branches.
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
	 * This tests the condition where the old project of the backend is approved but the current project is denied. we retun the mock project old as it has the getStatus return that we need. we verify this by checking if the getOldProject of the branch is called the right number of times.
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
	 * This tests if the project status is denied and the old project is in some other condition. We do this by verifying that it did not go down either of the other two branches.
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