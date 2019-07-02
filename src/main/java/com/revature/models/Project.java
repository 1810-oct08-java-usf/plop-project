package com.revature.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

/**
 * the Project class (implementing Serializable) represents a successfully-submitted project
 */
// TODO this class should have JSR-303 (Bean Validator) annotations (@NotNull, @Pattern, etc.)
public class Project implements Serializable {

	private static final long serialVersionUID = -1295401907345421001L;

	@Id
	private String id;

	private String name;
	private String batch;

	// TODO: This value should be retrieved from the auth-service.
	// TODO: There is a possibility that there can be more than one trainer per
	// batch. Such as a co-trainer. This should be refactored for that option.
	private String trainer;

	private List<String> groupMembers;
	private List<String> screenShots;
	private List<String> dataModel;
	private List<String> zipLinks;
	private String description;
	private String techStack;
	private String status;
	
	private Project oldProject;

	/**
     * This constructor is private for the builder design pattern. 
     * See below the toString() for the static builder class ProjectBuilder.
     * 
     * @param builder
     * @Author: Tucker Mitchell 190422-USF-Java
     */
	private Project(ProjectBuilder builder) {
		super();
		this.name = builder.name;
		this.batch = builder.batch;
		this.trainer = builder.trainer;
		this.groupMembers = builder.groupMembers;
		this.screenShots = builder.screenShots;
		this.dataModel = builder.dataModel;
		this.zipLinks = builder.zipLinks;
		this.description = builder.description;
		this.techStack = builder.techStack;
		this.status = builder.status;
	}

	public void addZipLink(String zipLink) {
		if (zipLinks == null) {
			zipLinks = new ArrayList<>();
		}
		zipLinks.add(zipLink);
	}



	public String getId() {
		return id;
	}

	@Size(min = 3, max = 30, message = "'Project Name' must have a minimum of 3 characters with a max of 30.")
	@NotNull(message = "Please provide a project name.")
	@NotBlank(message = "Please provide a project name.")
	public String getName() {
		return name;
	}

	@NotNull(message = "Please enter a valid batch.")
	@NotBlank(message = "Please enter a valid batch.")
	public String getBatch() {
		return batch;
	}

	@NotNull(message = "Please provide the name of your trainer.")
	@NotBlank(message = "Please provide the name of your trainer.")
	public String getTrainer() {
		return trainer;
	}

	@NotNull(message = "Please list all memebers of the group project.")
	@NotBlank(message = "Please list all memebers of the group project.")
	public List<String> getGroupMembers() {
		return groupMembers;
	}

	@NotNull(message = "Please provide a screenshot of the project.")
	@NotBlank(message = "Please provide a screenshot of the project.")
	public List<String> getScreenShots() {
		return screenShots;
	}

	@NotNull(message = "Please provide a link to your project repoistory.")
	@NotBlank(message = "Please provide a link to your project repoistory.")
	public List<String> getZipLinks() {
		return zipLinks;
	}

	@Size(min = 20, max = 500, message = "Project Description must be a minimum of 20 characters with a max of 500.")
	@NotNull(message = "Please provide a valid project description.")
	@NotBlank(message = "Please provide a valid project description.")
	public String getDescription() {
		return description;
	}

	@NotNull(message = "Please provide at least one technology.")
	@NotBlank(message = "Please provide at least one technology.")
	public String getTechStack() {
		return techStack;
	}

	public String getStatus() {
		return status;
	}
	
	public Project getOldProject() {
		return oldProject;
	}
	
	@NotNull(message = "Please provide a data model.")
	@NotBlank(message = "Please provide a data model.")
	public List<String> getDataModel() {
		return dataModel;
	}

	
	
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public void setGroupMembers(List<String> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public void setScreenShots(List<String> screenShots) {
		this.screenShots = screenShots;
	}

	public void setDataModel(List<String> dataModel) {
		this.dataModel = dataModel;
	}

	public void setZipLinks(List<String> zipLinks) {
		this.zipLinks = zipLinks;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTechStack(String techStack) {
		this.techStack = techStack;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOldProject(Project oldProject) {
		this.oldProject = oldProject;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result + ((dataModel == null) ? 0 : dataModel.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((groupMembers == null) ? 0 : groupMembers.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((oldProject == null) ? 0 : oldProject.hashCode());
		result = prime * result + ((screenShots == null) ? 0 : screenShots.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((techStack == null) ? 0 : techStack.hashCode());
		result = prime * result + ((trainer == null) ? 0 : trainer.hashCode());
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
		if (dataModel == null) {
			if (other.dataModel != null)
				return false;
		} else if (!dataModel.equals(other.dataModel))
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
		if (oldProject == null) {
			if (other.oldProject != null)
				return false;
		} else if (!oldProject.equals(other.oldProject))
			return false;
		if (screenShots == null) {
			if (other.screenShots != null)
				return false;
		} else if (!screenShots.equals(other.screenShots))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (techStack == null) {
			if (other.techStack != null)
				return false;
		} else if (!techStack.equals(other.techStack))
			return false;
		if (trainer == null) {
			if (other.trainer != null)
				return false;
		} else if (!trainer.equals(other.trainer))
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
		return "Project [id=" + id + ", name=" + name + ", batch=" + batch + ", trainer=" + trainer + ", groupMembers="
				+ groupMembers + ", screenShots=" + screenShots + ", dataModel=" + dataModel + ", zipLinks=" + zipLinks
				+ ", description=" + description + ", techStack=" + techStack + ", status=" + status + "]";
	}

	/**
     * The Project class uses the builder design pattern instead of using an all-args constructor for making more readable code.
     * Set methods can be used or ignored for setting fields of the Project; ignored fields will be default primitive values.
     * (The fields of the builder must be the same as the fields of the object being built)
     * The ProjectBuilder is a class within the Project class that can be used initialize the private fields of Project.
     * The no-args builder constructor is needed to instantiate the builder, after which the set methods can be used.
     * (Arguments put into the builder constructor will be mandatory for initialization; this was left as no-args for flexibility)
     * All of the setters need to return the builder object for chaining methods (instead of void).
     * build() is needed last to call the private Project constructor and instantiate with the builder just created.
     * 
     * Based on documentation found here: https://www.journaldev.com/1425/builder-design-pattern-in-java
     * 
     * @param builder
     * @Author: Daniel Shaffer 190422-USF-Java
     */
	public static class ProjectBuilder {
		
		private String name;
		private String batch;
		private String trainer;
		private List<String> groupMembers;
		private List<String> screenShots;
		private List<String> dataModel;
		private List<String> zipLinks;
		private String description;
		private String techStack;
		private String status;
		
		public ProjectBuilder setName(String name) {
			this.name = name;
			return this;
		}
		public ProjectBuilder setBatch(String batch) {
			this.batch = batch;
			return this;
		}
		public ProjectBuilder setTrainer(String trainer) {
			this.trainer = trainer;
			return this;
		}
		public ProjectBuilder setGroupMembers(List<String> groupMembers) {
			this.groupMembers = groupMembers;
			return this;
		}
		public ProjectBuilder setScreenShots(List<String> screenShots) {
			this.screenShots = screenShots;
			return this;
		}
		public ProjectBuilder setDataModel(List<String> dataModel) {
			this.dataModel = dataModel;
			return this;
		}
		public ProjectBuilder setZipLinks(List<String> zipLinks) {
			this.zipLinks = zipLinks;
			return this;
		}
		public ProjectBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		public ProjectBuilder setTechStack(String techStack) {
			this.techStack = techStack;
			return this;
		}
		public ProjectBuilder setStatus(String status) {
			this.status = status;
			return this;
		}
		
		public Project build() {
			return new Project(this);
		}
	}
}
