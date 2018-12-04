package com.revature.repositories;
 import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.revature.models.Project;
 public interface ProjectRepository extends MongoRepository<Project, String>{
 	public List<Project> findByName(String name);
	public List<Project> findByBatch(String batch);
	public List<Project> findByFullName(String name);
	public List<Project> findByTechStack(String techStack);
	public List<Project> findByStatus(String status);
}