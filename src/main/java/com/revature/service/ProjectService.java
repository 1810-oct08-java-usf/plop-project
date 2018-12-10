package com.revature.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;

// TODO: Do we need @Transactional?

@Service
public class ProjectService {

	ProjectRepository projectRepo;

	public ProjectService(ProjectRepository projectRepo) {
		this.projectRepo = projectRepo;
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

	public boolean deleteById(String id) {
		if (id != null) {
			projectRepo.deleteById(id);
			return true;
		} else {
			return  false;
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
				
		}
		
		return true;
	}

	public Boolean addProject(Project project) {
		if (project != null) {
			projectRepo.save(project);
			return true;
		} else {
			return false;
		}

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
