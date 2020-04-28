package com.apica.backend.integrationservice.model;

import java.io.Serializable;

public class TraceMetaData implements Serializable {
  private String requestId;
  private String callTreeName;
  private String entityName;
  private final static long serialVersionUID = -4753334925495618930L;
  
  /**
   * No args constructor for use in serialization
   *
   */
  public TraceMetaData() {}
  
  /**
   *
   * @param requestId
   * @param entityName
   * @param callTreeName
   */
  public TraceMetaData(String requestId, String callTreeName, String entityName) {
    super();
    this.requestId = requestId;
    this.callTreeName = callTreeName;
    this.entityName = entityName;
  }
  
  public String getRequestId() {
    return requestId;
  }
  
  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }
  
  public TraceMetaData withRequestId(String requestId) {
    this.requestId = requestId;
    return this;
  }
  
  public String getCallTreeName() {
    return callTreeName;
  }
  
  public void setCallTreeName(String callTreeName) {
    this.callTreeName = callTreeName;
  }
  
  public TraceMetaData withCallTreeName(String callTreeName) {
    this.callTreeName = callTreeName;
    return this;
  }
  
  public String getEntityName() {
    return entityName;
  }
  
  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }
  
  public TraceMetaData withEntityName(String entityName) {
    this.entityName = entityName;
    return this;
  }
}
