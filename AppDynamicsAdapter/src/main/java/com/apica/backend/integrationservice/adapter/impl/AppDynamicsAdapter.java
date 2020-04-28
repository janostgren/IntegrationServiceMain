package com.apica.backend.integrationservice.adapter.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import com.apica.backend.integrationservice.adapter.AbstractAPMAdapter;
import com.apica.backend.integrationservice.adapter.RemoteMetricsSupport;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.dto.APMPerformanceEntity;
import com.apica.backend.integrationservice.dto.APMTrace;
import com.apica.backend.integrationservice.dto.TimeRange;
import com.apica.backend.integrationservice.dto.TraceFilter;
import com.apica.backend.integrationservice.model.HttpHeader;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppDynamicsAdapter extends AbstractAPMAdapter implements RemoteMetricsSupport {
  private static final Logger logger = LoggerFactory.getLogger(AppDynamicsAdapter.class);
  // private static final long MsToMinute = (1000 * 60);
  private static final int STATUS_NORMAL = 0;
  private static final int STATUS_SLOW = 1;
  private static final int STATUS_VERY_SLOW = 2;
  private static final int STATUS_ERROR = 3;
  private static final int STATUS_STALL = 4;
  
  private MultiValueMap<String, String> queryMap() {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("output", "json");
    return queryParams;
  }
  
  private void addTimeRangeParams(TimeRange timeRange, MultiValueMap<String, String> queryParams) {
    if (timeRange != null) {
      long toTime = timeRange.getToTime();
      long now = System.currentTimeMillis();
      // String toTimeString= String.valueOf(toTime);
      if (toTime == 0L) {
        queryParams.add("time-range-type", "BEFORE_NOW");
        queryParams.add("duration-in-mins", String.valueOf(timeRange.getDurationInMinutes()));
      } else if (toTime < 0L) {
        queryParams.add("time-range-type", "BETWEEN_TIMES");
        long endTime = now - (toTime * MsToMinute);
        long startTime = endTime - (MsToMinute * timeRange.getDurationInMinutes());
        queryParams.add("start-time", String.valueOf(startTime));
        queryParams.add("end-time", String.valueOf(endTime));
      } else {
        queryParams.add("time-range-type", "BEFORE_TIME");
        queryParams.add("end-time", String.valueOf(toTime));
        queryParams.add("duration-in-mins", String.valueOf(timeRange.getDurationInMinutes()));
      }
    }
  }
  
  @Override
  protected WebClient initWebClient(IntegrationProfile profile) {
    Map<String, String> authProps = super.authProperties(profile);
    String user = String.format("%s@%s", authProps.get("user"), profile.getAccount());
    String password = authProps.get("password");
    return WebClient.builder().baseUrl(profile.getUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .filter(ExchangeFilterFunctions.basicAuthentication(user, password)).build();
  }
  
  @Override
  public String getAdapterId() {
    // TODO Auto-generated method stub
    return "AppDynamicsApapter version 1.0";
  }
  
  @Override
  public List<APMBasicEnity> getApplications(IntegrationProfile profile, boolean active) {
    logger.debug("getApplications: profile={}", profile.getName());
    List<APMBasicEnity> apps = new ArrayList<APMBasicEnity>();
    MultiValueMap<String, String> queryParams = queryMap();
    if (active) {
      TimeRange timeRange = new TimeRange(0, 60);
      addTimeRangeParams(timeRange, queryParams);
    }
    WebClient client = initWebClient(profile);
    JsonNode json =
        client.get().uri(uriBuilder -> uriBuilder.path("controller/rest").path("/applications")
            .queryParams(queryParams).build()).retrieve().bodyToMono(JsonNode.class).block();
    Iterator<JsonNode> iter = json.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String id = row.get("id").asText();
      String name = row.get("name").asText();
      String description = row.get("description").asText();
      APMBasicEnity app = new APMBasicEnity();
      app.setId(id);
      app.setName(name);
      app.setDescription(description);
      apps.add(app);
    }
    return apps;
  }
  
  @Override
  public List<APMPerformanceEntity> getPerformanceEntities(IntegrationProfile profile,
      boolean active) {
    List<APMPerformanceEntity> entities = new ArrayList<APMPerformanceEntity>();
    MultiValueMap<String, String> queryParams = queryMap();
    if (active) {
      TimeRange timeRange = new TimeRange(0, 60);
      addTimeRangeParams(timeRange, queryParams);
    }
    WebClient client = initWebClient(profile);
    JsonNode json = client.get()
        .uri(uriBuilder -> uriBuilder.path("controller/rest").path("/applications/")
            .path(profile.getApplication()).path("/business-transactions").queryParams(queryParams)
            .build())
        .retrieve().bodyToMono(JsonNode.class).block();
    Iterator<JsonNode> iter = json.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String id = row.get("id").asText();
      String name = row.get("name").asText();
      APMPerformanceEntity entity = new APMPerformanceEntity();
      entity.setId(id);
      entity.setName(name);
      entity.setEntityType("Business Transaction");
      entity.setComponentId(row.get("tierId").asText());
      entity.setComponentName(row.get("tierName").asText());
      entities.add(entity);
    }
    return entities;
  }
  
  @Override
  public List<APMTrace> getTraces(IntegrationProfile profile, TimeRange timeRange,
      TraceFilter traceFilter) {
    List<APMPerformanceEntity> entitites = this.getPerformanceEntities(profile, true);
    Map<String, APMPerformanceEntity> entityMap = entitites.stream()
        .collect(Collectors.toMap(APMPerformanceEntity::getId, Function.identity()));;
    List<APMTrace> traces = new ArrayList<APMTrace>();
    MultiValueMap<String, String> queryParams = this.queryMap();
    addTimeRangeParams(timeRange, queryParams);
    queryParams.add("bad-request", traceFilter.isIncludeNormal() ? "false" : "true");
    if (traceFilter.getEntities() != null)
      queryParams.add("business-transaction-ids", String.join(",", traceFilter.getEntities()));
    HttpHeader httpHeader = traceFilter.getHttpHeader();
    if (httpHeader != null && httpHeader.getName() != null && !httpHeader.getName().isEmpty()) {
      queryParams.add("data-collector-type", "Http Parameter");
      queryParams.add("data-collector-name", "Header-" + httpHeader.getName());
      if (httpHeader.getValue() != null && !httpHeader.getValue().isEmpty())
        queryParams.add("data-collector-value", httpHeader.getValue());
    }
    WebClient client = this.initWebClient(profile);
    JsonNode json = client.get()
        .uri(uriBuilder -> uriBuilder.path("controller/rest").path("/applications/")
            .path(profile.getApplicationId()).path("/request-snapshots").queryParams(queryParams)
            .build())
        .retrieve().bodyToMono(JsonNode.class).block();
    Iterator<JsonNode> iter = json.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String traceId = row.get("requestGUID").asText();
      String txId = row.get("businessTransactionId").asText();
      APMTrace trace = new APMTrace();
      APMPerformanceEntity entity = entityMap.get(txId);
      trace.setTraceType("Snapshot");
      trace.setEntity(entity);
      trace.setTraceId(traceId);
      if (entity != null)
        trace.setName(entity.getName());
      trace.setStartTime(row.get("serverStartTime").asLong());
      trace.setDuration(row.get("timeTakenInMilliSecs").asLong());
      trace.setResource("");
      trace.setResourceType("N/A");
      String resourceType = "URL";
      JsonNode resource = row.get(resourceType);
      if (resource != null) {
        trace.setResource(resource.asText());
        trace.setResourceType(resourceType);
      }
      trace.setFullTrace(row.get("hasDeepDiveData").asBoolean(false));
      trace.setError(row.get("errorOccured").asBoolean(false));
      String userExperience = row.get("userExperience").asText();
      trace.setStatusName(userExperience);
      int statusCode = 0;
      switch (userExperience) {
        case "NORMAL":
          statusCode = STATUS_NORMAL;
          break;
        case "SLOW":
          statusCode = STATUS_SLOW;
          break;
        case "VERY_SLOW":
          statusCode = STATUS_VERY_SLOW;
          break;
        case "ERROR":
          statusCode = STATUS_ERROR;
          break;
        case "STALL":
          statusCode = STATUS_STALL;
          break;
      }
      trace.setStatusCode(statusCode);
      // String format =
      // "%s/controller/#location=APP_SNAPSHOT_VIEWER&application=%s&requestGUID=%s&rsdTime=Custom_Time_Range.BETWEEN_TIMES.%d.%d.60";
      String format =
          "%s/controller/#/location=APP_SNAPSHOT_VIEWER&requestGUID=%s&application=%s&rsdTime=Custom_Time_Range.BETWEEN_TIMES.%d.%d.%d&tab=overview&dashboardMode=force";
      long minutes = 30;
      long fromTime = trace.getStartTime();
      long toTime = fromTime - (MsToMinute * minutes);
      String drillDownLink = String.format(format, profile.getUrl(), trace.getTraceId(),
          profile.getApplicationId(), fromTime, toTime, minutes);
      trace.setDrillDownLink(drillDownLink);
      traces.add(trace);
    }
    return traces;
  }
}
