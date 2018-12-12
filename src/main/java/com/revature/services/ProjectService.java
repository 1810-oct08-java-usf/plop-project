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

	public List<Project> findByName(String name) {
		return projectRepo.findByName(name);
	}

	public List<Project> findByBatch(String batch) {
		return projectRepo.findByBatch(batch);
	}

	public List<Project> findByTrainer(String trainer) {
		return projectRepo.findByTrainer(trainer);
	}

	public List<Project> findByTechStack(String techStack) {
		return projectRepo.findByTechStack(techStack);
	}

	public List<Project> findByStatus(String status) {
		return projectRepo.findByStatus(status);
	}

	public List<Project> findAllProjects() {
		return projectRepo.findAll();
	}

	public Boolean deleteById(String id) {
		if (id != null) {
			projectRepo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Updates the project by taking in JSON values and mapping them to a Project model.
	 * 
	 * @param project: User entered JSON data for a project.
	 * @param id: String data of an id.
	 * 
	 * Retrieves project from database through id.
	 * Checks whether a user has entered information for a specific value in the model. If they have then that is what is updated.
	 * 
	 * @author Miles LaCue (1810-Oct08-Java-USF)
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	*/
	public Boolean updateProject(Project project, String id) {
        Optional<Project> savedProject =  projectRepo.findById(id); 
        
        if (savedProject.isPresent()) {
            
            Project currentProject = savedProject.get();
            
            if(project.getName() != null) {
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
            
            projectRepo.save(currentProject);
            return true;   
        }
        
        return false;
    }
	
	

	public Project createProjectFromDTO(ProjectDTO projectDTO) {
		Project newProject = new Project();

		newProject.setName(projectDTO.getName());
		newProject.setBatch(projectDTO.getBatch());
		newProject.setTrainer(projectDTO.getTrainer());
		newProject.setGroupMembers(projectDTO.getGroupMembers());
		newProject.setDescription(projectDTO.getDescription());
		newProject.setTechStack(projectDTO.getTechStack());
		newProject.setStatus(projectDTO.getStatus());
		
		// drop screenshot images in s3 and populate project with links to those images
		List<String> screenShotsList = new ArrayList<>();
				
		for (MultipartFile multipartFile: projectDTO.getScreenShots() ){
			String endPoint = s3StorageServiceImpl.store(multipartFile);
			screenShotsList.add(endPoint);
		}
		
		newProject.setScreenShots(screenShotsList);

		// download a zip archive for each repo from github and store them in our s3 bucket,
		// populating the project object with links to those zip files
		for (String zipLink: projectDTO.getZipLinks()) {
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
		
		if(currProject.isPresent()) return currProject.get();
		else return null;
	}

}
