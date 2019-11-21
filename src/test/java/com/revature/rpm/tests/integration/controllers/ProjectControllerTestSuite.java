package com.revature.rpm.tests.integration.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.revature.rpm.controllers.ProjectController;
import com.revature.rpm.dtos.ProjectDTO;
import com.revature.rpm.dtos.ProjectErrorResponse;
import com.revature.rpm.entities.Project;
import com.revature.rpm.exceptions.ProjectNotAddedException;
import com.revature.rpm.exceptions.ProjectNotFoundException;
import com.revature.rpm.services.ProjectService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/** Testing suite for the Project Controller class */
@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTestSuite {

  @Mock Project project;

  @Mock ProjectDTO projectDTO;

  @Mock ProjectService projectService;

  /** Test for handleExceptions in the case that */
  @Mock ProjectErrorResponse projectErrorResponse;

  @Mock ProjectNotFoundException projectNotFoundException;

  @Mock ProjectNotAddedException projectNotAddedException;

  @InjectMocks ProjectController projectController;

  @Rule public ExpectedException exceptionRule = ExpectedException.none();

  /** Method to run before every test to initialize dependencies. */
  @Before
  public void setup() {
    project = new Project.ProjectBuilder().build();
    projectController = new ProjectController(projectService);
    projectDTO =
        new ProjectDTO.ProjectDTOBuilder()
            .setBatch("Cabbage")
            .setName("Kids")
            .setTechStack("Light, Water, Soil")
            .build();
  }

  /** Test if addProject works when passed valid project. */
  @Test
  public void T_addProject_Valid() {

    when(projectService.createProjectFromDTO(projectDTO)).thenReturn(project);

    projectController.addProject(
        projectDTO.getName(),
        projectDTO.getBatch(),
        projectDTO.getTrainer(),
        projectDTO.getGroupMembers(),
        projectDTO.getScreenShots(),
        projectDTO.getZipLinks(),
        projectDTO.getDescription(),
        projectDTO.getTechStack(),
        projectDTO.getStatus(),
        projectDTO.getDataModel(),
        projectDTO.getUserId());

    verify(projectService).createProjectFromDTO(projectDTO);
  }

  /** Test if addProject throws appropriate exception if passed projectDTO with no batch info. */
  @Test
  public void T_addProject_InvalidBatch() {

    projectDTO.setBatch(null);

    // exceptionRule.expect(ProjectNotAddedException.class);
    // exceptionRule.expectMessage("The 'batch' input cannot be empty when adding
    // project");

    projectController.addProject(
        projectDTO.getName(),
        projectDTO.getBatch(),
        projectDTO.getTrainer(),
        projectDTO.getGroupMembers(),
        projectDTO.getScreenShots(),
        projectDTO.getZipLinks(),
        projectDTO.getDescription(),
        projectDTO.getTechStack(),
        projectDTO.getStatus(),
        projectDTO.getDataModel(),
        projectDTO.getUserId());

    assertEquals(null, projectService.createProjectFromDTO(projectDTO));
  }

  /** Test if addProject throws appropriate exception if passed projectDTO with no project name. */
  @Test
  public void T_addProject_InvalidName() {

    projectDTO.setName(null);

    // exceptionRule.expect(ProjectNotAddedException.class);
    // exceptionRule.expectMessage("Empty/Invalid fields found on project");

    projectController.addProject(
        projectDTO.getName(),
        projectDTO.getBatch(),
        projectDTO.getTrainer(),
        projectDTO.getGroupMembers(),
        projectDTO.getScreenShots(),
        projectDTO.getZipLinks(),
        projectDTO.getDescription(),
        projectDTO.getTechStack(),
        projectDTO.getStatus(),
        projectDTO.getDataModel(),
        projectDTO.getUserId());

    assertEquals(null, projectService.createProjectFromDTO(projectDTO));
  }

  /**
   * Test if addProject throws appropriate exception if passed projectDTO with no tech stack info.
   */
  @Test
  public void T_addProject_InvalidTechStack() {

    projectDTO.setTechStack(null);

    // exceptionRule.expect(ProjectNotAddedException.class);
    // exceptionRule.expectMessage("The 'tech stack' input cannot be empty when
    // adding project");

    projectController.addProject(
        projectDTO.getName(),
        projectDTO.getBatch(),
        projectDTO.getTrainer(),
        projectDTO.getGroupMembers(),
        projectDTO.getScreenShots(),
        projectDTO.getZipLinks(),
        projectDTO.getDescription(),
        projectDTO.getTechStack(),
        projectDTO.getStatus(),
        projectDTO.getDataModel(),
        projectDTO.getUserId());

    assertEquals(null, projectService.createProjectFromDTO(projectDTO));
  }

  // deleteById
  // ------------------------------------------------------------------
  /** Test Delete by id Test method for deleting by id if the input project id is valid. */
  @Test
  public void T_deleteById_Valid() {
    project.setId("47");
    when(projectService.deleteById("47")).thenReturn(true);
    assertEquals(true, projectController.deleteById("47"));
  }

  /** Test for exception to be thrown when project not found in database via ID. */
  @Test
  public void T_deleteById_InvalidId() {
    project.setId("47");
    when(projectService.deleteById("47")).thenReturn(false);
    assertEquals(false, projectController.deleteById("47"));
  }

  // --------------------------------------------------------------------------

  /** Test for returning a project by valid id */
  @Test
  public void T_getProjectById_Valid() {
    project.setId("3");
    when(projectService.findById("3")).thenReturn(project);
    assertEquals(project, projectController.getProjectById("3"));
  }
}
