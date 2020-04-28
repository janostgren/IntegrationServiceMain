package com.apica.backend.integrationservice.model;

import java.io.Serializable;

public class HttpHeader implements Serializable {

  private String name;
  private String value;
  private final static long serialVersionUID = 2175505986462987582L;

  /**
   * No args constructor for use in serialization
   *
   */
  public HttpHeader() {}

  /**
   *
   * @param name
   * @param value
   */
  public HttpHeader(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HttpHeader withName(String name) {
    this.name = name;
    return this;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public HttpHeader withValue(String value) {
    this.value = value;
    return this;
  }
}
