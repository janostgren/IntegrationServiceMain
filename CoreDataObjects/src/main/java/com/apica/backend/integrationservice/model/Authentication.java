package com.apica.backend.integrationservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Authentication implements Serializable {

 
  private List<Property> properties = new ArrayList<Property>();
  private final static long serialVersionUID = 6416222437981275215L;

  /**
   * No args constructor for use in serialization
   *
   */
  public Authentication() {}

  /**
   *
   * @param type
   * @param properties
   */
  public Authentication(List<Property> properties) {
    super();
   
    this.properties = properties;
  }

 
  public List<Property> getProperties() {
    return properties;
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  public Authentication withProperties(List<Property> properties) {
    this.properties = properties;
    return this;
  }

}
