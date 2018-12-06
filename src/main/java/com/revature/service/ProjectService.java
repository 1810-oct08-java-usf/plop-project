package com.revature.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.repositories.ProjectRepository;

@Service
public class ProjectService {

	ProjectRepository projectRepo;
	
	StorageService s3StorageServiceImpl;
	
	public ProjectService(ProjectRepository projectRepo, StorageService s3StorageServiceImpl) {
		this.projectRepo = projectRepo;
		this.s3StorageServiceImpl = s3StorageServiceImpl;
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

	public Boolean updateProject(Project project) {
		if (project != null) {
			projectRepo.save(project);
			return true;
		} else {
			return false;
		}
	}
	
//	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//		storageService.store(file);
//		return "ok";
//	}
	
	
	
	public Project createProjectFromDTO(ProjectDTO projectDTO) {
		Project newProject = new Project();
		
		newProject.setName(projectDTO.getName());
		newProject.setBatch(projectDTO.getBatch());
		newProject.setFullName(projectDTO.getUserFullName());
		newProject.setGroupMembers(projectDTO.getGroupMembers());
		newProject.setZipLinks(projectDTO.getZipLinks());
		newProject.setDescription(projectDTO.getDescription());
		newProject.setTechStack(projectDTO.getTechStack());
		newProject.setStatus(projectDTO.getStatus());
		

		List<String> screenShotsList = new ArrayList<>();
				
		for (MultipartFile multipart: projectDTO.getScreenShots() ){
			String endPoint = s3StorageServiceImpl.store(multipart);
			screenShotsList.add(endPoint);
			
			System.out.println(endPoint);
			System.out.println("Added s3 image url to screenshotsList");
		}
		
		newProject.setScreenShots(screenShotsList);
		
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
