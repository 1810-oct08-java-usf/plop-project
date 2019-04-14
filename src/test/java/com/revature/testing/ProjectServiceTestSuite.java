package com.revature.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.FileService;
import com.revature.services.ProjectService;
import com.revature.services.StorageService;


/**
 * 
 * @author Derek Martin
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTestSuite {
	
	// Do not mock the class you intend to test
	private ProjectService classUnderTest;
	
	/**
	 * A simulated ProjectRespository
	 */
	@Mock
	private ProjectRepository testRepo;
	
	/**
	 * A simulated StorageService
	 */
	@Mock
	private StorageService testStorage;
	
	/**
	 * A simulated FileService
	 */
	@Mock
	private FileService testFileService;
	
	/**
	 * A simulated List<Project>; this can also be accomplished using a spy.
	 */
	@Mock
	private List<Project> dummyList;
	
	/**
	 * A simulated Project; holds data for the test methods to access during assertion.
	 */
	@Mock
	private Project dummyProject = new Project();
	
	@Before
	public void preTestInit() {			
		classUnderTest = new ProjectService(testRepo, testStorage, testFileService);
		
		// Define the behavior of dummyList
		dummyList.add(dummyProject);
		//Mockito.when(dummyList.add()).thenCallRealMethod(dummyProject);
		
		
		// Define the relevant behaviors of testRepo 
		Mockito.when(testRepo.findById("floop")).thenReturn(Optional.of(dummyProject));
		Mockito.when(testRepo.findByName("string")).thenReturn(dummyList);
		Mockito.when(testRepo.findByBatch("batchin")).thenReturn(dummyList);
		
		//Define the relevant behaviors of dummyProject
		
	}
	
	/**
	 * Assertion to verify that findByBatch return contains the dummyProject
	 */
	@Test
	public void shouldReturnProjectOnGoodBatchSearch() {
		assertThat(classUnderTest.findByBatch("batchin").contains(dummyProject));
	}
	
	/**
	 * Assertion should verify that method returns a Project instance
	 */
	@Test
	public void shouldReturnProjectOnGoodIdSearch() {
		assertThat(classUnderTest.findById("floop")).isInstanceOf(Project.class);
	}
	
	/**
	 * Assertion should verify that searching with a bad id value returns null
	 */
	@Test
	public void shouldReturnNullOnFailedIdSearch() {
		assertThat(classUnderTest.findById("test")).isNull();
	}
	
	/**
	 *  Assert that method should return an ArrayList given a correct string parameter
	 */
	// findByName()
	@Test
	public void shouldReturnArrayListByName() {
		assertThat(classUnderTest.findByName("string")).isInstanceOf(List.class);
	}
	
	/**
	 * Assert that method should return a value of true on a valid id parameter.
	 */
	// deleteById()
	@Test
	public void shouldReturnTrueOnValidDelete() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}
	
	/**
	 * Assert that method should return a value of true on any string parameter
	 */
	@Test
	public void shouldReturnTrueOnAnyString() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}
	
	/**
	 * Assert that method should return false on a null parameter
	 */
	@Test
	public void shouldReturnFalseOnNullParameter() {
		assertThat(classUnderTest.deleteById(null)).isEqualTo(Boolean.FALSE);
	}
	
	/**
	 * Assert that findAllProjects returns a LinkedList
	 */
	// findAllProjects()
	@Test
	public void shouldReturnLinkedList() {
		assertThat(classUnderTest.findAllProjects()).isInstanceOf(LinkedList.class);
	}
	
	// TODO: Add additional test methods to improve coverage of the test suite as needed.
}
