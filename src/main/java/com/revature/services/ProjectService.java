package com.revature.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

// TODO include transactional annotations to specify propagation and isolation levels
/** ProjectService provides an interface to interact with a ProjectRepository */
@Service
public class ProjectService {

  /** This is the project initial status. */
  private static final String INITIAL_PROJECT_STATUS = "Pending";

  ProjectRepository projectRepo;
  StorageService s3StorageServiceImpl;
  FileService fileService;

  @Autowired
  public ProjectService(ProjectRepository projectRepo, StorageService s3StorageServiceImpl, FileService fileService) {
    this.projectRepo = projectRepo;
    this.s3StorageServiceImpl = s3StorageServiceImpl;
    this.fileService = fileService;
  }

  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByUserId(Integer userId) {
	  
	  if (userId == null) {
		  throw new BadRequestException("Invalid fields found on project");
	    }

    List<Project> currProject = projectRepo.findByUserId(userId);

    if (currProject.isEmpty()) {
	      throw new ProjectNotFoundException(
		          "There is no project with id: " + userId + ", in the database.");
		    }
    
    return currProject;
  }

  /**
   * ProjectService.findByName retrieves a list of projects with a given name the transaction is
   * read-only and only reads committed data
   *
   * @param name the name of the project(s) you want to retrieve
   * @return a list of projects with the given name
   */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByName(String name) {
	  if (name == null) {
		  throw new BadRequestException("Invalid fields found on project");
	    }
	  
	  List<Project> project = projectRepo.findByBatch(name);
	  if (project.isEmpty()) {
	      throw new ProjectNotFoundException(
	          "There is no project named: " + name + ", in the database.");
	    }
    return project;
  }

  /**
   * ProjectService.findByBatch retrieves a list of projects with a given batch name the transaction
   * is read-only and only reads committed data
   *
   * @param name the batch for the project(s) you want to retrieve
   * @return a list of projects with the given batch
   */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByBatch(String batch) {
	  if (batch == null) {
		  throw new BadRequestException("Invalid fields found on project");
	    }
	  List<Project> result = projectRepo.findByBatch(batch);
	    if (result.isEmpty()) {
	      throw new ProjectNotFoundException(
	          "There is no project associated with batch: " + batch + ", in the database.");
	    }
    return result;
  }

  /**
   * ProjectService.findByTrainer retrieves a list of projects with a given trainer the transaction
   * is read-only and only reads committed data
   *
   * @param name the trainer for the project(s) you want to retrieve
   * @return a list of projects with the given trainer
   */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByTrainer(String trainer) {
	  
	  if (trainer == null) {
		  throw new BadRequestException("Invalid fields found on project");
	    }
	  
	  List<Project> project = projectRepo.findByTrainer(trainer);
	  if (project.isEmpty()) {
	      throw new ProjectNotFoundException(
	          "There is no project named: " + trainer + ", in the database.");
	    }
    return project;
  }

  /**
   * ProjectService.findByTechStack retrieves a list of projects with a given techStack the
   * transaction is read-only and only reads committed data
   *
   * @param name the techStack for the project(s) you want to retrieve
   * @return a list of projects with the given techStack
   */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByTechStack(String techStack) {
	  if (techStack == null) {
		  throw new BadRequestException("Invalid fields found on project");
	  }
	  List<Project> project = projectRepo.findByTechStack(techStack);
	  if (project.isEmpty()) {
	      throw new ProjectNotFoundException(
	          "There is no project named: " + techStack + ", in the database.");
	    }
    return project;
    
  }

  /**
   * ProjectService.findByStatus retrieves a list of projects with a given status The transaction is
   * read-only and only reads committed data
   *
   * @param status the status for the project(s) you want to retrieve
   * @return a list of projects with the given status
   */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByStatus(String status) {
	  
	  if (status == null) {
		  throw new BadRequestException("Invalid fields found on project");
	    }
	  List<Project> project = projectRepo.findByStatus(status);
	  if (project.isEmpty()) {
	      throw new ProjectNotFoundException(
	          "There is no project named: " + status + ", in the database.");
	    }
    return project;
  }

  /**
   * ProjectService.findAllProjects retrieve a list of all projects The transaction is read-only and
   * only reads committed data
   *
   * @return a list of all projects
   */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findAllProjects() {
	  
	  List<Project>  projects = projectRepo.findAll();
	  if (projects.isEmpty()) {
	      throw new ProjectNotFoundException("There are no projects in the database.");
	    }
    return projects;
  }

  /**
   * ProjectService.deleteById deletes a project with the given id Propagation.Requires_New makes
   * the transaction a new transaction to ensure a new delete transaction occurs each time.
   *
   * @param id an id for a project you want to delete
   * @return a boolean indicating if a project with the given id was deleted
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Boolean deleteById(String id) {
    if (id == null) {
      return false;
    } 
    projectRepo.deleteById(id);
    return true;
  }

  /**
   * Updates the project by taking in JSON values and mapping them to a Project model.
   * Propagation.Requires_New makes a new and different transaction occur each time.
   *
   * @param project: User entered JSON data for a project.
   * @param id: String data of an id.
   *     <p>Retrieves project from database through id. Checks whether a user has entered
   *     information for a specific value in the model. If they have then that is what is updated.
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Boolean updateProject(Project project) {
 
    if(!isValidFields(project)) {
    	return false;
    }
    
      projectRepo.save(project);
    return true;
  }

  /**
   * ProjectService.createProjectFromDTO accepts a ProjectDTO and persists a Project. Transaction
   * requires a new one every time to handle each new created object.
   *
   * <p>The screenShots field in the DTO contains MultipartFiles that are converted to Files and
   * stored. The Project screenShots field is populated with a list of links to those stored images.
   * The zipLinks field in the DTO contains links to github repositories. Zip archives are
   * downloaded from github for each repository and stored in S3. The Project's screenShots field is
   * populated with a list of links to those stored zip archives (Needs a unit test for zipLinks to
   * test fileSizeTooLargeException)
   *
   * @param projectDTO the data transfer object containing project details
   * @return the Project generated from the DTO
   *     <p>UPDATE (Testing Team): Implemented some validations for some properties that were
   *     considered require in the front-end, but are not enforced in the back-end. Setting initial
   *     status for the project, in this case it will be set to "Pending".
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Project createProjectFromDTO(ProjectDTO projectDTO) throws FileSizeTooLargeException {

    Project newProject =
        new Project.ProjectBuilder()
            .setName(projectDTO.getName())
            .setBatch(projectDTO.getBatch())
            .setTrainer(projectDTO.getTrainer())
            .setGroupMembers(projectDTO.getGroupMembers())
            .setDescription(projectDTO.getDescription())
            .setTechStack(projectDTO.getTechStack())
            .setStatus(projectDTO.getStatus())
            .setUserId(projectDTO.getUserId())
            .build();

    /*
     * Setting initial status of the project this way we can assure the project will
     * have this status even if the status is change in the front end.
     */
    newProject.setStatus(INITIAL_PROJECT_STATUS);

    if (!isValidFields(newProject)) {
		  throw new BadRequestException("Invalid fields found on project"); 		  
	    }

    // drop screenshot images in s3 and populate project with links to those images
    List<String> screenShotsList = new ArrayList<>();

    if (projectDTO.getScreenShots() == null) newProject.setScreenShots(screenShotsList);
    else {
      for (MultipartFile multipartFile : projectDTO.getScreenShots()) {
        if (multipartFile.getSize() > 1_000_000) {
          throw new FileSizeTooLargeException(
              "File size of screenshot: " + multipartFile.getName() + "is greater than 1MB.");
        }
        String endPoint = s3StorageServiceImpl.store(multipartFile);
        screenShotsList.add(endPoint);
      }
      newProject.setScreenShots(screenShotsList);
    }

    // load sql files in s3 and populate project with links to those files
    List<String> dataModelList = new ArrayList<>();

    if (projectDTO.getDataModel() == null) newProject.setDataModel(dataModelList);
    else {
      for (MultipartFile multipartFile : projectDTO.getDataModel()) {
        if (multipartFile.getSize() > 1_000_000) {
          throw new FileSizeTooLargeException(
              "File size of data model: " + multipartFile.getName() + "is greater than 1MB.");
        }
        String endPoint = s3StorageServiceImpl.store(multipartFile);
        dataModelList.add(endPoint);
      }
      newProject.setDataModel(dataModelList);
    }

    // download a zip archive for each repo from github and store them in our s3
    // bucket,
    // populating the project object with links to those zip files
    if (projectDTO.getZipLinks() == null) newProject.setZipLinks(new ArrayList<String>());
    else {
      for (String zipLink : projectDTO.getZipLinks()) {
        try {
          File zipArchive = fileService.download(zipLink + "/archive/master.zip");
          if (zipArchive.length() > 1_000_000_000) {
            throw new FileSizeTooLargeException(
                "The file size of: " + zipArchive.getName() + "exceeds 1GB");
          }
          newProject.addZipLink(s3StorageServiceImpl.store(zipArchive));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
   return projectRepo.save(newProject);
  }

  /** the transaction is read-only and only reads committed data */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public Project findById(String id) {
	  
	  if (id == null) {
		  throw new BadRequestException("Invalid fields found on project"); 		  
	    }

    Optional<Project> currProject = projectRepo.findById(id);

    if (!currProject.isPresent()) {
	      throw new ProjectNotFoundException(
		          "There is no project with id: " + id + ", in the database.");
		    }
    
    return currProject.get();
  }

  /**
   * Updates a Project to the database
   *
   * @param project - The updated project
   * @return boolean - true if the project was updated
   */
  public boolean submitEditRequest(Project project) {
   
	  if (!isValidFields(project)) {
      return false;
    }
    
	  projectRepo.save(project);
      return true;
  }
  
  public boolean isValidFields(Project project) {
	  
	  if(project.getDescription() == null || project.getDescription().trim().equals("")) return false;
	  if(project.getName() == null || project.getName().trim().equals("")) return false;
	  if(project.getBatch() == null || project.getBatch().trim().equals("")) return false;
	  if(project.getGroupMembers() == null || project.getGroupMembers().isEmpty()) return false;
	  if(project.getTechStack() == null || project.getTechStack().trim().equals("")) return false;
	  if(project.getTrainer() == null || project.getTrainer().equals("")) return false;
	  if(project.getUserId() == null || project.getUserId().equals(0)) return false;
	  
	  return true;
  }
}
