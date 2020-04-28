package com.apica.backend.integrationservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "platforms")
public class Platform implements Serializable {

  @Id
  private String id;
  private String name;
  private Boolean apmPlatform;
  private Boolean metricsSupport;
  private String authentication;
  private List<HttpHeader> httpHeaders = new ArrayList<HttpHeader>();
  private TraceMetaData traceMetaData;
  private SecurityScheme securityScheme;
  private final static long serialVersionUID = 5384504173125557567L;

  /**
   * No args constructor for use in serialization
   *
   */
  public Platform() {}

  /**
   *
   * @param metricsSupport
   * @param traceMetaData
   * @param apmPlatform
   * @param securityScheme
   * @param id
   * @param httpHeaders
   * @param name
   * @param authentication
   */
  public Platform(String id, String name, Boolean apmPlatform, Boolean metricsSupport,
      String authentication, List<HttpHeader> httpHeaders, TraceMetaData traceMetaData,
      SecurityScheme securityScheme) {
    super();
    this.id = id;
    this.name = name;
    this.apmPlatform = apmPlatform;
    this.metricsSupport = metricsSupport;
    this.authentication = authentication;
    this.httpHeaders = httpHeaders;
    this.traceMetaData = traceMetaData;
    this.securityScheme = securityScheme;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Platform withId(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Platform witName(String name) {
    this.name = name;
    return this;
  }

  public Boolean getApmPlatform() {
    return apmPlatform;
  }

  public void setApmPlatform(Boolean apmPlatform) {
    this.apmPlatform = apmPlatform;
  }

  public Platform withApmPlatform(Boolean apmPlatform) {
    this.apmPlatform = apmPlatform;
    return this;
  }

  public Boolean getMetricsSupport() {
    return metricsSupport;
  }

  public void setMetricsSupport(Boolean metricsSupport) {
    this.metricsSupport = metricsSupport;
  }

  public Platform withMetricsSupport(Boolean metricsSupport) {
    this.metricsSupport = metricsSupport;
    return this;
  }

  public String getAuthentication() {
    return authentication;
  }

  public void setAuthentication(String authentication) {
    this.authentication = authentication;
  }

  public Platform withAuthentication(String authentication) {
    this.authentication = authentication;
    return this;
  }

  public List<HttpHeader> getHttpHeaders() {
    return httpHeaders;
  }

  public void setHttpHeaders(List<HttpHeader> httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public Platform withHttpHeaders(List<HttpHeader> httpHeaders) {
    this.httpHeaders = httpHeaders;
    return this;
  }

  public TraceMetaData getTaceMetaData() {
    return traceMetaData;
  }

  public void setTraceMetaData(TraceMetaData traceMetaData) {
    this.traceMetaData = traceMetaData;
  }

  public Platform withCallTreeMetaData(TraceMetaData callTreeMetaData) {
    this.traceMetaData = callTreeMetaData;
    return this;
  }

  public SecurityScheme getSecurityScheme() {
    return securityScheme;
  }

  public void setSecurityScheme(SecurityScheme securityScheme) {
    this.securityScheme = securityScheme;
  }

  public Platform withSecurityScheme(SecurityScheme securityScheme) {
    this.securityScheme = securityScheme;
    return this;
  }

}
