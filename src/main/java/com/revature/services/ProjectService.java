package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProjectService {

	ProjectRepository projectRepo;
	StorageService s3StorageServiceImpl;
	FileService fileService;

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

	public List<Project> findByFullName(String fullName) {
		return projectRepo.findByFullName(fullName);
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

	public Boolean updateProject(Project project, String id) {
        Optional<Project> savedProject =  projectRepo.findById(id); 
        
        if (savedProject.isPresent()) {
            
            Project currentProject = savedProject.get();
          
            if (project.getBatch() != null) {
                currentProject.setBatch(project.getBatch());
            }
            if (project.getFullName() != null) {
                currentProject.setFullName(project.getFullName());
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
		
		// TODO remove
		newProject.setRepoURI(projectDTO.getRepoURI());

		newProject.setName(projectDTO.getName());
		newProject.setBatch(projectDTO.getBatch());
		newProject.setFullName(projectDTO.getUserFullName());
		newProject.setGroupMembers(projectDTO.getGroupMembers());
		newProject.setZipLinks(projectDTO.getZipLinks());
		newProject.setDescription(projectDTO.getDescription());
		newProject.setTechStack(projectDTO.getTechStack());
		newProject.setStatus(projectDTO.getStatus());
		
		// drop screenshot images in s3 and populate project with links to those images
		List<String> screenShotsList = new ArrayList<>();
				
		for (MultipartFile multipart: projectDTO.getScreenShots() ){
			String endPoint = s3StorageServiceImpl.store(multipart, "rpm-img");
			screenShotsList.add(endPoint);
			
			System.out.println(endPoint);
			System.out.println("Added s3 image url to screenshotsList");
		}
		
		newProject.setScreenShots(screenShotsList);

		// retrieve the specified project's zip archive from github, drop it in s3, and populate project with link to that zip (in s3)
		System.out.println("about to make a request for " + projectDTO.getRepoURI() + "/archive/master.zip");
		ResponseEntity<byte[]> dlResponse = fileService.download(projectDTO.getRepoURI() + "/archive/master.zip");

		if (dlResponse.getStatusCode().equals(HttpStatus.OK)) {
			System.out.println("filename from github content disposition: " + dlResponse.getHeaders().getContentDisposition());
			String s3Endpoint = s3StorageServiceImpl.store(dlResponse.getBody(), "rpm-zip", dlResponse.getHeaders().getContentDisposition().getFilename());
			newProject.setRepoURI(s3Endpoint);
		}

		projectRepo.save(newProject);
		return newProject;	
	}
	
	

	public Project findById(String id) {
		
		Optional<Project> currProject = projectRepo.findById(id);
		
		if(currProject.isPresent()) {
			return currProject.get();
		}else
		{
			return null;
		}
	}
}
