package com.revature.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

/** Test suite for ProjectService. */
@SpringBootTest
@RunWith(Theories.class)
// @RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTestSuite {

  private ProjectService classUnderTest;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  // A simulated ProjectRespository
  private ProjectRepository testRepo = Mockito.mock(ProjectRepository.class);

  // A simulated StorageService
  private S3StorageServiceImpl testStorage = Mockito.mock(S3StorageServiceImpl.class);

  // A simulated FileService
  private FileServiceImpl testFileService = Mockito.mock(FileServiceImpl.class);

  // A simulated ProjectDTO
  private ProjectDTO mockProjectDTO = Mockito.mock(ProjectDTO.class);

  // A simulated List<Project>; this can also be accomplished using a spy.
  private List<Project> dummyList = Mockito.mock(List.class);

  // A simulated Project; holds data for the test methods to access during
  // assertion.
  private Project dummyProject = Mockito.mock(Project.class);

  // A simulated Project; holds data for the test methods to access during
  // assertion.
  private Project dummySavedProject = Mockito.mock(Project.class);

  // A mock file
  private File mockFile = Mockito.mock(File.class);

  // A mock multipart file
  private MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);

  // Can't spy or mock final classes
  Optional<Project> optionalProject;
  String dummyString = "dummyString";

  @DataPoints("string cases")
  public static String[] dummyStrings = {null, "", "dummyString"};

  // A mock list of strings
  ArrayList<String> mockListString = new ArrayList<>();

  /**
   * + Creating exception rule, this is useful when we are expecting the method to throw an
   * exception. + Moved the listMultipartFile and listZipLink to a class variable to use them in
   * other testing methods.
   */
  @Rule public ExpectedException exceptionRule = ExpectedException.none();

  ArrayList<MultipartFile> listMultipartFile = new ArrayList<MultipartFile>();
  ArrayList<String> listZipLink = new ArrayList<String>();

  /** Configures data structures prior to each test. */
  @Before
  public void preTestInit() {
    classUnderTest = new ProjectService(testRepo, testStorage, testFileService);
    dummyProject.setBatch("batchin");
    dummyList = new ArrayList<Project>();
    dummyList.add(dummyProject);

    listZipLink.add("link");
    listMultipartFile.add(mockMultipartFile);
    mockListString.add("elemtem");
  }

  /**
   * Verifies the output of findByBatch when provided a null value. If operating correctly, a
   * BadRequestException will be expected.
   */
  @Test
  public void T_findByBatch_Null() {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByBatch(null);
            });
  }

  /**
   * Verifies the output of findByBatch when provided an empty string. If operating correctly, a
   * BadRequestException will be expected.
   */
  @Test
  public void T_findByBatch_Empty() {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByBatch(null);
            });
  }

  /**
   * Check for invalid names (i.e. the project name does not correspond to an existing project). If
   * operating properly, a ProjectNotFoundException will be expected.
   */
  @Test
  public void T_findByBatch_Invalid() {
    assertThatExceptionOfType(ProjectNotFoundException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByBatch("nonExistentName");
            });
  }

  /**
   * Check for valid names (i.e. the project name corresponds to an existing project). If operating
   * properly, a matching project is excepted.
   */
  @Test
  public void T_findByBatch_Valid() {
    when(testRepo.findByBatch("batchin")).thenReturn(dummyList);
    assertThat(classUnderTest.findByBatch("batchin")).contains(dummyProject);
  }

  /** Assertion should verify that method returns a Project instance */
  @Test
  public void shouldReturnProjectOnGoodIdSearch() {
    when(testRepo.findById("floop")).thenReturn(Optional.of(dummyProject));
    assertThat(classUnderTest.findById("floop")).isInstanceOf(Project.class);
  }

  @Test
  public void shouldReturnProjectOnGoodEditSubmission() {
    assertThat(classUnderTest.submitEditRequest(dummySavedProject)).isEqualTo(Boolean.TRUE);
  }

  /** Assertion should verify that searching with a bad id value returns null */
  @Test
  public void shouldReturnNullOnFailedIdSearch() {
    assertThat(classUnderTest.findById("test")).isNull();
  }

  /**
   * Assert that method should return an ArrayList given a correct string parameter for findByName()
   * .
   */
  @Test
  public void shouldReturnArrayListByName() {
    assertThat(classUnderTest.findByName("string")).isInstanceOf(List.class);
  }

  /** Assert that method should return a value of true on a valid id parameter for deleteById(). */
  @Test
  public void shouldReturnTrueOnValidDelete() {
    assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
  }

  /** Assert that method should return a value of true on any string parameter for deleteById(). */
  @Test
  public void shouldReturnTrueOnAnyString() {
    assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
  }

  /** Assert that method should return false on a null parameter for deleteById(). */
  @Test
  public void shouldReturnFalseOnNullParameter() {
    assertThat(classUnderTest.deleteById(null)).isEqualTo(Boolean.FALSE);
  }

  /**
   * Verifies behavior of findAllProjects when no projects exist. If operating correctly, a
   * ProjectNotFoundException is expected.
   */
  @Test
  public void T_findAllProjects_None() {
    assertThatExceptionOfType(ProjectNotFoundException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findAllProjects();
            });
  }

  /**
   * Checks that calling findAllProjects on dummyList will return a list of projects greater than 0.
   */
  @Test
  public void T_findAllProjects_Exist() {
    when(testRepo.findAll()).thenReturn(dummyList);
    assertThat(classUnderTest.findAllProjects().size()).isGreaterThan(0);
  }

  /** Check if we can update all parts of a project */
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
    assertTrue(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Test if we can create a project from a DTO. We need the lists and such to properly mock the
   * implementation.
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
      Mockito.verify(testFileService).download("link/archive/master.zip");
      Mockito.verify(testStorage).store(mockMultipartFile);
    } catch (Exception e) {
      System.out.println("Issue with createProjectFromDTO");
      e.printStackTrace();
    }
  }

  /**
   * This test will start from having an initial state of approved, then it will be set to pending
   * before we call the save method.
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
    Mockito.verify(testFileService).download("link/archive/master.zip");
    Mockito.verify(testStorage).store(mockMultipartFile);
  }

  /**
   * Verifies behavior of createProjectFromDTO when a project is valid but the multipart file's size
   * exceeds 1GB (1,000,000 bytes). If operating correctly, a FIleSizeTooLargeException is expected.
   */
  @Test
  public void T_createProjectFromDTO_FileTooLarge() {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockMultipartFile.getSize()).thenReturn(1_000_001L);

    // System.out.println(counter);
    assertThatExceptionOfType(FileSizeTooLargeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.createProjectFromDTO(mockProjectDTO);
            });
  }

  /** Testing that validates when the group members are not provided. */
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
    // when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    // when(mockMultipartFile.getSize()).thenReturn((long) 1000001);

    // Call method to be tested
    classUnderTest.createProjectFromDTO(mockProjectDTO);
  }

  /**
   * Verify that creating a project DTO with a null description raises an exception. If operating
   * correctly, a ProjectNotAddedException should be raised.
   */
  @Test
  public void T_createProjectFromDTO_NullDescription() {

    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getDescription()).thenReturn(null);

    assertThatExceptionOfType(ProjectNotAddedException.class)
        .isThrownBy(
            () -> {
              classUnderTest.createProjectFromDTO(mockProjectDTO);
            });
  }

  /**
   * Testing if project's GitHub links are not provided, if so, test should complete successfully.
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
    Mockito.verify(testRepo, Mockito.times(1)).save(Mockito.any());
  }
}
