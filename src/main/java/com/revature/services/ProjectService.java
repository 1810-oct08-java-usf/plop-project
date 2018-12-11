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
	
	public Project createProjectFromDTO(ProjectDTO projectDTO) {
		Project newProject = new Project();

		newProject.setName(projectDTO.getName());
		newProject.setBatch(projectDTO.getBatch());
		newProject.setFullName(projectDTO.getUserFullName());
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
		
		if(currProject.isPresent()) {
			return currProject.get();
		} else {
			return null;
		}
	}
}
