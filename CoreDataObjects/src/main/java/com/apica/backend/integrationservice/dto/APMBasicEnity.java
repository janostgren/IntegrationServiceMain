package com.apica.backend.integrationservice.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "An object in the back-end system. Often an application")
public class APMBasicEnity implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private String description;
  
  public APMBasicEnity() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  public APMBasicEnity(String id, String name, String description) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
  }
  
  /**
   * @return the id
   */
  public String getId() {
    return id;
  }
  
  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
