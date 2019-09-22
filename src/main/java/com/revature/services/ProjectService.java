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

import com.revature.exceptions.FileSizeTooLargeException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

// TODO include transactional annotations to specify propagation and isolation levels
/**
 * ProjectService provides an interface to interact with a ProjectRepository
 */
@Service
public class ProjectService {

	/**
	 * This is the project initial status.
	 * @author Testing Team (2019)
	 */
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

	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
    public List<Project> findByUserId(Integer userId) {
        return projectRepo.findByUserId(userId);
    }

	/**
	 * ProjectService.findByName retrieves a list of projects with a given name
	 * the transaction is read-only and only reads committed data
	 * 
	 * @param name the name of the project(s) you want to retrieve
	 * @return a list of projects with the given name
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */

	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Project> findByName(String name) {
		return projectRepo.findByName(name);
	}

	/**
	 * ProjectService.findByBatch retrieves a list of projects with a given batch name
	 * the transaction is read-only and only reads committed data
	 * 
	 * @param name the batch for the project(s) you want to retrieve
	 * @return a list of projects with the given batch
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Project> findByBatch(String batch) {
		return projectRepo.findByBatch(batch);
	}

	/**
	 * ProjectService.findByTrainer retrieves a list of projects with a given trainer
	 *the transaction is read-only and only reads committed data 
	 * @param name the trainer for the project(s) you want to retrieve
	 * @return a list of projects with the given trainer
	 * @author Stuart Pratuh (190422-JAVA-SPARK-USF)
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Project> findByTrainer(String trainer) {
		return projectRepo.findByTrainer(trainer);
	}

	/**
	 * ProjectService.findByTechStack retrieves a list of projects with a given techStack
	 * the transaction is read-only and only reads committed data
	 * @param name the techStack for the project(s) you want to retrieve
	 * @return a list of projects with the given techStack
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Project> findByTechStack(String techStack) {
		return projectRepo.findByTechStack(techStack);
	}

	/**
	 * ProjectService.findByStatus retrieves a list of projects with a given status
	 * The transaction is read-only and only reads committed data
	 * @param status the status for the project(s) you want to retrieve
	 * @return a list of projects with the given status
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Project> findByStatus(String status) {
		return projectRepo.findByStatus(status);
	}

	/**
	 * ProjectService.findAllProjects retrieve a list of all projects
	 * The transaction is read-only and only reads committed data
	 * 
	 * @return a list of all projects
	 * 
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public List<Project> findAllProjects() {
		return projectRepo.findAll();
	}

	/**
	 * ProjectService.deleteById deletes a project with the given id
	 * Propagation.Requires_New makes the transaction a new transaction
	 * to ensure a new delete transaction occurs each time.
	 * 
	 * @param id an id for a project you want to delete
	 * @return a boolean indicating if a project with the given id was deleted
	 * 
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Boolean deleteById(String id) {
		if (id != null) {
			projectRepo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Updates the project by taking in JSON values and mapping them to a Project
	 * model.
	 *  Propagation.Requires_New makes a new and different transaction occur each time.
	 * 
	 * @param project: User entered JSON data for a project.
	 * 
	 * @param id: String data of an id.
	 * 
	 * Retrieves project from database through id. Checks whether a user has entered
	 * information for a specific value in the model. If they have then that is what
	 * is updated.
	 * 
	 * @author Miles LaCue (1810-Oct08-Java-USF)
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * 
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Boolean updateProject(Project project, String id) {
		Optional<Project> savedProject = projectRepo.findById(id);

		if (savedProject.isPresent()) {

			Project currentProject = savedProject.get();

			if (project.getName() != null) {
				currentProject.setName(project.getName());
			}
			if (project.getBatch() != null) {
				currentProject.setBatch(project.getBatch());
			}
			if (project.getTrainer() != null) {
				currentProject.setTrainer(project.getTrainer());
			}
			if (project.getGroupMembers() != null) {
				currentProject.setGroupMembers(project.getGroupMembers());
			}
			if (project.getScreenShots() != null) {
				currentProject.setScreenShots(project.getScreenShots());
			}
			if (project.getZipLinks() != null) {
				currentProject.setZipLinks(project.getZipLinks());
			}
			if (project.getDescription() != null) {
				currentProject.setDescription(project.getDescription());
			}
			if (project.getTechStack() != null) {
				currentProject.setTechStack(project.getTechStack());
			}
			if (project.getStatus() != null) {
				currentProject.setStatus(project.getStatus());
			}
			if (project.getOldProject() != null) {
				currentProject.setOldProject(project.getOldProject());
			}

			projectRepo.save(currentProject);
			return true;
		}

		return false;
	}

	/**
	 * ProjectService.createProjectFromDTO accepts a ProjectDTO and persists a Project.
	 * Transaction requires a new one every time to handle each new created object.
	 * 
	 * The screenShots field in the DTO contains MultipartFiles that are converted to Files and
	 * stored. The Project screenShots field is populated with a list of links to those stored images.
	 * The zipLinks field in the DTO contains links to github repositories. Zip archives are downloaded
	 * from github for each repository and stored in S3. The Project's screenShots field is populated with
	 * a list of links to those stored zip archives
	 * (Needs a unit test for zipLinks to test fileSizeTooLargeException)
	 * 
	 * @param projectDTO the data transfer object containing project details
	 * @return the Project generated from the DTO
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 * @author Tucker Mitchell (190422-Java-USF)
	 * @author Kevin Ocampo (190422-Java-USF)
	 * 
	 * UPDATE (Testing Team): Implemented some validations for some properties that were considered require in the
	 * front-end, but are not enforced in the back-end.
	 * Setting initial status for the project, in this case it will be set to "Pending".
	 * 
	 * @author Testing Team (ago 2019) - USF
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Project createProjectFromDTO(ProjectDTO projectDTO) throws FileSizeTooLargeException {
		
		Project newProject = new Project.ProjectBuilder()
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
		 * Setting initial status of the project this way we can assure the
		 *  project will have this status even if the status is change in the
		 *  front end.
		 *  Testing Team (ago 2019) - USF
		 */
		newProject.setStatus(INITIAL_PROJECT_STATUS);
		
		/*
		 * Implementing new validations
		 * Testing Team (ago 2019) - USF
		 */
		if (newProject.getDescription() == null || newProject.getDescription().isEmpty()) {
			throw new ProjectNotAddedException("The 'description' input cannot be empty when adding project");
		}
		
		if (newProject.getGroupMembers() == null || newProject.getGroupMembers().size() == 0) {
			throw new ProjectNotAddedException("The 'group members' input cannot be empty when adding project");
		}
		
		// drop screenshot images in s3 and populate project with links to those images
		List<String> screenShotsList = new ArrayList<>();
		
		if(projectDTO.getScreenShots() == null)
			newProject.setScreenShots(screenShotsList);
		else {
			for (MultipartFile multipartFile : projectDTO.getScreenShots()) {
				if (multipartFile.getSize() > 1_000_000) {
					throw new FileSizeTooLargeException("File size of screenshot: " + multipartFile.getName() + "is greater than 1MB.");
				}
				String endPoint = s3StorageServiceImpl.store(multipartFile);
				screenShotsList.add(endPoint);
			}
			newProject.setScreenShots(screenShotsList);
		}
		
		// load sql files in s3 and populate project with links to those files
		List<String> dataModelList = new ArrayList<>();
		
		if(projectDTO.getDataModel() == null)
			newProject.setDataModel(dataModelList);
		else {
			for (MultipartFile multipartFile : projectDTO.getDataModel()) {
				if (multipartFile.getSize() > 1_000_000) {
					throw new FileSizeTooLargeException("File size of data model: " + multipartFile.getName() + "is greater than 1MB.");
				}
				String endPoint = s3StorageServiceImpl.store(multipartFile);
				dataModelList.add(endPoint);
			}		
			newProject.setDataModel(dataModelList);
		}
		
		// download a zip archive for each repo from github and store them in our s3
		// bucket,
		// populating the project object with links to those zip files
		if(projectDTO.getZipLinks() == null)
			newProject.setZipLinks(new ArrayList<String>());
		else {
			for (String zipLink : projectDTO.getZipLinks()) {
				try {
					// TODO produce an http status code for error getting project zip and ABORT
					File zipArchive = fileService.download(zipLink + "/archive/master.zip");
					if (zipArchive.length() > 1_000_000_000) {
						throw new FileSizeTooLargeException("The file size of: " + zipArchive.getName() + "exceeds 1GB");
					}
					newProject.addZipLink(s3StorageServiceImpl.store(zipArchive));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		projectRepo.save(newProject);
		return newProject;
	}
	
	/**
	 * the transaction is read-only and only reads committed data
	 * 
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */
	@Transactional(readOnly=true, isolation=Isolation.READ_COMMITTED)
	public Project findById(String id) {

		Optional<Project> currProject = projectRepo.findById(id);

		if (currProject.isPresent())
			return currProject.get();
		else
			return null;
	}
	
    /**
     * 
     * @param project
     * @return
     */
    public boolean submitEditRequest(Project project) {
        if(project != null) {
            projectRepo.save(project);
            return true;
        }
        
        return false;
    }

}
