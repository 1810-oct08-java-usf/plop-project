package com.revature.rpm.dtos;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProjectDTO represents the project information a user would initially submit. it differs from
 * Project in that the user will submit github repo URLs in ProjectDTO.zipLinks where
 * Project.zipLinks will contain links to their archives, and in that the user will submit
 * MultipartFile objects in ProjectDTO.screenShots where Project.screenShots will contain remote
 * URLs for those images where they have been stored @Author Unknown, from a previous batch
 */
@Component
public class ProjectDTO {

  private String name;
  private String batch;
  private String trainer;
  private List<String> groupMembers;
  private List<MultipartFile> screenShots;
  private List<String> zipLinks;
  private String description;
  private String techStack;
  private String status;
  // field for dataModel
  private List<MultipartFile> dataModel;
  private Integer userId;

  public ProjectDTO() {
    super();
  }

  /**
   * This constructor is private for the builder design pattern. This may not be implemented for
   * creating a new DTO because it is passed in from the front-end, but was made for consistency
   * alongside the Project.java builder. See below the toString() for the static builder class
   * ProjectDTOBuilder.
   *
   * @param builder @Author: Daniel Shaffer 190422-USF-Java
   */
  private ProjectDTO(ProjectDTOBuilder builder) {
    super();
    this.name = builder.name;
    this.batch = builder.batch;
    this.trainer = builder.trainer;
    this.groupMembers = builder.groupMembers;
    this.screenShots = builder.screenShots;
    this.zipLinks = builder.zipLinks;
    this.description = builder.description;
    this.techStack = builder.techStack;
    this.status = builder.status;
    this.dataModel = builder.dataModel;
    this.userId = builder.userId;
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

  public List<MultipartFile> getDataModel() {
    return dataModel;
  }

  public void setDataModel(List<MultipartFile> dataModel) {
    this.dataModel = dataModel;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((batch == null) ? 0 : batch.hashCode());
    result = prime * result + ((dataModel == null) ? 0 : dataModel.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((groupMembers == null) ? 0 : groupMembers.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((screenShots == null) ? 0 : screenShots.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + ((techStack == null) ? 0 : techStack.hashCode());
    result = prime * result + ((trainer == null) ? 0 : trainer.hashCode());
    result = prime * result + ((zipLinks == null) ? 0 : zipLinks.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ProjectDTO other = (ProjectDTO) obj;
    if (batch == null) {
      if (other.batch != null) return false;
    } else if (!batch.equals(other.batch)) return false;
    if (dataModel == null) {
      if (other.dataModel != null) return false;
    } else if (!dataModel.equals(other.dataModel)) return false;
    if (description == null) {
      if (other.description != null) return false;
    } else if (!description.equals(other.description)) return false;
    if (groupMembers == null) {
      if (other.groupMembers != null) return false;
    } else if (!groupMembers.equals(other.groupMembers)) return false;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    if (screenShots == null) {
      if (other.screenShots != null) return false;
    } else if (!screenShots.equals(other.screenShots)) return false;
    if (status == null) {
      if (other.status != null) return false;
    } else if (!status.equals(other.status)) return false;
    if (techStack == null) {
      if (other.techStack != null) return false;
    } else if (!techStack.equals(other.techStack)) return false;
    if (trainer == null) {
      if (other.trainer != null) return false;
    } else if (!trainer.equals(other.trainer)) return false;
    if (zipLinks == null) {
      if (other.zipLinks != null) return false;
    } else if (!zipLinks.equals(other.zipLinks)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "ProjectDTO [name="
        + name
        + ", batch="
        + batch
        + ", trainer="
        + trainer
        + ", groupMembers="
        + groupMembers
        + ", screenShots="
        + screenShots
        + ", zipLinks="
        + zipLinks
        + ", description="
        + description
        + ", techStack="
        + techStack
        + ", status="
        + status
        + ", dataModel="
        + dataModel
        + "]";
  }

  /**
   * The ProjectDTO uses the builder design pattern instead of using an all-args constructor for
   * making more readable code. Set methods can be used or ignored for setting fields of the
   * ProjectDTO; ignored fields will be default primitive values. (The fields of the builder must be
   * the same as the fields of the object being built) The ProjectDTOBuilder is a class within the
   * ProjectDTO class that can be used initialize the private fields of ProjectDTO. The no-args
   * builder constructor is needed to instantiate the builder, after which the set methods can be
   * used. (Arguments put into the builder constructor will be mandatory for initialization; this
   * was left as no-args for flexibility) All of the setters need to return the builder object for
   * chaining methods (instead of void). build() is needed last to call the private ProjectDTO
   * constructor and instantiate with the builder just created.
   *
   * <p>Based on documentation found here:
   * https://www.journaldev.com/1425/builder-design-pattern-in-java
   *
   * @param builder @Author: Daniel Shaffer 190422-USF-Java
   */
  public static class ProjectDTOBuilder {
    private String name;
    private String batch;
    private String trainer;
    private List<String> groupMembers;
    private List<MultipartFile> screenShots;
    private List<String> zipLinks;
    private String description;
    private String techStack;
    private String status;
    private List<MultipartFile> dataModel;

    private Integer userId;

    public ProjectDTOBuilder() {
      super();
    }

    public ProjectDTOBuilder setName(String name) {
      this.name = name;
      return this;
    }

    public ProjectDTOBuilder setBatch(String batch) {
      this.batch = batch;
      return this;
    }

    public ProjectDTOBuilder setTrainer(String trainer) {
      this.trainer = trainer;
      return this;
    }

    public ProjectDTOBuilder setGroupMembers(List<String> groupMembers) {
      this.groupMembers = groupMembers;
      return this;
    }

    public ProjectDTOBuilder setScreenShots(List<MultipartFile> screenShots) {
      this.screenShots = screenShots;
      return this;
    }

    public ProjectDTOBuilder setZipLinks(List<String> zipLinks) {
      this.zipLinks = zipLinks;
      return this;
    }

    public ProjectDTOBuilder setDescription(String description) {
      this.description = description;
      return this;
    }

    public ProjectDTOBuilder setTechStack(String techStack) {
      this.techStack = techStack;
      return this;
    }

    public ProjectDTOBuilder setStatus(String status) {
      this.status = status;
      return this;
    }

    public ProjectDTOBuilder setDataModel(List<MultipartFile> dataModel) {
      this.dataModel = dataModel;
      return this;
    }

    public ProjectDTOBuilder setUserId(Integer userId) {
      this.userId = userId;
      return this;
    }

    public ProjectDTO build() {
      return new ProjectDTO(this);
    }
  }
}
