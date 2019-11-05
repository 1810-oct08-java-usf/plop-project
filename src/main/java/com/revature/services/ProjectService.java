package com.revature.services;

import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

import springfox.documentation.spring.web.paths.RelativePathProvider;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// TODO include transactional annotations to specify propagation and isolation levels
/** ProjectService provides an interface to interact with a ProjectRepository */
@Service
public class ProjectService {

  /** This is the project initial status. */
  private static final String INITIAL_PROJECT_STATUS = "Pending";

  private ProjectRepository projectRepo;
  private StorageService s3StorageServiceImpl;
  private FileService fileService;
  private ByteArrayOutputStream downloadInputStream;
  
  @Autowired
  public ProjectService(
      ProjectRepository projectRepo, StorageService s3StorageServiceImpl, FileService fileService) {
    this.projectRepo = projectRepo;
    this.s3StorageServiceImpl = s3StorageServiceImpl;
    this.fileService = fileService;
  }

  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public List<Project> findByUserId(Integer userId) {

    if (userId == null || userId <= 0) {
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
    if (name == null || name == "") {
      throw new BadRequestException("Invalid fields found on project");
    }

    List<Project> project = projectRepo.findByName(name);
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
    if (batch == null || batch == "") {
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

    if (trainer == null || trainer == "") {
      throw new BadRequestException("Invalid fields found on project");
    }

    List<Project> project = projectRepo.findByTrainer(trainer);
    if (project.isEmpty()) {
      throw new ProjectNotFoundException(
          "There is no trainer named: " + trainer + ", in the database.");
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
    if (techStack == null || techStack == "") {
      throw new BadRequestException("Invalid fields found on project");
    }
    List<Project> project = projectRepo.findByTechStack(techStack);
    if (project.isEmpty()) {
      throw new ProjectNotFoundException(
          "There is no tech stack named: " + techStack + ", in the database.");
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

    if (status == null || status == "") {
      throw new BadRequestException("Invalid fields found on project");
    }
    List<Project> project = projectRepo.findByStatus(status);
    if (project.isEmpty()) {
      throw new ProjectNotFoundException(
          "There is no project with: " + status + ", in the database.");
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

    List<Project> projects = projectRepo.findAll();
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

    if (id == null || id == "") {
      throw new ProjectNotFoundException("There is no project with: " + id + ", in the database.");
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
  public Boolean evaluateProject(Project project) {

    if (!isValidFields(project)) {
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
      throw new ProjectNotAddedException("Empty/Invalid fields found on project");
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
@Transactional
  public File codeBaseScreenShots(String id) throws IOException  {
	Project project =  findById(id);
	
	File file = null;
	
	 List<String> keys =  project.getScreenShots();
	  List<String> keyNames = new ArrayList<>();
	  System.out.println("The test key before loop is: " + keys);
	   
	    for(String key : keys) {
	    	String array1[]= key.split("/");
	    	String newKey = array1[2].toString();
	    	System.out.println("The test key after split is: " + newKey);
	    	keyNames.add(newKey);
	    	System.out.println("The testkey inside loop is: " + keyNames);
	    }
	    System.out.println("The tes key after loop is: " + keyNames);
	    
	    File newFile = new File("screenshot.png");
	    if(newFile.createNewFile()) {
	    	System.out.println("New file created in the root directory");
	    }else {
	    	System.out.println("File already exist");
	    }
	    byte [] esc = {'\n'};
	    OutputStream outStream = null;
	   outStream = new FileOutputStream(newFile);	
	    for(String key : keyNames) {
	    	this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
	    
	    	this.downloadInputStream.write(esc);
	    	this.downloadInputStream.writeTo(outStream);  
	    	    file = newFile;
	    }
	    
	return file;
  }

@Transactional
public File codeBaseDataModels(String id) throws IOException  {
	Project project =  findById(id);
	
	File file = null;
	
	 List<String> keys =  project.getDataModel();
	  List<String> keyNames = new ArrayList<>();
	  System.out.println("The test key before loop is: " + keys);
	   
	    for(String key : keys) {
	    	String array1[]= key.split("/");
	    	String newKey = array1[2].toString();
	    	System.out.println("The test key after split is: " + newKey);
	    	keyNames.add(newKey);
	    	System.out.println("The testkey inside loop is: " + keyNames);
	    }
	    System.out.println("The tes key after loop is: " + keyNames);
	    
	    File newFile = new File("datamodels.txt");
	    if(newFile.createNewFile()) {
	    	System.out.println("New file created in the root directory");
	    }else {
	    	System.out.println("File already exist");
	    }
	    byte [] esc = {'\n', 'E', 'N', 'D', 'O', 'F', 'F', 'I', 'L', 'E'};
	    OutputStream outStream = null;
	   outStream = new FileOutputStream(newFile);	
	    for(String key : keyNames) {
	    	this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
	    
	    	this.downloadInputStream.write(esc);
	    	this.downloadInputStream.writeTo(outStream); 
	    	file = newFile;
	    }
	outStream.close();
 
	return file;
}

@Transactional
public ByteArrayOutputStream codeBaseZipLinks(String id) throws IOException  {
	Project project =  findById(id);
	
	 List<String> keys =  project.getZipLinks();
	  List<String> keyNames = new ArrayList<>();
	  System.out.println("The test key before loop is: " + keys);
	   
	    for(String key : keys) {
	    	String array1[]= key.split("/");
	    	String newKey = array1[2].toString();
	    	System.out.println("The test key after split is: " + newKey);
	    	keyNames.add(newKey);
	    	System.out.println("The testkey inside loop is: " + keyNames);
	    }
	    System.out.println("The tes key after loop is: " + keyNames);
	    
	    OutputStream outStream = null;
	    outStream = new FileOutputStream(keyNames.toString());
	   
	    for(String key : keyNames) {
	    	this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
	    	this.downloadInputStream.writeTo(outStream); 
	    	
	    }
	    outStream.close();
	return downloadInputStream;
}

	@Transactional
	public ZipOutputStream zipFile(File fileToZip) throws IOException {

		FileOutputStream fos = new FileOutputStream("ScreenShots.zip");
		
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		zipOut.close();
		fis.close();
		fos.close();
		return zipOut;
	}


  public boolean isValidFields(Project project) {

    if (project.getDescription() == null || project.getDescription().trim().equals(""))
      return false;
    if (project.getName() == null || project.getName().trim().equals("")) return false;
    if (project.getBatch() == null || project.getBatch().trim().equals("")) return false;
    if (project.getGroupMembers() == null || project.getGroupMembers().isEmpty()) return false;
    if (project.getTechStack() == null || project.getTechStack().trim().equals("")) return false;
    if (project.getTrainer() == null || project.getTrainer().equals("")) return false;
    if (project.getUserId() == null || project.getUserId().equals(0)) return false;

    return true;
  }
}
