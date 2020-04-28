package com.apica.backend.integrationservice.adapter.dto.instana;

import java.io.Serializable;

public class TagFilter implements Serializable {

  private String name;
  private String operator;
  private String value;
  private final static long serialVersionUID = -2497531068643717012L;

  /**
   * No args constructor for use in serialization
   *
   */
  public TagFilter() {}

  /**
   *
   * @param name
   * @param value
   * @param operator
   */
  public TagFilter(String name, String operator, String value) {
    super();
    this.name = name;
    this.operator = operator;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TagFilter withName(String name) {
    this.name = name;
    return this;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public TagFilter withOperator(String operator) {
    this.operator = operator;
    return this;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TagFilter withValue(String value) {
    this.value = value;
    return this;
  }

}
