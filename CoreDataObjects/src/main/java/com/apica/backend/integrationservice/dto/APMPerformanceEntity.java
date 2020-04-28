package com.apica.backend.integrationservice.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;

@ApiModel(
    description = "The main performnce object in a back-end system. Examples Service or Business Application ")
public class APMPerformanceEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private String entityType;
  private String componentId = "";
  private String componentName = "";
  
  public APMPerformanceEntity() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  public APMPerformanceEntity(String id, String name, String entityType, String componentId,
      String componentName) {
    this();
    this.id = id;
    this.name = name;
    this.entityType = entityType;
    this.componentId = componentId;
    this.componentName = componentName;
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
   * @return the entityType
   */
  public String getEntityType() {
    return entityType;
  }
  
  /**
   * @param entityType the entityType to set
   */
  public void setEntityType(String entityType) {
    this.entityType = entityType;
  }
  
  /**
   * @return the componentId
   */
  public String getComponentId() {
    return componentId;
  }
  
  /**
   * @param componentId the componentId to set
   */
  public void setComponentId(String componentId) {
    this.componentId = componentId;
  }
  
  /**
   * @return the componentName
   */
  public String getComponentName() {
    return componentName;
  }
  
  /**
   * @param componentName the componentName to set
   */
  public void setComponentName(String componentName) {
    this.componentName = componentName;
  }
}
