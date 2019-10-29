package com.revature.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.InvalidStatusException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;

/** The ProjectController maps service endpoints for essential CRUD operations on Projects */
@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectController {

  private ProjectService projectService;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  /**
   * This method retrieves all of the projects stored within embedded MongoDB Uses HTTP method GET
   * and only retrieves JSON data. <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<Project> getAllProjects() {
    
    return projectService.findAllProjects();
  }

  /**
   * This method retrieves project by ID Uses HTTP method GET and only retrieves JSON data <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database
   *
   * @param id: String that serves as the id for the project
   */
  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Project getProjectById(@PathVariable String id) {
    
    return projectService.findById(id);
  }

  @GetMapping(value = "/userId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<Project> getProjectByUserId(@PathVariable Integer userId) {
    System.out.println("In Project Controller getProjectById " + userId);
        return projectService.findByUserId(userId);
  }

  /**
   * This method retrieves project by name Uses HTTP method GET and only retrieves JSON data <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database
   *
   * @param name: String that serves as the name of the project
   */
  @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<Project> getProjectsByName(@PathVariable String name) {
    
    return projectService.findByName(name);
  }

  /**
   * This method retrieves project by batch Uses HTTP method GET and only retrieves JSON data <br>
   * <br>
   * Added Spring Security annotations to prevent outside users from accessing database
   *
   * @param batch: String that serves as the batch for the project
   */
  @GetMapping(value = "/batch/{batch}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<Project> getProjectsByBatch(@PathVariable String batch) {
    
    return projectService.findByBatch(batch);
  }

  /**
   * This method retrieves project by status Uses HTTP method GET and only retrieves JSON data <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database
   *
   * @param status: String that serves as the status of the project
   */
  @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<Project> getProjectsByStatus(@PathVariable String status) {
    
    return projectService.findByStatus(status);
  }

  /**
   * This method allows a user to submit an edit request on one of their projects. Uses HTTP method
   * POST and only consumes JSON data.
   *
   * @param project: The new project object for the submitted edit request.
   */
  @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public boolean submitEditRequest(@RequestBody Project project) {
    return projectService.submitEditRequest(project);
  }

  /**
   * This method accepts each field of a ProjectDTO object in the form of multipart form data. A
   * ProjectDTO object is created from the fields and sent to the service layer to be converted to a
   * Project object and saved. <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database
   *
   * @param name the name field of the form data
   * @param batch the batch field of the form data
   * @param trainer the trainer field of the form data
   * @param groupMembers the groupMembers field of the form data
   * @param screenShots the screenShots field of the form data
   * @param zipLinks the zipLinks field of the form data
   * @param description the description field of the form data
   * @param techStack the techStack field of the form data
   * @param status the status field of the form data
   * @return project: The Project object derived from ProjectDTO in the service layer.
   */
  @PostMapping(
      value = "/",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  //@PreAuthorize("hasRole('USER')")
  public Project addProject(
      @RequestParam("name") String name,
      @RequestParam("batch") String batch,
      @RequestParam("trainer") String trainer,
      @RequestParam("groupMembers") List<String> groupMembers,
      @RequestParam("screenShots") List<MultipartFile> screenShots,
      @RequestParam("zipLinks") List<String> zipLinks,
      @RequestParam("description") String description,
      @RequestParam("techStack") String techStack,
      @RequestParam("status") String status,
      @RequestParam("dataModel") List<MultipartFile> dataModel,
      @RequestParam("userId") Integer userId) {
   
    ProjectDTO projectDTO =
        new ProjectDTO.ProjectDTOBuilder()
            .setName(name)
            .setBatch(batch)
            .setTrainer(trainer)
            .setGroupMembers(groupMembers)
            .setScreenShots(screenShots)
            .setZipLinks(zipLinks)
            .setDescription(description)
            .setTechStack(techStack)
            .setStatus(status)
            .setDataModel(dataModel)
            .setUserId(userId)
            .build();

    return projectService.createProjectFromDTO(projectDTO);
  }

  /**
   * This method is used to delete an entry into the embedded MongoDB based on the ID <br>
   * <br>
   * Uses HTTP method DELETE and only retrieves JSON data <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database
   *
   * @param id: String that serves as the id for the project
   */
  @DeleteMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Boolean deleteById(@PathVariable String id) {
   
    return projectService.deleteById(id);
  }

  /**
   * This method is used to update an entry into the embedded MongoDB based on the ID <br>
   * <br>
   * Added Spring Security annotations to prevent unauthorized users from accessing database <br>
   * <br>
   * Uses HTTP method PUT. Retrieves and produces JSON data
   *
   * @param project: Requests that the user enters a project
   * @param id: String that serves as the id for the project
   */
  @PutMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  //	@PreAuthorize("hasRole('ADMIN')")
  public Boolean updateProject(@RequestBody Project project) {
   
    return projectService.updateProject(project);
  }

  /**
   * This method is used to send a status code into the client based on the validity of the
   * information sent. <br>
   * <br>
   * Exception Handler for Response Status Bad Request which is used for addProject() [/add] <br>
   * <br>
   * Uses @ExceptionHandler annotation. Creates a new error response error.setStatus: Defines the
   * value of the status code returned if thrown (BAD_REQUEST) error.setMessage: Defines a custom
   * message sent to the client if the exception is thrown error.setTimeStamp: Defines the time this
   * error was thrown
   */
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProjectErrorResponse handleExceptions(ProjectNotAddedException br) {
    ProjectErrorResponse error = new ProjectErrorResponse();
    error.setStatus(HttpStatus.BAD_REQUEST.value());
    error.setMessage(br.getMessage());
    error.setTimeStamp(System.currentTimeMillis());
    return error;
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProjectErrorResponse handleExceptions(ProjectNotFoundException pnae) {
    ProjectErrorResponse error = new ProjectErrorResponse();
    error.setStatus(HttpStatus.NOT_FOUND.value());
    error.setMessage(pnae.getMessage());
    error.setTimeStamp(System.currentTimeMillis());
    return error;
  }

  /**
   * This method is used to send a status code into the client based on the validity of the
   * information sent. <br>
   * <br>
   * Exception Handler for Invalid Status Response which is used for updateProject() <br>
   * <br>
   * Uses @ExceptionHandler annotation. Creates a new error response error.setStatus: Defines the
   * value of the status code returned if thrown(BAD_REQUEST) error.setMessage: Defines a custom
   * message sent to the client if the exception is thrown error.setTimeStamp: Defines the time this
   * error was thrown
   */
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProjectErrorResponse handleExceptions(BadRequestException br) {
    ProjectErrorResponse error = new ProjectErrorResponse();
    error.setStatus(HttpStatus.BAD_REQUEST.value());
    error.setMessage(br.getMessage());
    error.setTimeStamp(System.currentTimeMillis());
    return error;
  }
}
