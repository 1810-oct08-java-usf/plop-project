package com.revature.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;
import com.revature.services.FileService;
import com.revature.services.ProjectService;
import com.revature.services.StorageService;


/**
 * Test suite for ProjectService.
 * @author Derek Martin
 * @author Alonzo Muncy (190107-Java-Spark-USF)
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
	 * A simulated ProjectDTO
	 */
	@Mock
	private ProjectDTO mockProjectDTO;
	
	/**
	 * A simulated List<Project>; this can also be accomplished using a spy.
	 */
	@Mock
	private List<Project> dummyList;
	
	/**
	 * A simulated Project; holds data for the test methods to access during assertion.
	 */
	@Mock
	private Project dummyProject;
	
	/**
	 * A simulated Project; holds data for the test methods to access during assertion.
	 */
	@Mock
	private Project dummySavedProject;
	
	/**
	 * A mock file
	 */
	@Mock
	private File mockFile;
	/**
	 * Can't spy or mock final classes
	 */
	Optional<Project> optionalProject;
	
	String dummyString = "Something";
	
	/**
	 * A mock list of strings
	 */
	@Mock
	ArrayList<String> mockListString;
	
	
	@Before
	public void preTestInit() {			
		classUnderTest = new ProjectService(testRepo, testStorage, testFileService);
		
		// Define the behavior of dummyList
		dummyList.add(dummyProject);
		
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
	 *  Assert that method should return an ArrayList given a correct string parameter for findByName()
.	 */
	@Test
	public void shouldReturnArrayListByName() {
		assertThat(classUnderTest.findByName("string")).isInstanceOf(List.class);
	}
	
	/**
	 * Assert that method should return a value of true on a valid id parameter for deleteById().
	 */
	@Test
	public void shouldReturnTrueOnValidDelete() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}
	
	/**
	 * Assert that method should return a value of true on any string parameter for deleteById().
	 */
	@Test
	public void shouldReturnTrueOnAnyString() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}
	
	/**
	 * Assert that method should return false on a null parameter for deleteById().
	 */
	@Test
	public void shouldReturnFalseOnNullParameter() {
		assertThat(classUnderTest.deleteById(null)).isEqualTo(Boolean.FALSE);
	}
	
	/**
	 * Assert that findAllProjects returns a LinkedList
	 */
	@Test
	public void shouldReturnLinkedList() {
		assertThat(classUnderTest.findAllProjects()).isInstanceOf(LinkedList.class);
	}
	/**
	 * Check if we can update all parts of a project
	 */
	@Test
	public void testUpdateProject() {
		optionalProject = Optional.of(dummySavedProject);
		when(testRepo.findById("97")).thenReturn(optionalProject);
		when(dummyProject.getName()).thenReturn(dummyString);
		when(dummyProject.getBatch()).thenReturn(dummyString);
		when(dummyProject.getTrainer()).thenReturn(dummyString);
		when(dummyProject.getGroupMembers()).thenReturn(mockListString);
		when(dummyProject.getScreenShots()).thenReturn(mockListString);
		when(dummyProject.getZipLinks()).thenReturn(mockListString);
		when(dummyProject.getDescription()).thenReturn(dummyString);
		when(dummyProject.getTechStack()).thenReturn(dummyString);
		when(dummyProject.getStatus()).thenReturn(dummyString);
		when(dummyProject.getOldProject()).thenReturn(dummySavedProject);
		assertTrue(classUnderTest.updateProject(dummyProject, "97"));
	}
	
	/**
	 * Test if we can create a project from a DTO. We need the lists and such to properly mock the implementation. 
	 * TODO: Find a better way to test this thing. Good Luck.
	 */
	
	@Test
	public void testCreateProjectFromDTO(){
		
		ArrayList<MultipartFile> listMultipartFile = new ArrayList<MultipartFile>();
		
		MultipartFile mockMultipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
		
		ArrayList<String> listZipLink = new ArrayList<String>();
		listZipLink.add("link");
		listMultipartFile.add(mockMultipartFile);
		
		when(mockProjectDTO.getName()).thenReturn(dummyString);
		when(mockProjectDTO.getBatch()).thenReturn(dummyString);
		when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
		when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
		when(mockProjectDTO.getDescription()).thenReturn(dummyString);
		when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
		when(mockProjectDTO.getStatus()).thenReturn(dummyString);
		when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
		when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
		when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);
		
		
		try {
			when(testFileService.download("link/archive/master.zip")).thenReturn(mockFile);
			
			when(testStorage.store(mockFile)).thenReturn(dummyString);
		assertTrue(classUnderTest.createProjectFromDTO(mockProjectDTO) instanceof Project); 
	
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
