package com.apica.backend.integrationservice.dto;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A trace from back-end APM System.")
public class APMTrace implements Serializable {
  private static final long serialVersionUID = -4714174399407105846L;
  private String traceId;
  @ApiModelProperty(notes = "The logical name/label for the trace")
  private String name;
  private APMPerformanceEntity entity;
  private String traceType;
  @ApiModelProperty(notes = "0=Normal, 1=Slow, 2=Very Slow, 3=Error, 4=Stalled")
  private int statusCode;
  private String statusName;
  private boolean error;
  private boolean fullTrace;
  @ApiModelProperty(notes = "Duration of trace in ms")
  private long duration;
  @ApiModelProperty(notes = "Start time for trace in epoch format")
  private long startTime;
  private String resource;
  private String resourceType;
  @ApiModelProperty(notes = "The UI drill-down link for the trace")
  private String drillDownLink;
  
  public APMTrace() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  public APMTrace(String traceId, String name, APMPerformanceEntity entity, String traceType,
      int statusCode, String statusName, boolean error, boolean fullTrace, long duration,
      long startTime, String resource, String resourceType, String drillDownLink) {
    super();
    this.traceId = traceId;
    this.name = name;
    this.entity = entity;
    this.traceType = traceType;
    this.statusCode = statusCode;
    this.statusName = statusName;
    this.error = error;
    this.fullTrace = fullTrace;
    this.duration = duration;
    this.startTime = startTime;
    this.resource = resource;
    this.resourceType = resourceType;
    this.setDrillDownLink(drillDownLink);
  }
  
  /**
   * @return the traceId
   */
  public String getTraceId() {
    return traceId;
  }
  
  /**
   * @param traceId the traceId to set
   */
  public void setTraceId(String traceId) {
    this.traceId = traceId;
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
   * @return the entity
   */
  public APMPerformanceEntity getEntity() {
    return entity;
  }
  
  /**
   * @param entity the entity to set
   */
  public void setEntity(APMPerformanceEntity entity) {
    this.entity = entity;
  }
  
  /**
   * @return the traceType
   */
  public String getTraceType() {
    return traceType;
  }
  
  /**
   * @param traceType the traceType to set
   */
  public void setTraceType(String traceType) {
    this.traceType = traceType;
  }
  
  /**
   * @return the statusCode
   */
  public int getStatusCode() {
    return statusCode;
  }
  
  /**
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  
  /**
   * @return the statusName
   */
  public String getStatusName() {
    return statusName;
  }
  
  /**
   * @param statusName the statusName to set
   */
  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }
  
  /**
   * @return the error
   */
  public boolean isError() {
    return error;
  }
  
  /**
   * @param error the error to set
   */
  public void setError(boolean error) {
    this.error = error;
  }
  
  /**
   * @return the fullTrace
   */
  public boolean isFullTrace() {
    return fullTrace;
  }
  
  /**
   * @param fullTrace the fullTrace to set
   */
  public void setFullTrace(boolean fullTrace) {
    this.fullTrace = fullTrace;
  }
  
  /**
   * @return the duration
   */
  public long getDuration() {
    return duration;
  }
  
  /**
   * @param duration the duration to set
   */
  public void setDuration(long duration) {
    this.duration = duration;
  }
  
  /**
   * @return the startTime
   */
  public long getStartTime() {
    return startTime;
  }
  
  /**
   * @param startTime the startTime to set
   */
  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
  
  /**
   * @return the resource
   */
  public String getResource() {
    return resource;
  }
  
  /**
   * @param resource the resource to set
   */
  public void setResource(String resource) {
    this.resource = resource;
  }
  
  /**
   * @return the resourceType
   */
  public String getResourceType() {
    return resourceType;
  }
  
  /**
   * @param resourceType the resourceType to set
   */
  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }
  
  public String getDrillDownLink() {
    return drillDownLink;
  }
  
  public void setDrillDownLink(String drillDownLink) {
    this.drillDownLink = drillDownLink;
  }
}
