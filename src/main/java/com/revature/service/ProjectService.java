package com.revature.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
