package com.revature.repositorys;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.revature.models.Project;

public interface ProjectRepository extends MongoRepository<Project, String>{

	public Project findByName(String name);
	public List<Project> findByBatch(String batch);
}
