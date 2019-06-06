package com.revature.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	public Project() {
		super();
	}

	// TODO implement builder pattern to reduce the complexity of this object's construction
	public Project(String name, String batch, String trainer, List<String> groupMembers, List<String> screenShots,
			List<String> dataModel, List<String> zipLinks, String description, String techStack, String status) {
		super();
		this.name = name;
		this.batch = batch;
		this.trainer = trainer;
		this.groupMembers = groupMembers;
		this.screenShots = screenShots;
		this.dataModel = dataModel;
		this.zipLinks = zipLinks;
		this.description = description;
		this.techStack = techStack;
		this.status = status;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Project getOldProject() {
		return oldProject;
	}

	public void setOldProject(Project oldProject) {
		this.oldProject = oldProject;
	}
	
	public List<String> getDataModel() {
		return dataModel;
	}

	public void setDataModel(List<String> dataModel) {
		this.dataModel = dataModel;
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


	
	

}
