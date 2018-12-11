package com.revature.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ProjectDTO {

	private String name;
	private String batch;

	// TODO: This value should be retrieved from the auth-service.
	// TODO: There is a possibility that there can be more than one trainer per
	// batch. Such as a co-trainer. This should be refactored for that option.
	private String trainer;

	private List<String> groupMembers;
	private List<MultipartFile> screenShots;
	private List<String> zipLinks;
	private String description;
	private String techStack;
	private String status;

	public ProjectDTO() {
		super();
	}

	// TODO implement builder pattern to reduce the complexity of this object's creation
	public ProjectDTO(String name, String batch, String trainer, List<String> groupMembers,
			List<MultipartFile> screenShots, List<String> zipLinks, String description, String techStack,
			String status) {
		super();
		this.name = name;
		this.batch = batch;
		this.trainer = trainer;
		this.groupMembers = groupMembers;
		this.screenShots = screenShots;
		this.zipLinks = zipLinks;
		this.description = description;
		this.techStack = techStack;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public List<String> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<String> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public List<MultipartFile> getScreenShots() {
		return screenShots;
	}

	public void setScreenShots(List<MultipartFile> screenShots) {
		this.screenShots = screenShots;
	}

	public List<String> getZipLinks() {
		return zipLinks;
	}

	public void setZipLinks(List<String> zipLinks) {
		this.zipLinks = zipLinks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTechStack() {
		return techStack;
	}

	public void setTechStack(String techStack) {
		this.techStack = techStack;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
