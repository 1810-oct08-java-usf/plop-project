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

/** Testing suite for the Project Controller class */
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

  /** Method to run before every test to initialize dependencies. */
  @Before
  public void setup() {
    project = new Project.ProjectBuilder().build();
    projectController = new ProjectController(projectService);
    projectDTO = new ProjectDTO.ProjectDTOBuilder().setBatch("Cabbage").setName("Kids")
        .setTechStack("Light, Water, Soil").build();
  }

  /** Test if addProject works when passed valid project. */
  @Test
  public void testAddProjectIfProjectValid() {

    when(projectService.createProjectFromDTO(projectDTO)).thenReturn(project);

    projectController.addProject(projectDTO.getName(), projectDTO.getBatch(), projectDTO.getTrainer(),
        projectDTO.getGroupMembers(), projectDTO.getScreenShots(), projectDTO.getZipLinks(),
        projectDTO.getDescription(), projectDTO.getTechStack(), projectDTO.getStatus(), projectDTO.getDataModel(),
        projectDTO.getUserId());

    verify(projectService).createProjectFromDTO(projectDTO);
  }

  /**
   * Test if addProject throws appropriate exception if passed projectDTO with no
   * batch info.
   */
  @Test
  public void testAddProjectIfNoBatchGiven() {

    projectDTO.setBatch(null);

    // exceptionRule.expect(ProjectNotAddedException.class);
    // exceptionRule.expectMessage("The 'batch' input cannot be empty when adding
    // project");

    projectController.addProject(projectDTO.getName(), projectDTO.getBatch(), projectDTO.getTrainer(),
        projectDTO.getGroupMembers(), projectDTO.getScreenShots(), projectDTO.getZipLinks(),
        projectDTO.getDescription(), projectDTO.getTechStack(), projectDTO.getStatus(), projectDTO.getDataModel(),
        projectDTO.getUserId());

    assertEquals(null, projectService.createProjectFromDTO(projectDTO));
  }

  /**
   * Test if addProject throws appropriate exception if passed projectDTO with no
   * project name.
   */
  @Test
  public void testAddProjectIfNoNameGiven() {

    projectDTO.setName(null);

    // exceptionRule.expect(ProjectNotAddedException.class);
    // exceptionRule.expectMessage("Empty/Invalid fields found on project");

    projectController.addProject(projectDTO.getName(), projectDTO.getBatch(), projectDTO.getTrainer(),
        projectDTO.getGroupMembers(), projectDTO.getScreenShots(), projectDTO.getZipLinks(),
        projectDTO.getDescription(), projectDTO.getTechStack(), projectDTO.getStatus(), projectDTO.getDataModel(),
        projectDTO.getUserId());

    assertEquals(null, projectService.createProjectFromDTO(projectDTO));
  }

  /**
   * Test if addProject throws appropriate exception if passed projectDTO with no
   * tech stack info.
   */
  @Test
  public void testAddProjectIfNoTechStackGiven() {

    projectDTO.setTechStack(null);

    // exceptionRule.expect(ProjectNotAddedException.class);
    // exceptionRule.expectMessage("The 'tech stack' input cannot be empty when
    // adding project");

    projectController.addProject(projectDTO.getName(), projectDTO.getBatch(), projectDTO.getTrainer(),
        projectDTO.getGroupMembers(), projectDTO.getScreenShots(), projectDTO.getZipLinks(),
        projectDTO.getDescription(), projectDTO.getTechStack(), projectDTO.getStatus(), projectDTO.getDataModel(),
        projectDTO.getUserId());

    assertEquals(null, projectService.createProjectFromDTO(projectDTO));
  }

  /**
   * Test Delete by id Test method for deleting by id if the input project id is
   * valid.
   */
  @Test
  public void testDeleteByIdIfIdValid() {
    project.setId("47");
    // when(projectService.findById("47")).thenReturn(project);

    when(projectService.deleteById("47")).thenReturn(true);

    assertEquals(true, projectController.deleteById("47"));
  }

  // testDeleteById
  // ------------------------------------------------------------------

  /**
   * Test for exception to be thrown when project not found in database via ID.
   */
  @Test
  public void testDeleteByIdIfNotFound() {

    // when(projectController.deleteById("47")).thenReturn(null);

    // exceptionRule.expect(ProjectNotFoundException.class);
    // exceptionRule.expectMessage("Project with id: 47, cannot be found to delete
    // this project.");

    project.setId("47");
    // when(projectService.findById("47")).thenReturn(project);

    when(projectService.deleteById("47")).thenReturn(false);

    assertEquals(false, projectController.deleteById("47"));
  }

  /** Test for handleExceptions in the case that */
  @Mock
  ProjectErrorResponse projectErrorResponse;

  @Mock
  ProjectNotFoundException projectNotFoundException;

  @Mock
  ProjectNotAddedException projectNotAddedException;

  // --------------------------------------------------------------------------------------------

  /** Test for returning a non-empty list of projects. */
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

  /** Test for returning list with only one project. */
  @Test
  public void testGetAllProjectsOnlyOneProject() {
    List<Project> projectList2 = new ArrayList<>();
    projectList2.add(project);

    when(projectService.findAllProjects()).thenReturn(projectList2);

    assertEquals(projectList2, projectController.getAllProjects());
  }

  // --------------------------------------------------------------------------
  /** Test for returning a list of projects by valid name */
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

  /** Test for returning a list of projects by valid batch */
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

  /** Test for returning a list of projects by status */
  @Test
  public void testGetProjectsByStatusIfValid() {
    Project project5 = new Project.ProjectBuilder().build();
    project5.setStatus("Approved");

    List<Project> projectList = new ArrayList<>();
    projectList.add(project5);

    when(projectService.findByStatus("Approved")).thenReturn(projectList);
    assertEquals(projectList, projectController.getProjectsByStatus("Approved"));
  }

  /** Test for returning a project by valid id */
  @Test
  public void testGetProjectByValidId() {
    project.setId("3");
    when(projectService.findById("3")).thenReturn(project);
    assertEquals(project, projectController.getProjectById("3"));
  }
}
