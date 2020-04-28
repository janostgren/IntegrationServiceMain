package com.apica.backend.integrationservice.dto;

import java.io.Serializable;
import java.util.List;
import com.apica.backend.integrationservice.model.HttpHeader;

public class TraceFilter implements Serializable{
  
 
  private static final long serialVersionUID = 283875412700743651L;
  private List <String> entities;
  private HttpHeader httpHeader;
  private boolean includeNormal;
  
  public TraceFilter() {
    super();
    // TODO Auto-generated constructor stub
  }

  public TraceFilter(List<String> entities, HttpHeader httpHeader,boolean includeNormal) {
    this();
    this.entities = entities;
    this.httpHeader = httpHeader;
    this.includeNormal=includeNormal;
  }

  /**
   * @return the entities
   */
  public List<String> getEntities() {
    return entities;
  }

  /**
   * @param entities the entities to set
   */
  public void setEntities(List<String> entities) {
    this.entities = entities;
  }

  /**
   * @return the httpHeader
   */
  public HttpHeader getHttpHeader() {
    return httpHeader;
  }

  /**
   * @param httpHeader the httpHeader to set
   */
  public void setHttpHeader(HttpHeader httpHeader) {
    this.httpHeader = httpHeader;
  }

  public boolean isIncludeNormal() {
    return includeNormal;
  }

  public void setIncludeNormal(boolean includeNormal) {
    this.includeNormal = includeNormal;
  }
  
  

  
  
}
