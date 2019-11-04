package com.revature.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

  // A simulated List<Project> that will remain empty.
  private List<Project> dummyListEmpty = Mockito.mock(List.class);

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

  // A mock string
  private String dummyString = "dummyString";

  // All possible string inputs: null, empty, valid
  @DataPoints("string cases")
  public static String[] dummyStrings = {null, "", "dummyString"};

  // Only invalid string inputs
  @DataPoints("Invalid strings")
  public static String[] invalidStrings = {null, ""};

  // All possible int inputs: negative, zero, positive
  @DataPoints("int cases")
  public static Integer[] dummyNumbers = {-1, 0, 1, null};

  // A mock list of strings
  private ArrayList<String> mockListString = new ArrayList<>();

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
    dummyList = new ArrayList<>();
    dummyListEmpty = new ArrayList<>();
    dummyList.add(dummyProject);
    listZipLink.add("link");
    listMultipartFile.add(mockMultipartFile);
    mockListString.add("elemtem");
  }

  /**
   * Check for invalid names (i.e. the project name does not correspond to an existing project). If
   * operating properly, a ProjectNotFoundException will be expected.
   */
  @Theory
  public void T_findByBatch_Invalid(@FromDataPoints("string cases") String batchName) {
    when(testRepo.findByBatch(batchName)).thenReturn(dummyListEmpty);
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByBatch(batchName);
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

  /**
   * Tests findByName with invalid inputs. Since the input is a string, the invalid inputs would be
   * null and "". If operating properly, a BadRequestException should be thrown.
   *
   * @param str1 - One of several data points from the @DataPoints(Invalid strings) collection of
   *     values.
   */
  @Theory
  public void T_findByName_Invalid(@FromDataPoints("Invalid strings") String str1) {
    assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByName(str1);
            });
  }

  /**
   * Tests findByName with nonexistent project. With a valid name there's still a possibility that
   * the project may not be valid. If operating properly, a ProjectNotFound should be thrown.
   */
  @Test
  public void T_findByName_EmptyProject() {
    when(testRepo.findByName(dummyString)).thenReturn(dummyListEmpty);
    assertThatExceptionOfType(ProjectNotFoundException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByName("arbitraryString");
            });
  }

  /**
   * Assert that findByName should return an ArrayList given a correct string parameter for
   * findByName(). Given valid input, a list should be returned and it should not be empty.
   */
  @Test
  public void T_findByName_Valid() {
    when(testRepo.findByName("dummyString")).thenReturn(dummyList);
    assertThat(classUnderTest.findByName("dummyString")).isInstanceOf(List.class);
    assertThat(classUnderTest.findByName("dummyString").size()).isGreaterThan(0);
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

  /** Passing Valid Id to findByTrainer */
  @Test
  public void T_findByTrainer_Valid() {
    when(testRepo.findByTrainer(Mockito.anyString())).thenReturn(dummyList);
    assertTrue(
        "Passed in String that is valid", classUnderTest.findByTrainer("id") instanceof List<?>);
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Theory
  public void T_findByTrainer_Invalid_Id(@FromDataPoints("string cases") String dummyId) {
    when(testRepo.findByTrainer(Mockito.anyString())).thenReturn(dummyListEmpty);
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByTrainer(dummyId);
            });
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Test
  public void T_findByTechStack_Valid() {
    when(testRepo.findByTechStack(Mockito.anyString())).thenReturn(dummyList);
    assertTrue(
        "Passed in String that is valid", classUnderTest.findByTechStack("id") instanceof List<?>);
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Theory
  public void T_findByTechStack_Invalid_Id(@FromDataPoints("string cases") String dummyId) {
    when(testRepo.findByTechStack(Mockito.anyString())).thenReturn(dummyListEmpty);
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByTechStack(dummyId);
            });
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Test
  public void T_findByStatus_Valid() {
    when(testRepo.findByStatus(Mockito.anyString())).thenReturn(dummyList);
    assertTrue(
        "Passed in String that is valid", classUnderTest.findByStatus("id") instanceof List<?>);
  }

  /** Assertion should verify that searching with a bad id value throws exception */
  @Theory
  public void T_findByStatus_Invalid_Id(@FromDataPoints("string cases") String dummyId) {
    when(testRepo.findByStatus(Mockito.anyString())).thenReturn(dummyListEmpty);
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByStatus(dummyId);
            });
  }

  /**
   * Valid Branches of findByUserId()
   *
   * @param userId
   */
  @Test
  public void T_findByUserId_Valid() {
    when(testRepo.findByUserId(1)).thenReturn(dummyList);
    assertTrue("Passes for UserId", classUnderTest.findByUserId(1) instanceof List<?>);
  }

  /**
   * All Invalid Branches of findByUserId()
   *
   * @param userId
   */
  @Theory
  public void T_findByUserId_Invalid(@FromDataPoints("int cases") Integer userId) {

    when(testRepo.findByUserId(userId)).thenReturn(dummyListEmpty);
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.findByUserId(userId);
            });
  }

  /** Assert that method should return false on a null parameter for deleteById(). */
  @Test
  public void T_deleteById_NullId() {
    assertThat(classUnderTest.deleteById(null)).isEqualTo(Boolean.FALSE);
  }

  /** Assert that method should return false on a empty parameter for deleteById(). */
  @Test
  public void T_deleteById_EmptyId() {
    assertThat(classUnderTest.deleteById("")).isEqualTo(Boolean.FALSE);
  }

  /** Assert that method should return true on a valid parameter for deleteById(). */
  @Test
  public void T_deleteById_Valid() {
    assertThat(classUnderTest.deleteById(dummyString)).isEqualTo(Boolean.TRUE);
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
   * Trainer is Null. The ScreenShots that are passed are an Empty array.
   */
  @Test
  public void T_updateProject_EmptyScreenShots() {
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
    when(dummyProject.getScreenShots()).thenReturn(new ArrayList<>());
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
   * implementation. The size of ScreenShots will be too large.
   *
   * @throws FileSizeTooLargeException
   */
  @Test
  public void T_createProjectFromDTO_InvalidScreenShot_Size() {
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

    assertThatExceptionOfType(FileSizeTooLargeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.createProjectFromDTO(mockProjectDTO);
            });
  }

  /**
   * Test if we can create a project from a DTO. We need the lists and such to properly mock the
   * implementation. The size of Data Models will be too large.
   *
   * @throws FileSizeTooLargeException
   */
  @Test
  public void T_createProjectFromDTO_InvalidDataModel_Size() {
    ArrayList<MultipartFile> listMultipartFile2 = new ArrayList<MultipartFile>();
    MultipartFile mockMultipartFile2 = Mockito.mock(MultipartFile.class);
    listMultipartFile2.add(mockMultipartFile2);

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getDataModel()).thenReturn(listMultipartFile2);
    when(mockMultipartFile.getSize()).thenReturn(1_000_000L);
    when(mockMultipartFile2.getSize()).thenReturn(1_000_001L);

    assertThatExceptionOfType(FileSizeTooLargeException.class)
        .isThrownBy(
            () -> {
              classUnderTest.createProjectFromDTO(mockProjectDTO);
            });
  }

  /**
   * Test if we can create a project from a DTO. We need the lists and such to properly mock the
   * implementation. The size of ZipLinks will be too large.
   *
   * @throws FileSizeTooLargeException
   */
  @Test
  public void T_createProjectFromDTO_InvalidZiplinks_Size() {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getDataModel()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
    when(mockMultipartFile.getSize()).thenReturn(1_000_000L);

    try {
      when(testFileService.download(Mockito.anyString())).thenReturn(mockFile);
      when(mockFile.length()).thenReturn(1_000_000_001L);
      assertThatExceptionOfType(FileSizeTooLargeException.class)
          .isThrownBy(
              () -> {
                classUnderTest.createProjectFromDTO(mockProjectDTO);
              });
    } catch (IOException e) {
      System.out.println("Exception thrown in Invalid Ziplinks Size");
      assertFalse(true);
      e.printStackTrace();
    }
  }

  @Test
  public void T_createProjectFromDTO_Valid() {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
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

  /** Passed in Null Ziplink to createProjectFromDTO(). Should throw exception. */
  @Test
  public void T_createProjectFromDTO_NullZiplinks() {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getDataModel()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getZipLinks()).thenReturn(null);
    when(testRepo.save(Mockito.any())).thenReturn(new Project());

    try {
      when(testFileService.download(Mockito.anyString())).thenReturn(mockFile);
      when(mockFile.length()).thenReturn(1_000L);
      when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

      assertThatExceptionOfType(ProjectNotAddedException.class)
          .isThrownBy(
              () -> {
                classUnderTest.createProjectFromDTO(mockProjectDTO);
              });

    } catch (Exception e) {
      System.out.println("Issue with createProjectFromDTO");
      e.printStackTrace();
      assertFalse(true);
    }
  }

  /** Passed in Null ScreenShots to createProjectFromDTO(). Should throw exception. */
  @Test
  public void T_createProjectFromDTO_NullScreenShots() {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(null);
    when(mockProjectDTO.getDataModel()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
    when(testRepo.save(Mockito.any())).thenReturn(new Project());

    try {
      when(testFileService.download(Mockito.anyString())).thenReturn(mockFile);
      when(mockFile.length()).thenReturn(1_000L);
      when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

      assertThatExceptionOfType(ProjectNotAddedException.class)
          .isThrownBy(
              () -> {
                classUnderTest.createProjectFromDTO(mockProjectDTO);
              });

    } catch (Exception e) {
      System.out.println("Issue with createProjectFromDTO");
      e.printStackTrace();
      assertFalse(true);
    }
  }

  /** Passed in Null DataModel to createProjectFromDTO(). Should throw exception. */
  @Test
  public void T_createProjectFromDTO_NullDataModel() {

    when(mockProjectDTO.getUserId()).thenReturn(1);
    when(mockProjectDTO.getName()).thenReturn(dummyString);
    when(mockProjectDTO.getBatch()).thenReturn(dummyString);
    when(mockProjectDTO.getTrainer()).thenReturn(dummyString);
    when(mockProjectDTO.getGroupMembers()).thenReturn(mockListString);
    when(mockProjectDTO.getTechStack()).thenReturn(dummyString);
    when(mockProjectDTO.getStatus()).thenReturn(dummyString);
    when(mockProjectDTO.getDescription()).thenReturn(dummyString);
    when(mockProjectDTO.getScreenShots()).thenReturn(listMultipartFile);
    when(mockProjectDTO.getDataModel()).thenReturn(null);
    when(mockProjectDTO.getZipLinks()).thenReturn(listZipLink);
    when(testRepo.save(Mockito.any())).thenReturn(new Project());

    try {
      when(testFileService.download(Mockito.anyString())).thenReturn(mockFile);
      when(mockFile.length()).thenReturn(1_000L);
      when(testStorage.store(mockMultipartFile)).thenReturn(dummyString);

      assertThatExceptionOfType(ProjectNotAddedException.class)
          .isThrownBy(
              () -> {
                classUnderTest.createProjectFromDTO(mockProjectDTO);
              });

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
        && dummyName == dummyTechStack) {
      assertTrue("Valid Use Case: Not to be Tested", true);
      return;
    }

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
