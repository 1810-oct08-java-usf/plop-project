package com.revature.models;

import java.util.List;

public class Project {

	private String id;
	  
	private String name;
	private String batch;
	private List<AppUser> user;
	private List<String> groupMembers;
	private List<String> screenShots;
	private List<String> zipLinks;
	private String description;
	private String techStack;
	private int status;
	
	public Project() {
		super();
	}

	public Project(String name, String batch, List<AppUser> user, List<String> groupMembers,
			List<String> screenShots, List<String> zipLinks, String description, String techStack, int status) {
		super();
		this.name = name;
		this.batch = batch;
		this.user = user;
		this.groupMembers = groupMembers;
		this.screenShots = screenShots;
		this.zipLinks = zipLinks;
		this.description = description;
		this.techStack = techStack;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<AppUser> getTrainer() {
		return user;
	}

	public void setTrainer(List<AppUser> user) {
		this.user = user;
	}

	public List<String> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<String> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public List<String> getScreenShots() {
		return screenShots;
	}

	public void setScreenShots(List<String> screenShots) {
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((groupMembers == null) ? 0 : groupMembers.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((screenShots == null) ? 0 : screenShots.hashCode());
		result = prime * result + status;
		result = prime * result + ((techStack == null) ? 0 : techStack.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((zipLinks == null) ? 0 : zipLinks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (batch == null) {
			if (other.batch != null)
				return false;
		} else if (!batch.equals(other.batch))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (groupMembers == null) {
			if (other.groupMembers != null)
				return false;
		} else if (!groupMembers.equals(other.groupMembers))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (screenShots == null) {
			if (other.screenShots != null)
				return false;
		} else if (!screenShots.equals(other.screenShots))
			return false;
		if (status != other.status)
			return false;
		if (techStack == null) {
			if (other.techStack != null)
				return false;
		} else if (!techStack.equals(other.techStack))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (zipLinks == null) {
			if (other.zipLinks != null)
				return false;
		} else if (!zipLinks.equals(other.zipLinks))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", batch=" + batch + ", user=" + user + ", groupMembers="
				+ groupMembers + ", screenShots=" + screenShots + ", zipLinks=" + zipLinks + ", description="
				+ description + ", techStack=" + techStack + ", status=" + status + "]";
	}

	
	
}