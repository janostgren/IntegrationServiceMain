package com.apica.backend.integrationservice.model;

import java.io.Serializable;

public class SecurityScheme implements Serializable {
  private String name;
  private String type;
  private String scheme;
  private String template;
  private final static long serialVersionUID = 51219122776985633L;
  
  /**
   * No args constructor for use in serialization
   *
   */
  public SecurityScheme() {}
  
  /**
   *
   * @param template
   * @param scheme
   * @param name
   * @param type
   */
  public SecurityScheme(String name, String type, String scheme, String template) {
    super();
    this.name = name;
    this.type = type;
    this.scheme = scheme;
    this.template = template;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public SecurityScheme withName(String name) {
    this.name = name;
    return this;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public SecurityScheme withType(String type) {
    this.type = type;
    return this;
  }
  
  public String getScheme() {
    return scheme;
  }
  
  public void setScheme(String scheme) {
    this.scheme = scheme;
  }
  
  public SecurityScheme withScheme(String scheme) {
    this.scheme = scheme;
    return this;
  }
  
  public String getTemplate() {
    return template;
  }
  
  public void setTemplate(String template) {
    this.template = template;
  }
  
  public SecurityScheme withTemplate(String template) {
    this.template = template;
    return this;
  }
}
