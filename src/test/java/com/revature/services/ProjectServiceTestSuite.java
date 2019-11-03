package com.revature.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.revature.exceptions.ProjectNotAddedException;
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
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
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

  String dummyString = "Something";

  @DataPoints("string cases")
  public static String[] dummyStrings = {null, "", "dummyString"};

  @DataPoints("int cases")
  public static int[] dummyNumbers = {-1, 0, 1};

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

  @Before
  public void preTestInit() {
    classUnderTest = new ProjectService(testRepo, testStorage, testFileService);

    dummyList.add(dummyProject);
    listZipLink.add("link");
    listMultipartFile.add(mockMultipartFile);
    mockListString.add("elemtem");
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Test
  public void T_findById_Valid() {
    Optional<Project> localproj = Optional.of(dummyProject);
    when(testRepo.findById(Mockito.anyString())).thenReturn(localproj);
    assertEquals("Passed in String that is valid", dummyProject, classUnderTest.findById("id"));
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Theory
  public void T_findById_Invalid_Id(@FromDataPoints("string cases") String dummyId) {
    when(testRepo.findById(dummyId)).thenReturn(Optional.empty());
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findById(dummyId);
            });
  }

  /** Assert that method should return false on a null parameter for deleteById(). */
  @Test
  public void T_deleteById_Null() {
    assertThat(classUnderTest.deleteById(null)).isEqualTo(Boolean.FALSE);
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. All fields are valid.
   */
  @Test
  public void T_updateProject_Valid() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertTrue(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The GroupMembers that is passed is null
   */
  @Test
  public void T_updateProject_NullGroupMembers() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(null);
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The GroupMembers that is passed is an Empty List.
   */
  @Test
  public void T_updateProject_EmptyGroupMembers() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(new ArrayList<>());
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The Ziplinks that is passed is null
   */
  @Test
  public void T_updateProject_NullZipLinks() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(null);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The Ziplinks that is passed is an Empty List.
   */
  @Test
  public void T_updateProject_EmptyZipLinks() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(new ArrayList<>());
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The DataModel that is passed is null
   */
  @Test
  public void T_updateProject_NullDataModel() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(null);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The DataModel that is passed is an Empty List.
   */
  @Test
  public void T_updateProject_EmptyDataModel() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(new ArrayList<>());
    when(dummyProject.getScreenShots()).thenReturn(mockListString);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. The ScreenShots that is passed is null
   */
  @Test
  public void T_updateProject_NullScreenShots() {
    optionalProject = Optional.of(dummySavedProject);
    when(dummyProject.getUserId()).thenReturn(1);
    when(dummyProject.getDescription()).thenReturn(dummyString);
    when(dummyProject.getName()).thenReturn(dummyString);
    when(dummyProject.getBatch()).thenReturn(dummyString);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyString);
    when(dummyProject.getTrainer()).thenReturn(dummyString);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(null);
    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Checks if userId, description, name, batch, groupmembers, Ziplinks, techStack, Status, or
   * Trainer is Null. All iteratations have at least 1 invalid value.
   */
  @Theory
  public void T_updateProject_Invalid(
      @FromDataPoints("string cases") String dummyDescription,
      @FromDataPoints("string cases") String dummyName,
      @FromDataPoints("string cases") String dummyBatch,
      @FromDataPoints("string cases") String dummyTechStack,
      @FromDataPoints("string cases") String dummyTrainer,
      @FromDataPoints("int cases") int dummyId) {

    optionalProject = Optional.of(dummySavedProject);
    if (dummyId > 0
        && dummyName != null
        && !dummyName.isEmpty()
        && dummyName.equals(dummyBatch)
        && dummyName.equals(dummyTrainer)
        && dummyName.equals(dummyDescription)
        && dummyName.equals(dummyTechStack)) {
      assertTrue("Valid Use Case: Not to be Tested", true);
      return;
    }

    when(dummyProject.getUserId()).thenReturn(dummyId);
    when(dummyProject.getDescription()).thenReturn(dummyDescription);
    when(dummyProject.getName()).thenReturn(dummyName);
    when(dummyProject.getBatch()).thenReturn(dummyBatch);
    when(dummyProject.getGroupMembers()).thenReturn(mockListString);
    when(dummyProject.getZipLinks()).thenReturn(mockListString);
    when(dummyProject.getTechStack()).thenReturn(dummyTechStack);
    when(dummyProject.getTrainer()).thenReturn(dummyTrainer);
    when(dummyProject.getDataModel()).thenReturn(mockListString);
    when(dummyProject.getScreenShots()).thenReturn(mockListString);

    assertFalse(classUnderTest.evaluateProject(dummyProject));
  }

  /**
   * Test if we can create a project from a DTO. We need the lists and such to properly mock the
   * implementation.
   *
   * @throws IOException
   */
  @Test
  public void T_CreateProjectFromDTO_Valid() throws IOException {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getDataModel()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
    when(testRepo.save(Mockito.any())).thenReturn(new Project());

    try {
      when(testFileService.download(Mockito.anyString())).thenReturn(mockFile);
      when(mockFile.length()).thenReturn(1_000L);
      when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

      assertTrue(classUnderTest.createProjectFromDTO(mockProjectDTO) instanceof Project);

    } catch (Exception e) {
      System.out.println("Issue with createProjectFromDTO");
      e.printStackTrace();
      assertFalse(true);
    }
  }

  /** Test if an exception is thrown if a invalid field is set within ProjectDTO */
  @Theory
  public void T_createProjectFromDTO_Invalid(
      @FromDataPoints("string cases") String dummyName,
      @FromDataPoints("string cases") String dummyBatch,
      @FromDataPoints("string cases") String dummyTrainer,
      @FromDataPoints("string cases") String dummyDescription,
      @FromDataPoints("string cases") String dummyTechStack,
      @FromDataPoints("int cases") int dummyInt) {

    if (dummyInt > 0
        && dummyName != null
        && !dummyName.isEmpty()
        && dummyName == dummyBatch
        && dummyName == dummyTrainer
        && dummyName == dummyDescription
        && dummyName == dummyTechStack) assertTrue("Valid Use Case: Not to be Tested", true);

    when(mockProjectDTO.getUserId()).thenReturn(dummyInt);
    when(mockProjectDTO.getName()).thenReturn(dummyName);
    when(mockProjectDTO.getBatch()).thenReturn(dummyBatch);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyTrainer);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyDescription);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyTechStack);
    when(mockProjectDTO.getStatus()).thenReturn("Pending");
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockMultipartFile.getSize()).thenReturn((long) 1000000);

    assertThatExceptionOfType(ProjectNotAddedException.class)
        .isThrownBy(
            () -> {
              classUnderTest.createProjectFromDTO(mockProjectDTO);
            });
  }
}
