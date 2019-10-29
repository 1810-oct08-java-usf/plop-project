package com.revature.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.revature.models.Project;

/**
 * ProjectRepository extends MongoRepository to provide the persistence layer for Project objects
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

  /**
   * ProjectRepository.findByName accepts a project name and returns a list of projects with that
   * name
   *
   * @param name the name of the project(s) you want to retrieve
   * @return a list of projects matching the given name
   */
  public List<Project> findByName(String name);

  /**
   * ProjectRepository.findByBatch accepts a project batch and returns a list of projects with that
   * batch
   *
   * @param batch the batch of the project(s) you want to retrieve
   * @return a list of projects matching the given batch
   */
  public List<Project> findByBatch(String batch);

  /**
   * ProjectRepository.findByTrainer accepts a project trainer and returns a list of projects with
   * that trainer
   *
   * @param trainer the trainer of the project(s) you want to retrieve
   * @return a list of projects matching the given trainer
   */
  public List<Project> findByTrainer(String trainer);

  public List<Project> findByUserId(Integer userId);

  /**
   * ProjectRepository.findByTechStack accepts a project techStack and returns a list of projects
   * with that techStack
   *
   * @param techStack the techStack of the project(s) you want to retrieve
   * @return a list of projects matching the given techStack
   */
  public List<Project> findByTechStack(String techStack);

  /**
   * ProjectRepository.findByStatus accepts a project status and returns a list of projects with
   * that status
   *
   * @param status the status of the project(s) you want to retrieve
   * @return a list of projects matching the given status
   */
  public List<Project> findByStatus(String status);
  
  //public List<Project> update(Project project);
}
