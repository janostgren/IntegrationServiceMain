package com.apica.backend.integrationservice.model;

import java.io.Serializable;

public class Property implements Serializable {
  private String key;
  private String value;
  private final static long serialVersionUID = 3485285800099992622L;
  
  /**
   * No args constructor for use in serialization
   *
   */
  public Property() {}
  
  /**
   *
   * @param value
   * @param key
   */
  public Property(String key, String value) {
    super();
    this.key = key;
    this.value = value;
  }
  
  public String getKey() {
    return key;
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public Property withKey(String key) {
    this.key = key;
    return this;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public Property withValue(String value) {
    this.value = value;
    return this;
  }
}
