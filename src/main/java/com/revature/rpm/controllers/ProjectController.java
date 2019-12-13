package com.revature.rpm.controllers;

import com.revature.rpm.dtos.ProjectDTO;
import com.revature.rpm.dtos.ProjectErrorResponse;
import com.revature.rpm.entities.Project;
import com.revature.rpm.exceptions.BadRequestException;
import com.revature.rpm.exceptions.ProjectNotAddedException;
import com.revature.rpm.exceptions.ProjectNotFoundException;
import com.revature.rpm.services.ProjectService;
import com.revature.rpm.services.S3StorageServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

/** The ProjectController maps service endpoints for essential CRUD operations on Projects */
@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectController {

  private ProjectService projectService;
  private ByteArrayOutputStream downloadInputStream;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  /**
   * This methods allows us to hit the endpoint needed to download a project's screenshots.
   *
   * @param id - An ID used to uniquely identify a project.
   * @return JSON object with a decimal ASCII representation of all screenshots for the project.
   */
  @GetMapping(value = "/downloads/screenshots/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Byte[] downloadSceenShots(@PathVariable String id) {
    try {
      return projectService.codeBaseScreenShots(id);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * This method provides and endpoint to fetch datamodels from S3 bucket
   *
   * @param id - An ID used to uniquely identify a project.
   * @return data model in a response entity
   */
  @GetMapping(value = "/downloads/datamodels/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> downloadDataModels(@PathVariable String id) {
    String name = "datamodel.txt";
    byte[] media = projectService.codeBaseDataModels(id);
    return ResponseEntity.ok()
        .contentType(contentType(name))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name)
        .body(media);
  }

  /**
   * This method provides and endpoint to fetch ziplinks from S3 bucket
   *
   * @param id - An ID used to uniquely identify a project.
   * @return ziplinks in a response entity.
   */
  @GetMapping(value = "/downloads/ziplinks/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> downloadZipLinks(@PathVariable String id) {
    downloadInputStream = projectService.codeBaseZipLinks(id);
    return ResponseEntity.ok()
        .contentType(contentType("Oct-stream"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "\"")
        .body(downloadInputStream.toByteArray());
  }

  /**
   * @param id - An ID used to uniquely identify a project.
   * @return the updated project.
   */
  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Project getProjectById(@PathVariable String id) {

    return projectService.findById(id);
  }

  /**
   * @param field - A project property to target.
   * @param value - A project property's value that will be used to locate a project.
   * @return Project(s) that match the field and value.
   */
  @GetMapping(value = "/q", produces = "application/json")
  @ResponseStatus(HttpStatus.OK)
  public List<Project> getProjectFieldValue(
      @RequestParam("field") String field, @RequestParam("value") String value) {

    switch (field) {
      case "status":
        return projectService.findByStatus(value);
      case "name":
        return projectService.findByName(value);
      case "trainer":
        return projectService.findByTrainer(value);
      case "techStack":
        return projectService.findByTechStack(value);
      case "batch":
        return projectService.findByBatch(value);
      case "userId":
        return projectService.findByUserId(Integer.valueOf(value));
      case "all":
        return projectService.findAllProjects();
      default:
        throw new BadRequestException("Invalid field param value specified!");
    }
  }

  /**
   * This method accepts each field of a ProjectDTO object in the form of multipart form data. A
   * ProjectDTO object is created from the fields and sent to the service layer to be converted to a
   * Project object and saved,
   *
   * @param name - The name field of the form data
   * @param batch - The batch field of the form data
   * @param trainer - The trainer field of the form data
   * @param groupMembers - The groupMembers field of the form data
   * @param screenShots - The screenShots field of the form data
   * @param zipLinks - The zipLinks field of the form data
   * @param description - The description field of the form data
   * @param techStack - The techStack field of the form data
   * @param status - The status field of the form data
   * @return A project object created from ProjectDTO in the service layer.
   */
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  // @PreAuthorize("hasRole('USER')")
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
  
  @GetMapping("/{id}/screenshots")
  @ResponseStatus(HttpStatus.OK)
  public List<String> generatePreSignedUrls(@PathVariable String id) throws IOException{
	  return projectService.generatePreSignedUrls(id);
  }

  /**
   * This method is used to update an entry into the embedded MongoDB based on the ID via an HTTP
   * PUT request.
   *
   * @param project - A project with valid properties.
   */
  @PutMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ADMIN')")
  public Boolean updateProject(@RequestBody Project project) {

    return projectService.evaluateProject(project);
  }

  /**
   * This method is used to delete an entry into the embedded MongoDB based on the ID.
   *
   * @param id - An ID used to uniquely identify a project.
   * @return a boolean that signifies whether or not the delete was successful.
   */
  @DeleteMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Boolean deleteById(@PathVariable String id) {

    return projectService.deleteById(id);
  }

  /**
   * Ensures the proper content type is returned
   *
   * @param keyname - An AWS key/object.
   * @return content type of the key.
   */
  private MediaType contentType(String keyname) {
    String[] arr = keyname.split("\\.");
    String type = arr[arr.length - 1];
    switch (type) {
      case "txt":
        return MediaType.TEXT_PLAIN;
      case "png":
        return MediaType.IMAGE_PNG;
      case "jpg":
        return MediaType.IMAGE_JPEG;
      default:
        return MediaType.APPLICATION_OCTET_STREAM;
    }
  }

  /**
   * Uses @ExceptionHandler annotation. Creates a new error response error.setStatus: Defines the
   * value of the status code returned if thrown (BAD_REQUEST) error.setMessage: Defines a custom
   * message sent to the client if the exception is thrown error.setTimeStamp: Defines the time this
   * error was thrown. Is triggered in response to a ProjectNotAddedException.
   *
   * @param br - An instance of ProjectNotAddedException
   * @return an error with the appropriate response.
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

  /**
   * Uses @ExceptionHandler annotation. Creates a new error response error.setStatus: Defines the
   * value of the status code returned if thrown(NOT_FOUND) error.setMessage: Defines a custom
   * message sent to the client if the exception is thrown error.setTimeStamp: Defines the time this
   * error was thrown. Is triggered in response to a ProjectNotFoundException.
   *
   * @param pnfe - An instance of ProjectNotFoundException.
   * @return an error with the appropriate response.
   */
  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ProjectErrorResponse handleExceptions(ProjectNotFoundException pnfe) {
    ProjectErrorResponse error = new ProjectErrorResponse();
    error.setStatus(HttpStatus.NOT_FOUND.value());
    error.setMessage(pnfe.getMessage());
    error.setTimeStamp(System.currentTimeMillis());
    return error;
  }

  /**
   * Uses @ExceptionHandler annotation. Creates a new error response error.setStatus: Defines the
   * value of the status code returned if thrown(BAD_REQUEST) error.setMessage: Defines a custom
   * message sent to the client if the exception is thrown error.setTimeStamp: Defines the time this
   * error was thrown. Is triggered in response to a BadRequestException.
   *
   * @param br - An instance of BadRequestException
   * @return an error with the appropriate response.
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
