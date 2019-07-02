package com.revature.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

// TODO include transactional annotations to specify propagation and isolation levels
/**
 * ProjectService provides an interface to interact with a ProjectRepository
 */
@Service
public class ProjectService {

	ProjectRepository projectRepo;
	StorageService s3StorageServiceImpl;
	FileService fileService;

	@Autowired
	public ProjectService(ProjectRepository projectRepo, StorageService s3StorageServiceImpl, FileService fileService) {
		this.projectRepo = projectRepo;
		this.s3StorageServiceImpl = s3StorageServiceImpl;
		this.fileService = fileService;
	}

	/**
	 * ProjectService.findByName retrieves a list of projects with a given name
	 * 
	 * @param name the name of the project(s) you want to retrieve
	 * @return a list of projects with the given name
	 */
	public List<Project> findByName(String name) {
		return projectRepo.findByName(name);
	}

	/**
	 * ProjectService.findByBatch retrieves a list of projects with a given batch name
	 * 
	 * @param name the batch for the project(s) you want to retrieve
	 * @return a list of projects with the given batch
	 */
	public List<Project> findByBatch(String batch) {
		return projectRepo.findByBatch(batch);
	}

	/**
	 * ProjectService.findByTrainer retrieves a list of projects with a given trainer
	 * 
	 * @param name the trainer for the project(s) you want to retrieve
	 * @return a list of projects with the given trainer
	 */
	public List<Project> findByTrainer(String trainer) {
		return projectRepo.findByTrainer(trainer);
	}

	/**
	 * ProjectService.findByTechStack retrieves a list of projects with a given techStack
	 * 
	 * @param name the techStack for the project(s) you want to retrieve
	 * @return a list of projects with the given techStack
	 */
	public List<Project> findByTechStack(String techStack) {
		return projectRepo.findByTechStack(techStack);
	}

	/**
	 * ProjectService.findByStatus retrieves a list of projects with a given status
	 * 
	 * @param status the status for the project(s) you want to retrieve
	 * @return a list of projects with the given status
	 */
	public List<Project> findByStatus(String status) {
		return projectRepo.findByStatus(status);
	}

	/**
	 * ProjectService.findAllProjects retrieve a list of all projects
	 * 
	 * @return a list of all projects
	 */
	public List<Project> findAllProjects() {
		return projectRepo.findAll();
	}

	/**
	 * ProjectService.deleteById deletes a project with the given id
	 * 
	 * @param id an id for a project you want to delete
	 * @return a boolean indicating if a project with the given id was deleted
	 */
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
	 */
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
	 * ProjectService.createProjectFromDTO accepts a ProjectDTO and persists a Project
	 * The screenShots field in the DTO contains MultipartFiles that are converted to Files and
	 * stored. The Project screenShots field is populated with a list of links to those stored images.
	 * The zipLinks field in the DTO contains links to github repositories. zip archives are downloaded
	 * from github for each repository and stored in S3. The Project's screenShots field is populated with
	 * a list of links to those stored zip archives
	 * 
	 * @param projectDTO the data transfer object containing project details
	 * @return the Project generated from the DTO
	 */
	public Project createProjectFromDTO(ProjectDTO projectDTO) {
		Project newProject = new Project.ProjectBuilder()

		.setName(projectDTO.getName())
		.setBatch(projectDTO.getBatch())
		.setTrainer(projectDTO.getTrainer())
		.setGroupMembers(projectDTO.getGroupMembers())
		.setDescription(projectDTO.getDescription())
		.setTechStack(projectDTO.getTechStack())
		.setStatus(projectDTO.getStatus())
		.build();

		// drop screenshot images in s3 and populate project with links to those images
		List<String> screenShotsList = new ArrayList<>();

		for (MultipartFile multipartFile : projectDTO.getScreenShots()) {
			String endPoint = s3StorageServiceImpl.store(multipartFile);
			screenShotsList.add(endPoint);
		}
		newProject.setScreenShots(screenShotsList);
		
		// load sql files in s3 and populate project with links to those files
		List<String> dataModelList = new ArrayList<>();
		
		for (MultipartFile multipartFile : projectDTO.getDataModel()) {
			String endPoint = s3StorageServiceImpl.store(multipartFile);
			dataModelList.add(endPoint);
		}		

		// download a zip archive for each repo from github and store them in our s3
		// bucket,
		// populating the project object with links to those zip files
		for (String zipLink : projectDTO.getZipLinks()) {
			try {
				// TODO produce an http status code for error getting project zip and ABORT
				File zipArchive = fileService.download(zipLink + "/archive/master.zip");
				newProject.addZipLink(s3StorageServiceImpl.store(zipArchive));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		projectRepo.save(newProject);
		return newProject;
	}

	public Project findById(String id) {

		Optional<Project> currProject = projectRepo.findById(id);

		if (currProject.isPresent())
			return currProject.get();
		else
			return null;
	}

}
