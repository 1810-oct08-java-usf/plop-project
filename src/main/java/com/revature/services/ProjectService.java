<<<<<<< HEAD
package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.apache.commons.io.IOUtils;
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
	public ProjectService(ProjectRepository projectRepo, StorageService s3StorageServiceImpl, FileService fileService) {
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
			throw new ProjectNotFoundException("There is no project with id: " + userId + ", in the database.");
		}

		return currProject;
	}

	/**
	 * ProjectService.findByName retrieves a list of projects with a given name the
	 * transaction is read-only and only reads committed data
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
			throw new ProjectNotFoundException("There is no project named: " + name + ", in the database.");
		}
		return project;
	}

	/**
	 * ProjectService.findByBatch retrieves a list of projects with a given batch
	 * name the transaction is read-only and only reads committed data
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
	 * ProjectService.findByTrainer retrieves a list of projects with a given
	 * trainer the transaction is read-only and only reads committed data
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
			throw new ProjectNotFoundException("There is no trainer named: " + trainer + ", in the database.");
		}
		return project;
	}

	/**
	 * ProjectService.findByTechStack retrieves a list of projects with a given
	 * techStack the transaction is read-only and only reads committed data
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
			throw new ProjectNotFoundException("There is no tech stack named: " + techStack + ", in the database.");
		}
		return project;
	}

	/**
	 * ProjectService.findByStatus retrieves a list of projects with a given status
	 * The transaction is read-only and only reads committed data
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
			throw new ProjectNotFoundException("There is no project with: " + status + ", in the database.");
		}
		return project;
	}

	/**
	 * ProjectService.findAllProjects retrieve a list of all projects The
	 * transaction is read-only and only reads committed data
	 *
	 * @return a list of all projects
	 */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	public List<Project> findAllProjects() {

		List<Project> projects = projectRepo.findAll();
		System.out.println(projects);
		if (projects.isEmpty()) {
			throw new ProjectNotFoundException("There are no projects in the database.");
		}
		return projects;
	}

	/**
	 * ProjectService.deleteById deletes a project with the given id
	 * Propagation.Requires_New makes the transaction a new transaction to ensure a
	 * new delete transaction occurs each time.
	 *
	 * @param id an id for a project you want to delete
	 * @return a boolean indicating if a project with the given id was deleted
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean deleteById(String id) {

		if (id == null || id == "") {
			// throw new ProjectNotFoundException("There is no project with: " + id + ", in
			// the
			// database.");
			System.out.println("Invalid id passed within deleteById");
			return false;
		}

		projectRepo.deleteById(id);
		return true;
	}

	/**
	 * Updates the project by taking in JSON values and mapping them to a Project
	 * model. Propagation.Requires_New makes a new and different transaction occur
	 * each time.
	 *
	 * @param project: User entered JSON data for a project.
	 * @param id:      String data of an id.
	 *                 <p>
	 *                 Retrieves project from database through id. Checks whether a
	 *                 user has entered information for a specific value in the
	 *                 model. If they have then that is what is updated.
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
	 * ProjectService.createProjectFromDTO accepts a ProjectDTO and persists a
	 * Project. Transaction requires a new one every time to handle each new created
	 * object.
	 *
	 * <p>
	 * The screenShots field in the DTO contains MultipartFiles that are converted
	 * to Files and stored. The Project screenShots field is populated with a list
	 * of links to those stored images. The zipLinks field in the DTO contains links
	 * to github repositories. Zip archives are downloaded from github for each
	 * repository and stored in S3. The Project's screenShots field is populated
	 * with a list of links to those stored zip archives (Needs a unit test for
	 * zipLinks to test fileSizeTooLargeException)
	 *
	 * @param projectDTO the data transfer object containing project details
	 * @return the Project generated from the DTO
	 *         <p>
	 *         UPDATE (Testing Team): Implemented some validations for some
	 *         properties that were considered require in the front-end, but are not
	 *         enforced in the back-end. Setting initial status for the project, in
	 *         this case it will be set to "Pending".
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Project createProjectFromDTO(ProjectDTO projectDTO) throws FileSizeTooLargeException {

		Project newProject = new Project.ProjectBuilder().setUserId(projectDTO.getUserId())
				.setDescription(projectDTO.getDescription()).setName(projectDTO.getName())
				.setBatch(projectDTO.getBatch()).setGroupMembers(projectDTO.getGroupMembers())
				.setTechStack(projectDTO.getTechStack()).setTrainer(projectDTO.getTrainer())
				.setStatus(INITIAL_PROJECT_STATUS).build();

		// drop screenshot images in s3 and populate project with links to those images
		List<String> screenShotsList = new ArrayList<>();

		if (projectDTO.getScreenShots() == null)
			throw new ProjectNotAddedException("ScreenShots not Present for Project");
		else {
			for (MultipartFile multipartFile : projectDTO.getScreenShots()) {
				// System.out.println("Within for loop for screenshot: " +
				// multipartFile.getName());
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

		if (projectDTO.getDataModel() == null)
			throw new ProjectNotAddedException("ScreenShots not Present for Project");
		else {
			for (MultipartFile multipartFile : projectDTO.getDataModel()) {
				System.out.println("Within for loop for sql Files: " + multipartFile.getName());
				if (multipartFile.getSize() > 1_000_000) {
					throw new FileSizeTooLargeException(
							"File size of data model: " + multipartFile.getName() + "is greater than 1MB.");
				}
				String endPoint = s3StorageServiceImpl.store(multipartFile);
				dataModelList.add(endPoint);
			}
			newProject.setDataModel(dataModelList);
		}

		List<String> zipLinks = projectDTO.getZipLinks();
		// download a zip archive for each repo from github and store them in our s3
		// bucket,
		// populating the project object with links to those zip files
		if (projectDTO.getZipLinks() == null)
			throw new ProjectNotAddedException("Ziplinks not present for Project");
		else {
			for (String zipLink : zipLinks) {
				try {
					System.out.println("Within for loop for Ziplink: " + zipLink);
					File zipArchive = fileService.download(zipLink + "/archive/master.zip");
					if (zipArchive.length() > 1_000_000_000) {
						throw new FileSizeTooLargeException(
								"The file size of: " + zipArchive.getName() + "exceeds 1GB");
					}
					System.out.println("Within for loop for Ziplink: File was not too big");
					String end = s3StorageServiceImpl.store(zipArchive);
					System.out.println("Within for loop for Ziplink: File was stored");
					newProject.addZipLink(end);
					System.out.println("Within for loop for Ziplink: File was added");

				} catch (IOException e) {
					System.out.println("Within for loop for Ziplink: Exception was thrown = " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (!isValidFields(newProject)) {
			throw new ProjectNotAddedException("Empty/Invalid fields found on project");
		}

		Project result = projectRepo.save(newProject);
		System.out.println("Finished result: " + result);
		return result;
	}

	/** the transaction is read-only and only reads committed data */
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	public Project findById(String id) {

		if (id == null || id.trim().isEmpty()) {
			throw new BadRequestException("Invalid fields found on project");
		}

		Optional<Project> currProject = projectRepo.findById(id);

		if (!currProject.isPresent()) {
			throw new ProjectNotFoundException("There is no project with id: " + id + ", in the database.");
		}

		return currProject.get();
	}

	/**
	 * This methods takes datamodels downloaded from the S3 bucket and put the in a single file 
	 * @param id
	 * @return name of file that data models are stored in
	 * @throws IOException
	 */
	@Transactional
	public byte[] codeBaseDataModels(String id){
		Project project = findById(id);

		File file = null;

		List<String> keys = project.getDataModel();
		List<String> keyNames = new ArrayList<>();

		for (String key : keys) {
			String array1[] = key.split("/");
			String newKey = array1[2].toString();

			keyNames.add(newKey);

		}
		try {
		File newFile = new File("datamodels.txt");
		if (newFile.createNewFile()) {
			System.out.println("New file created in the root directory");
		} else {
			System.out.println("File already exist");
		}
		byte[] esc = { '\n', 'E', 'N', 'D', 'O', 'F', 'F', 'I', 'L', 'E' };
		OutputStream outStream = null;
		outStream = new FileOutputStream(newFile);
		for (String key : keyNames) {
			this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);

			this.downloadInputStream.write(esc);
			this.downloadInputStream.writeTo(outStream);
			file = newFile;
		}
		outStream.close();

		InputStream in =  new FileInputStream(file.getName());
	    byte[] media = IOUtils.toByteArray(in);
	    
	    return media;
		}catch(IOException ioe) {
			ioe.getMessage();
		}
		return null;
	}

	/**
	 * This methods takes ziplinks downloaded from S3 buck and put then as a stream of bytes
	 * @param id
	 * @return a stream of byes representing the ziplinks
	 * @throws IOException
	 */
	@Transactional
	public ByteArrayOutputStream codeBaseZipLinks(String id) {
		
		Project project = findById(id);

		List<String> keys = project.getZipLinks();
		List<String> keyNames = new ArrayList<>();

		for (String key : keys) {
			String array1[] = key.split("/");
			String newKey = array1[2].toString();

			keyNames.add(newKey);

		}
		try {
		OutputStream outStream = null;
		outStream = new FileOutputStream(keyNames.toString());

		for (String key : keyNames) {
			this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
			this.downloadInputStream.writeTo(outStream);

		}
		outStream.close();
		return downloadInputStream;
		}catch(IOException ioe) {
			ioe.getMessage();
		}
		return null;
	}

	/**
	 * Checks UserId, Description, Name, Batch, GroupMembers, TechStack, Trainer,
	 * and Ziplinks if they are valid fields.
	 *
	 * @param project
	 * @return true on fields are valid
	 */
	private boolean isValidFields(Project project) {
		if (project.getUserId() == null || project.getUserId() < 1 || project.getDescription() == null
				|| project.getDescription().trim().equals("") || project.getName() == null
				|| project.getName().trim().equals("") || project.getBatch() == null
				|| project.getBatch().trim().equals("") || project.getGroupMembers() == null
				|| project.getGroupMembers().isEmpty() || project.getTechStack() == null
				|| project.getTechStack().trim().equals("") || project.getTrainer() == null
				|| project.getTrainer().equals("") || project.getZipLinks() == null || project.getZipLinks().isEmpty()
				|| project.getDataModel() == null || project.getDataModel().isEmpty()
				|| project.getScreenShots() == null || project.getScreenShots().isEmpty())
			return false;
		else
			return true;

	}
}
=======
package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    System.out.println(projects);
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
      // throw new ProjectNotFoundException("There is no project with: " + id + ", in the
      // database.");
      System.out.println("Invalid id passed within deleteById");
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
            .setUserId(projectDTO.getUserId())
            .setDescription(projectDTO.getDescription())
            .setName(projectDTO.getName())
            .setBatch(projectDTO.getBatch())
            .setGroupMembers(projectDTO.getGroupMembers())
            .setTechStack(projectDTO.getTechStack())
            .setTrainer(projectDTO.getTrainer())
            .setStatus(INITIAL_PROJECT_STATUS)
            .build();

    // drop screenshot images in s3 and populate project with links to those images
    List<String> screenShotsList = new ArrayList<>();

    if (projectDTO.getScreenShots() == null)
      throw new ProjectNotAddedException("ScreenShots not Present for Project");
    else {
      for (MultipartFile multipartFile : projectDTO.getScreenShots()) {
        // System.out.println("Within for loop for screenshot: " + multipartFile.getName());
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

    if (projectDTO.getDataModel() == null)
      throw new ProjectNotAddedException("ScreenShots not Present for Project");
    else {
      for (MultipartFile multipartFile : projectDTO.getDataModel()) {
        System.out.println("Within for loop for sql Files: " + multipartFile.getName());
        if (multipartFile.getSize() > 1_000_000) {
          throw new FileSizeTooLargeException(
              "File size of data model: " + multipartFile.getName() + "is greater than 1MB.");
        }
        String endPoint = s3StorageServiceImpl.store(multipartFile);
        dataModelList.add(endPoint);
      }
      newProject.setDataModel(dataModelList);
    }

    List<String> zipLinks = projectDTO.getZipLinks();
    // download a zip archive for each repo from github and store them in our s3
    // bucket,
    // populating the project object with links to those zip files
    if (projectDTO.getZipLinks() == null)
      throw new ProjectNotAddedException("Ziplinks not present for Project");
    else {
      for (String zipLink : zipLinks) {
        try {
          System.out.println("Within for loop for Ziplink: " + zipLink);
          File zipArchive = fileService.download(zipLink + "/archive/master.zip");
          if (zipArchive.length() > 1_000_000_000) {
            throw new FileSizeTooLargeException(
                "The file size of: " + zipArchive.getName() + "exceeds 1GB");
          }
          System.out.println("Within for loop for Ziplink: File was not too big");
          String end = s3StorageServiceImpl.store(zipArchive);
          System.out.println("Within for loop for Ziplink: File was stored");
          newProject.addZipLink(end);
          System.out.println("Within for loop for Ziplink: File was added");

        } catch (IOException e) {
          System.out.println(
              "Within for loop for Ziplink: Exception was thrown = " + e.getMessage());
          e.printStackTrace();
        }
      }
    }

    if (!isValidFields(newProject)) {
      throw new ProjectNotAddedException("Empty/Invalid fields found on project");
    }

    Project result = projectRepo.save(newProject);
    System.out.println("Finished result: " + result);
    return result;
  }

  /** the transaction is read-only and only reads committed data */
  @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
  public Project findById(String id) {

    if (id == null || id.trim().isEmpty()) {
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
   * Checks UserId, Description, Name, Batch, GroupMembers, TechStack, Trainer, and Ziplinks if they
   * are valid fields.
   *
   * @param project
   * @return true on fields are valid
   */
  private boolean isValidFields(Project project) {
    if (project.getUserId() == null
        || project.getUserId() < 1
        || project.getDescription() == null
        || project.getDescription().trim().equals("")
        || project.getName() == null
        || project.getName().trim().equals("")
        || project.getBatch() == null
        || project.getBatch().trim().equals("")
        || project.getGroupMembers() == null
        || project.getGroupMembers().isEmpty()
        || project.getTechStack() == null
        || project.getTechStack().trim().equals("")
        || project.getTrainer() == null
        || project.getTrainer().equals("")
        || project.getZipLinks() == null
        || project.getZipLinks().isEmpty()
        || project.getDataModel() == null
        || project.getDataModel().isEmpty()
        || project.getScreenShots() == null
        || project.getScreenShots().isEmpty()) return false;
    else return true;
  }
 // --------------------------------------------------------------------------------------------------------------
  /**
   * Download all screenshots for a single project and returns them in a byte[]. 
   * 
   * @param String Project Id
   * @return Byte[] - Byte[] containing all screenshots bytes combined together
   */
  @Transactional
  public Byte[] codeBaseScreenShots(String id) throws IOException, FileNotFoundException {
    Project project = findById(id);
    List<String> keys = project.getScreenShots();
    List<String> keyNames = s3KeySplitting(keys);
    List<Byte> bList = s3downloadScreenshots(keyNames);
    return bList.toArray(new Byte[0]);
  }

 /**
	 * This methods takes datamodels downloaded from the S3 bucket and put the in a single file 
	 * @param id
	 * @return name of file that data models are stored in
	 * @throws IOException
	 */
	@Transactional
	public byte[] codeBaseDataModels(String id){
		Project project = findById(id);
		File file = null;
		List<String> keys = project.getDataModel();
		List<String> keyNames = new ArrayList<>();
		for (String key : keys) {
			String array1[] = key.split("/");
			String newKey = array1[2].toString();
			keyNames.add(newKey);
		}
		try {
		File newFile = new File("datamodels.txt");
		if (newFile.createNewFile()) {
			System.out.println("New file created in the root directory");
		} else {
			System.out.println("File already exist");
		}
		byte[] esc = { '\n', 'E', 'N', 'D', 'O', 'F', 'F', 'I', 'L', 'E' };
		OutputStream outStream = null;
		outStream = new FileOutputStream(newFile);
		for (String key : keyNames) {
			this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
			this.downloadInputStream.write(esc);
			this.downloadInputStream.writeTo(outStream);
			file = newFile;
		}
		outStream.close();
		InputStream in =  new FileInputStream(file.getName());
	    byte[] media = IOUtils.toByteArray(in);
	    return media;
		}catch(IOException ioe) {
			ioe.getMessage();
		}
		return null;
	}
	/**
	 * This methods takes ziplinks downloaded from S3 buck and put then as a stream of bytes
	 * @param id
	 * @return a stream of byes representing the ziplinks
	 * @throws IOException
	 */
	@Transactional
	public ByteArrayOutputStream codeBaseZipLinks(String id) {
		Project project = findById(id);
		List<String> keys = project.getZipLinks();
		List<String> keyNames = new ArrayList<>();
		for (String key : keys) {
			String array1[] = key.split("/");
			String newKey = array1[2].toString();
			keyNames.add(newKey);
		}
		try {
		OutputStream outStream = null;
		outStream = new FileOutputStream(keyNames.toString());
		for (String key : keyNames) {
			this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
			this.downloadInputStream.writeTo(outStream);
		}
		outStream.close();
		return downloadInputStream;
		}catch(IOException ioe) {
			ioe.getMessage();
		}
		return null;
  }
  
  /**
   * Zips passed in file[] into a single zipped folder
   * 
   * @param files
   * @return Zipped Folder
   * @throws IOException
   */
  @Transactional
  public File zipFile(File[] files) throws IOException {

    System.out.println("Entered the Zipping");
    File zipper = new File("project-service/src/main/resources/tmp/compressed.zip");
    zipper.getParentFile().mkdirs();
    FileOutputStream fos = new FileOutputStream(zipper);
    ZipOutputStream zipOut = new ZipOutputStream(fos);

    for (File fileToZip : files) {
      if(fileToZip == null || !fileToZip.exists())
          {
            System.out.println("File was not found or created");
            continue;
          }
      System.out.println(fileToZip.getAbsolutePath());
      FileInputStream fis = new FileInputStream(fileToZip);
      ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
      zipOut.putNextEntry(zipEntry);
      byte[] bytes = new byte[1024];
      int length = fis.read(bytes);

      while (length >= 0) {
        zipOut.write(bytes, 0, length);
      }
      fis.close();
    }
    zipOut.close();
    fos.close();
    return zipper;
  }

  /**
   * Converting png Image from a ByteArray
   * @param fileBStream
   * @param filename
   * @return BufferedImage - Returns BufferedImage of png to be used. If no image was created or file does not exist, returns null
   */
  public File convertImageFromByteArray(
      ByteArrayOutputStream fileBStream, File file) {
      try {
      byte[] data = fileBStream.toByteArray();
      ByteArrayInputStream bis = new ByteArrayInputStream(data);
      BufferedImage bImage2 = ImageIO.read(bis);
      ImageIO.write(bImage2, "png", file);
      System.out.println("image created: "+ file.getName());
      return file;
    } catch (IOException e) {
      System.out.println("Converting Image failed");
      e.printStackTrace();
    }
    return null;
  }

  
  /**
   * Downloads screenshots from a s3 bucket using the StorageService. All screenshots downloaded should be a png file
   * so that we can not worry about compression.
   * @param keyNames
   * @return List<Byte> - list of all bytes of the screenshots together in sequential order.
   * @throws IOException
   */
  private List<Byte> s3downloadScreenshots(List<String> keyNames) throws IOException {
    List<Byte> bList = new ArrayList<>();
    FileInputStream fis = null;
    for (String key : keyNames) {
      this.downloadInputStream = s3StorageServiceImpl.downloadFile(key);
      File file = new File("project-service/src/main/resources/tmp/"+key);
      file.getParentFile().mkdirs();
      file.createNewFile();
      fis = new FileInputStream(convertImageFromByteArray(this.downloadInputStream, file));
      byte[] bArr = IOUtils.toByteArray(fis);
      List<Byte> byteList = Arrays.asList(ArrayUtils.toObject(bArr));
      bList.addAll(byteList); 
    }
    if(fis != null)
      fis.close();
    return bList;
  }

  /**
   * Screenshots urls are parsed to get their filename(keys) split from the url.
   * 
   * @param _screenshotUrls
   * @return List<String> - file names of all screenshots
   */
  private List<String> s3KeySplitting(List<String> _screenshotUrls) {
    List<String> _screenshotNames = new ArrayList<>();
    System.out.println("The keys before loop is: " + _screenshotUrls);
    for (String key : _screenshotUrls) {
      System.out.println("The key before split is: " + key);
      String[] keySplit = key.split("/");
      String newKey = keySplit[(keySplit.length - 1)];
      System.out.println("The key after split is: " + newKey);
      _screenshotNames.add(newKey);
    }
    System.out.println("The keys after loop is: " + _screenshotNames);
    return _screenshotNames;
  }
}
>>>>>>> 72305bd8f4c9627074dcc1c6856f1a2abf41ab2e
