package com.revature.controllers;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;

import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

/**
 * The ProjectController maps service endpoints for essential CRUD operations on
 * Projects
 */
@RestController
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectController {

  private ProjectService projectService;
  private ByteArrayOutputStream downloadInputStream;

  @Autowired
  private ServletContext servletContext;

  @Autowired
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  /**
   * This methods allows us to hit the endpoint needed to download a project's screenshots. 
   *
   * @param String Project Id
   * @return Screenshots - JSON object with a decimal ASCII representation of all screenshots for the project.
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
 * @param id
 * @return datamodel in a response entity 
 */
@GetMapping(value = "/downloads/datamodels/{id}")
@ResponseStatus(HttpStatus.OK)
public ResponseEntity<byte[]> downloadDataModels(@PathVariable String id)  {
   String name = "datamodel.txt";
    byte[] media = projectService.codeBaseDataModels(id);
    return ResponseEntity.ok()
            .contentType(contentType(name))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name )
            .body(media);
}

/**
   * This method provides and endpoint to fetch ziplinks from S3 bucket
   * @param id
   * @return ziplinks in a response entity
   */
  @GetMapping(value = "/downloads/ziplinks/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<byte[]> downloadZipLinks(@PathVariable String id)  {
		  downloadInputStream = projectService.codeBaseZipLinks(id);
		  return ResponseEntity.ok()
			        .contentType(contentType("Oct-stream"))
			        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""  + "\"")
			        .body(downloadInputStream.toByteArray());
  }

  @RequestMapping(value = "/image-resource", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Resource> getImageAsResource() {
    final HttpHeaders headers = new HttpHeaders();
    Resource resource =
        new ServletContextResource(servletContext, "/src/main/resources/screenshot.txt");
    return new ResponseEntity<>(resource, headers, HttpStatus.OK);
  }

  /**
   * Ensures the proper content type is returned
   *
   * @param keyname
   * @return content type of the key we are looking for
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

  @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Project getProjectById(@PathVariable String id) {

    return projectService.findById(id);
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
  @PreAuthorize("hasRole('ADMIN')")
  public Boolean updateProject(@RequestBody Project project) {

    return projectService.evaluateProject(project);
  }

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
