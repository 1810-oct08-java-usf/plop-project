package com.revature.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Project;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

	public List<Project> findByName(String name);

	public List<Project> findByBatch(String batch);

	public List<Project> findByTrainer(String trainer);

	public List<Project> findByTechStack(String techStack);

	public List<Project> findByStatus(String status);

}
