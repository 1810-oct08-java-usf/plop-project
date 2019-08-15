package com.revature.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

/**
 * Test suite for ProjectService.
 * 
 * @author Derek Martin
 * @author Alonzo Muncy (190107-Java-Spark-USF)
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTestSuite {

	private ProjectService classUnderTest;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	// A simulated ProjectRespository
	@Mock
	private ProjectRepository testRepo;

	// A simulated StorageService
	@Mock
	private S3StorageServiceImpl testStorage;

	// A simulated FileService
	@Mock
	private FileServiceImpl testFileService;

	// A simulated ProjectDTO
	@Mock
	private ProjectDTO mockProjectDTO;

	// A simulated List<Project>; this can also be accomplished using a spy.
	@Mock
	private List<Project> dummyList;

	// A simulated Project; holds data for the test methods to access during
	// assertion.
	@Mock
	private Project dummyProject;

	// A simulated Project; holds data for the test methods to access during
	// assertion.
	@Mock
	private Project dummySavedProject;

	// A mock file
	@Mock
	private File mockFile;

	// A mock multipart file
	@Mock
	private MultipartFile mockMultipartFile;

	// Can't spy or mock final classes
	Optional<Project> optionalProject;

	String dummyString = "Something";

	// A mock list of strings
	ArrayList<String> mockListString = new ArrayList<>();

	/**
	 * + Creating exception rule, this is useful when we are expecting the
	 * method to throw an exception.
	 * + Moved the listMultipartFile and listZipLink to a class variable to use them
	 * in other testing methods.
	 * @author Testing team (ago 2019) - USF
	 */
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	ArrayList<MultipartFile> listMultipartFile = new ArrayList<MultipartFile>();
	ArrayList<String> listZipLink = new ArrayList<String>();

	@Before
	public void preTestInit() {
		classUnderTest = new ProjectService(testRepo, testStorage, testFileService);

		dummyList.add(dummyProject);

		// Testing team (2019) - USF
		listZipLink.add("link");
		listMultipartFile.add(mockMultipartFile);
		mockListString.add("elemtem");
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
		when(testRepo.findById("floop")).thenReturn(Optional.of(dummyProject));
		assertThat(classUnderTest.findById("floop")).isInstanceOf(Project.class);
	}
	
	@Test
	public void shouldReturnProjectOnGoodEditSubmission() {
		assertThat(classUnderTest.submitEditRequest(dummySavedProject)).isEqualTo(Boolean.TRUE);
	}

	/**
	 * Assertion should verify that searching with a bad id value returns null
	 */
	@Test
	public void shouldReturnNullOnFailedIdSearch() {
		assertThat(classUnderTest.findById("test")).isNull();
	}

	/**
	 * Assert that method should return an ArrayList given a correct string
	 * parameter for findByName() .
	 */
	@Test
	public void shouldReturnArrayListByName() {
		assertThat(classUnderTest.findByName("string")).isInstanceOf(List.class);
	}

	/**
	 * Assert that method should return a value of true on a valid id parameter for
	 * deleteById().
	 */
	@Test
	public void shouldReturnTrueOnValidDelete() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}

	/**
	 * Assert that method should return a value of true on any string parameter for
	 * deleteById().
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
	 * Test if we can create a project from a DTO. We need the lists and such to
	 * properly mock the implementation.
	 * 
	 * @author Testing team (ago 2019) - USF
	 *  UPDATED: Move some local variables to class variables so that they can be used
	 *  in many testing methods.
	 */
	@Test
	public void testCreateProjectFromDTO() {

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
			when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

			assertTrue(classUnderTest.createProjectFromDTO(mockProjectDTO) instanceof Project);

			// verifying that these methods are being used during this test
			verify(testFileService).download("link/archive/master.zip");
			verify(testStorage).store(mockMultipartFile);
		} catch (Exception e) {
			System.out.println("Issue with createProjectFromDTO");
			e.printStackTrace();
		}
	}

	/**
	 * This test will start from having an initial state of approved, then it will
	 * be set to pending before we call the save method.
	 * 
	 * @author Testing team (ago 2019) - USF
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testCreateProjectFromDTO_InitialStatusApproved() throws IOException {

		// Initializing mock
		when(mockProjectDTO.getName()).thenReturn(dummyString);
		when(mockProjectDTO.getBatch()).thenReturn(dummyString);
		when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
		when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
		when(mockProjectDTO.getDescription()).thenReturn(dummyString);
		when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
		when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
		when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
		when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);
		when(testFileService.download("link/archive/master.zip")).thenReturn(mockFile);
		when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

		// Setting status to approved
		when(mockProjectDTO.getStatus()).thenReturn("Approved");
		
		// Calling method to be tested
		Project result = classUnderTest.createProjectFromDTO(mockProjectDTO);

		// assertions
		assertTrue(result instanceof Project);
		assertEquals("Pending", result.getStatus());

		// verifying that these methods are being used during this test
		verify(testFileService).download("link/archive/master.zip");
		verify(testStorage).store(mockMultipartFile);
	}

	/**
	 * Test if an exception is thrown if the file size is over set limitation
	 * 
	 * Calls the createProjectFromDTO() method using mostly dummy data and a sample
	 * file found on github that is currently 57707774 bytes. It should trigger a
	 * FileSizeTooLargeException.
	 * 
	 * If there is ever any future errors with this test. Make sure to check that
	 * the link(s) added to listZipLink are still valid first.
	 * 
	 * @author Kevin Ocampo (190422-Java-Spark-USF)
	 * 
	 *         UPDATED: Changed local variables to global.
	 * @author Testing Team (ago 2019) - USF
	 */
	@Test
	public void testCreateProjectFromDTOFileSizeTooLarge() {

		when(mockProjectDTO.getName()).thenReturn(dummyString);
		when(mockProjectDTO.getBatch()).thenReturn(dummyString);
		when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
		when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
		when(mockProjectDTO.getDescription()).thenReturn(dummyString);
		when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
		when(mockProjectDTO.getStatus()).thenReturn(dummyString);
		when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
		when(mockMultipartFile.getSize()).thenReturn((long) 1000001);

		boolean thrown = false;
		try {
			classUnderTest.createProjectFromDTO(mockProjectDTO);
		} catch (FileSizeTooLargeException fstle) {
			System.out.println("FileSizeTooLargeException caught in test.");
			thrown = true;
		}
		assertTrue(thrown);
	}

	/**
	 * Testing that validates when the group members are not provided.
	 * 
	 * @author Testing team (ago 2019) - USF
	 */
	@Test
	public void testAddProjectIfNoGroupMembersProvided() {

		// expecting an exception
		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'group members' input cannot be empty when adding project");

		// Initializing mock
		when(mockProjectDTO.getName()).thenReturn(dummyString);
		when(mockProjectDTO.getBatch()).thenReturn(dummyString);
		when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
		when(mockProjectDTO.getGroupMembers()).thenReturn(null); // Setting this to null
		when(mockProjectDTO.getDescription()).thenReturn(dummyString);
		when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
		when(mockProjectDTO.getStatus()).thenReturn(dummyString);
//		when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
//		when(mockMultipartFile.getSize()).thenReturn((long) 1000001);

		// Call method to be tested
		classUnderTest.createProjectFromDTO(mockProjectDTO);
	}

	/**
	 * Testing if project's description is not provided, it is going to raise an
	 * exception.
	 * 
	 * @author Testing team (ago 2019) - USF
	 */
	@Test
	public void testAddProjectIfNoDescriptionIsProvided() {

		// Setting expectations
		exceptionRule.expect(ProjectNotAddedException.class);
		exceptionRule.expectMessage("The 'description' input cannot be empty when adding project");

		// Initializing mock
		when(mockProjectDTO.getName()).thenReturn(dummyString);
		when(mockProjectDTO.getBatch()).thenReturn(dummyString);
		when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
		when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
		when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
		when(mockProjectDTO.getStatus()).thenReturn(dummyString);
//		when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
//		when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
//		when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

		// setting description to null
		when(mockProjectDTO.getDescription()).thenReturn(null);

		// executing method
		classUnderTest.createProjectFromDTO(mockProjectDTO);
	}

	/**
	 * Testing if project's GitHub links are not provided, if so, test should
	 * complete successfully.
	 * 
	 * @author Testing team (ago 2019) - USF
	 */
	@Test
	public void testAddProjectIfNoGitHubLinkIsProvided() {

		// Initializing mock
		when(mockProjectDTO.getName()).thenReturn(dummyString);
		when(mockProjectDTO.getBatch()).thenReturn(dummyString);
		when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
		when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
		when(mockProjectDTO.getDescription()).thenReturn(dummyString);
		when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
		when(mockProjectDTO.getStatus()).thenReturn(dummyString);
		when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);

		// setting zip links to null
		when(mockProjectDTO.getZipLinks()).thenReturn(null);

		when(testRepo.save(Mockito.any())).thenReturn(dummySavedProject);

		// Calling method to be tested
		Project result = classUnderTest.createProjectFromDTO(mockProjectDTO);

		// assert
		assertNotNull(result); // Project should not be null
		verify(testRepo, times(1)).save(Mockito.any());
	}
	
	
}
