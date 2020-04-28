package com.apica.backend.integrationservice.adapter.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import com.apica.backend.integrationservice.adapter.AbstractAPMAdapter;
import com.apica.backend.integrationservice.adapter.dto.instana.InstanaRequestFilter;
import com.apica.backend.integrationservice.adapter.dto.instana.TagFilter;
import com.apica.backend.integrationservice.adapter.dto.instana.TimeFrame;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.dto.APMPerformanceEntity;
import com.apica.backend.integrationservice.dto.APMTrace;
import com.apica.backend.integrationservice.dto.TimeRange;
import com.apica.backend.integrationservice.dto.TraceFilter;
import com.apica.backend.integrationservice.model.HttpHeader;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;
import com.apica.backend.integrationservice.util.CommonUtils;

public class InstanaAdapter extends AbstractAPMAdapter {
  static final String MONTORING_PATH = "api/application-monitoring";
  private static final Logger logger = LoggerFactory.getLogger(InstanaAdapter.class);
  
  private MultiValueMap<String, String> queryMap() {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    return queryParams;
  }
  
  private void addTimeRangeParams(TimeRange timeRange, MultiValueMap<String, String> queryParams) {
    if (timeRange != null) {
      long toTime = timeRange.getToTime();
      long now = System.currentTimeMillis();
      long windowsSize = timeRange.getDurationInMinutes() * (MsToMinute);
      // String toTimeString= String.valueOf(toTime);
      if (toTime == 0L) {
        queryParams.add("windowsSize", String.valueOf(windowsSize));
      } else if (toTime < 0L) {
        long to = now - (toTime * MsToMinute);
        queryParams.add("to", String.valueOf(to));
        queryParams.add("windowsSize", String.valueOf(windowsSize));
      } else {
        queryParams.add("to", String.valueOf(toTime));
        queryParams.add("windowsSize", String.valueOf(windowsSize));
      }
    }
  }
  
  private TimeFrame fromTimeRange(TimeRange timeRange) {
    TimeFrame timeFrame = new TimeFrame();
    if (timeRange != null) {
      long toTime = timeRange.getToTime();
      long now = System.currentTimeMillis();
      timeFrame.setWindowSize(timeRange.getDurationInMinutes() * MsToMinute);
      // String toTimeString= String.valueOf(toTime);
      if (toTime == 0L) {
        timeFrame.setTo(now);
      } else if (toTime < 0L) {
        long to = now - (toTime * MsToMinute);
        timeFrame.setTo(to);
      } else {
        timeFrame.setTo(timeRange.getToTime());
      }
    }
    return timeFrame;
  }
  
  @Override
  public List<APMBasicEnity> getApplications(IntegrationProfile profile, boolean active) {
    List<APMBasicEnity> apps = new ArrayList<APMBasicEnity>();
    MultiValueMap<String, String> queryParams = queryMap();
    if (active) {
      TimeRange timeRange = new TimeRange(0, 60);
      addTimeRangeParams(timeRange, queryParams);
    }
    WebClient client = initWebClient(profile);
    JsonNode json =
        client.get().uri(uriBuilder -> uriBuilder.path(MONTORING_PATH).path("/applications")
            .queryParams(queryParams).build()).retrieve().bodyToMono(JsonNode.class).block();
    JsonNode items = json.get("items");
    Iterator<JsonNode> iter = items.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String id = row.get("id").asText();
      String name = row.get("label").asText();
      APMBasicEnity app = new APMBasicEnity();
      app.setId(id);
      app.setName(name);
      app.setDescription(row.get("entityType").asText() + " " + row.get("boundaryScope").asText());
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
      TimeRange timeRange = new TimeRange(0, 120);
      addTimeRangeParams(timeRange, queryParams);
    }
    WebClient client = initWebClient(profile);
    String appPath = String.format(";id=%s;", profile.getApplicationId());
    JsonNode json = client.get()
        .uri(uriBuilder -> uriBuilder.path(MONTORING_PATH).path("/applications").path(appPath)
            .path("/services").queryParams(queryParams).build())
        .retrieve().bodyToMono(JsonNode.class).block();
    JsonNode items = json.get("items");
    logger.debug("application={}, number of services={}", profile.getApplication(), items.size());
    Iterator<JsonNode> iter = items.iterator();
    String entityType;
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String id = row.get("id").asText();
      String name = row.get("label").asText();
      APMPerformanceEntity entity = new APMPerformanceEntity();
      entity.setId(id);
      entity.setName(name);
      entityType = String.format("%s %s", row.get("types").get(0).asText(""),
          row.get("entityType").asText(""));
      entity.setEntityType(entityType);
      entity.setComponentId("Technology");
      entity.setComponentName(row.get("technologies").get(0).asText(""));
      entities.add(entity);
    }
    return entities;
  }
  
  @Override
  public List<APMTrace> getTraces(IntegrationProfile profile, TimeRange timeRange,
      TraceFilter traceFilter) {
    // Get a map of entities (services) to enhance trace information.
    Map<String, APMPerformanceEntity> entityMap = this.getPerformanceEntities(profile, true)
        .stream().collect(Collectors.toMap(APMPerformanceEntity::getId, Function.identity()));;
    List<APMTrace> traces = new ArrayList<APMTrace>();
    InstanaRequestFilter body = new InstanaRequestFilter();
    body.setTimeFrame(this.fromTimeRange(timeRange));
    List<TagFilter> tags = new ArrayList<TagFilter>();
    TagFilter app = new TagFilter();
    app.setName("application.id");
    app.setValue(profile.getApplicationId());
    app.setOperator("EQUALS");
    tags.add(app);
    TagFilter latency = new TagFilter();
    latency.setName("call.latency");
    latency.setOperator("GREATER_THAN");
    latency.setValue("1");
    tags.add(latency);
    if (traceFilter != null) {
      HttpHeader header = traceFilter.getHttpHeader();
      if (!CommonUtils.isNull(header) && CommonUtils.notEmpty(header.getName())
          && CommonUtils.notEmpty(header.getValue())) {
        TagFilter headerFilter = new TagFilter();
        headerFilter.setName("call.http.header");
        headerFilter.setOperator("EQUALS");
        headerFilter
            .setValue(String.format("%s=%s", header.getName().toLowerCase(), header.getValue()));
        tags.add(headerFilter);
        if (traceFilter.getEntities() != null && !traceFilter.getEntities().isEmpty()) {
          TagFilter serviceFilter = new TagFilter();
          serviceFilter.setOperator("EQUALS");
          serviceFilter.setName("service.id");
          for (String serviceId : traceFilter.getEntities()) {
            serviceFilter.setValue(serviceId);
            tags.add(serviceFilter);
          }
        }
      }
    }
    body.setTagFilters(tags);
    WebClient client = this.initWebClient(profile);
    JsonNode json = client.post()
        .uri(uriBuilder -> uriBuilder.path(MONTORING_PATH).path("/analyze/traces").build())
        .accept(MediaType.APPLICATION_JSON)
        // .body(BodyInserters.fromObject(body))
        .body(Mono.just(body), InstanaRequestFilter.class).retrieve().bodyToMono(JsonNode.class)
        .block();
    Iterator<JsonNode> iter = json.get("items").iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next().get("trace");
      String traceId = row.get("id").asText();
      APMTrace trace = new APMTrace();
      trace.setTraceType("Trace");
      JsonNode service = row.get("service");
      APMPerformanceEntity entity =
          entityMap.getOrDefault(service.get("id").asText(""), new APMPerformanceEntity());
      if (CommonUtils.notEmpty(entity.getId())) {
        entity.setId(service.get("id").asText());
        entity.setName(service.get("label").asText());
      } else {
        entity.setId(service.get("id").asText(""));
        entity.setName(service.get("label").asText(""));
        entity.setEntityType(service.get("entityType").asText(""));
      }
      trace.setEntity(entity);
      trace.setTraceId(traceId);
      trace.setName(row.get("label").asText());
      trace.setStartTime(row.get("startTime").asLong());
      trace.setDuration(row.get("duration").asLong());
      trace.setResource("");
      trace.setResourceType("N/A");
      if (service != null) {
        trace.setResource(service.get("label").asText());
        trace.setResourceType(service.get("entityType").asText());
      }
      trace.setFullTrace(true);
      boolean error = row.get("erroneous").asBoolean(false);
      trace.setError(error);
      trace.setStatusName(!error ? "NORMAL" : "ERROR");
      int statusCode = !error ? 0 : 3;
      trace.setStatusCode(statusCode);
      String format =
          "%s/#/analyze;callList.dataSource=calls;callList.groupBy=()~;callList.tagFilter;callList.previewEnabled=false;rawItems.orderBy=latency;rawItems.orderDirection=DESC/trace;traceId=%s;callId=%s/tree?timeline.to=%d&timeline.ws=%d&timeline.fm";
      long windowSize = MsToMinute * 10;
      long toTime = row.get("startTime").asLong() + windowSize / 2;
      String drillDownLink = String.format(format, profile.getUrl(), trace.getTraceId(),
          trace.getTraceId(), toTime, windowSize);
      trace.setDrillDownLink(drillDownLink);
      traces.add(trace);
    }
    return traces;
  }
  
  @Override
  public String getAdapterId() {
    // TODO Auto-generated method stub
    return "InstanaAdapter version 1.0";
  }
  
  @Override
  protected WebClient initWebClient(IntegrationProfile profile) {
    Map<String, String> authProps = super.authProperties(profile);
    String apiKey = String.format("apitoken %s", authProps.get("apiKey"));
    return WebClient.builder().baseUrl(profile.getUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey).filter(logRequest).build();
  }
}
