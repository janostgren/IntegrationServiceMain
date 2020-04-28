package com.apica.backend.integrationservice.test.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.dto.APMPerformanceEntity;
import com.apica.backend.integrationservice.dto.APMTrace;
import com.apica.backend.integrationservice.dto.TimeRange;
import com.apica.backend.integrationservice.dto.TraceFilter;
import com.apica.backend.integrationservice.model.HttpHeader;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppDynamicsRestClient {
  // private WebClient webClient;
  private String baseUrl;
  private String user;
  private String password;
  private String application;

  private static final long MsToMinute = (1000 * 60);
  private static final int STATUS_NORMAL=0;
  private static final int STATUS_SLOW=1;
  private static final int STATUS_VERY_SLOW=2;
  private static final int STATUS_ERROR=3;
  private static final int STATUS_STALL=4;
  
  private static final Logger logger=LoggerFactory.getLogger(AppDynamicsRestClient.class);


  public static void main(String[] args) {
    AppDynamicsRestClient client = new AppDynamicsRestClient();
    client.setup(args);
    TimeRange timeRange = new TimeRange(0, 60);
    timeRange.setToTime(System.currentTimeMillis());
    List<APMBasicEnity> apps = client.getApplications(timeRange);
    for (APMBasicEnity app : apps) {
      logger.debug("Application: id={}, name={}, description={}", app.getId(), app.getName(),
          app.getDescription());
      if (client.application.equals(app.getName())) {
        List<APMPerformanceEntity> txs = client.getTransactions(timeRange, app.getName());
         HashMap<String,APMPerformanceEntity> txMap =  new HashMap<String,APMPerformanceEntity>();
        for (APMPerformanceEntity tx : txs) {
          txMap.put(tx.getId(), tx);
          logger.debug("Transaction: id={}, name={}, type={}, component=({}:{})\n", tx.getId(),
              tx.getName(), tx.getEntityType(), tx.getComponentId(), tx.getComponentName());
        }
        TraceFilter traceFilter = new TraceFilter();
        traceFilter.setIncludeNormal(true);
        ArrayList<String> entityIds= new  ArrayList<String>();
        entityIds.add("874521");
        traceFilter.setEntities(entityIds);
        HttpHeader httpHeader= new HttpHeader("ApicaGUID", "");
        traceFilter.setHttpHeader(httpHeader);
        
        List<APMTrace> traces = client.getTraces(timeRange, app,txMap,traceFilter); 
        for (APMTrace trace : traces) {
          logger.debug("Trace: id={}, name={}, time={}, duration={}, resource=({}:{}), status=({}:{}), error={} full trace={},\ndrilldownLink={}"
              , trace.getTraceId()
              , trace.getName()
              , trace.getStartTime()
              , trace.getDuration()
              ,trace.getResource()
              ,trace.getResourceType()
              , trace.getStatusCode()
              ,trace.getStatusName()
              ,trace.isError()
              ,trace.isFullTrace()
              ,trace.getDrillDownLink()
              
              );
        }
      }
    }
  }



  public void setup(String[] args) {
    this.baseUrl = args[0];
    this.user = args[1];
    this.password = args[2];
    this.application = args[3];
  }

  WebClient initWebClient() {
    /*
     * HashMap <String,String> defvars=new HashMap <String,String>(); defvars.put("output","json");
     */
    return WebClient.builder().baseUrl(baseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .filter(ExchangeFilterFunctions.basicAuthentication(user, password)).build();
  }

  public MultiValueMap<String, String> queryMap() {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    queryParams.add("output", "json");
    return queryParams;
  }


  public void addTimeRangeParams(TimeRange timeRange, MultiValueMap<String, String> queryParams) {
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



  List<APMBasicEnity> getApplications(TimeRange timeRange) {
    List<APMBasicEnity> apps = new ArrayList<APMBasicEnity>();
    MultiValueMap<String, String> queryParams = queryMap();
    addTimeRangeParams(timeRange, queryParams);
    WebClient client = this.initWebClient();
    JsonNode json = client.get()

        .uri(uriBuilder -> uriBuilder.path("controller/rest").path("/applications")
            .queryParams(queryParams).build())
        .retrieve().bodyToMono(JsonNode.class).block();

    Iterator<JsonNode> iter = json.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String id = row.get("id").asText();
      String name = row.get("name").asText();
      String description = row.get("description").asText();
      // System.out.format("row id=%s, name=%s\n", id, name);
      APMBasicEnity app = new APMBasicEnity();
      app.setId(id);
      app.setName(name);
      app.setDescription(description);
      apps.add(app);
    }
    return apps;
  }


  List<APMPerformanceEntity> getTransactions(TimeRange timeRange, String application) {
    MultiValueMap<String, String> queryParams = this.queryMap();
    addTimeRangeParams(timeRange, queryParams);
    List<APMPerformanceEntity> txs = new ArrayList<APMPerformanceEntity>();
    WebClient client = this.initWebClient();

    JsonNode json = client.get()

        .uri(uriBuilder -> uriBuilder.path("controller/rest").path("/applications/")
            .path(application).path("/business-transactions").queryParams(queryParams)

            .build())
        .retrieve().bodyToMono(JsonNode.class).block();
    Iterator<JsonNode> iter = json.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String id = row.get("id").asText();
      String name = row.get("name").asText();

      // System.out.format("row id=%s, name=%s\n", id, name);
      APMPerformanceEntity tx = new APMPerformanceEntity();
      tx.setId(id);
      tx.setName(name);
      tx.setEntityType("Business Transaction");
      tx.setComponentId(row.get("tierId").asText());
      tx.setComponentName(row.get("tierName").asText());
      txs.add(tx);
    }
    return txs;
  }

  public List<APMTrace> getTraces(TimeRange timeRange, APMBasicEnity app,HashMap<String,APMPerformanceEntity> txMap, TraceFilter traceFilter) {
    List<APMTrace> traces = new ArrayList<APMTrace>();

    MultiValueMap<String, String> queryParams = this.queryMap();
    addTimeRangeParams(timeRange, queryParams);
    queryParams.add("bad-request", traceFilter.isIncludeNormal()?"false":"true");
    if(traceFilter.getEntities() != null)
      queryParams.add("business-transaction-ids", String.join(",", traceFilter.getEntities()));
    HttpHeader httpHeader = traceFilter.getHttpHeader();
    if(httpHeader != null && httpHeader.getName() != null && !httpHeader.getName().isEmpty()) {
      queryParams.add("data-collector-type", "Http Parameter");
      queryParams.add("data-collector-name", "Header-"+httpHeader.getName());
      if (httpHeader.getValue() != null && !httpHeader.getValue().isEmpty())
        queryParams.add("data-collector-value", httpHeader.getValue());
    }

    WebClient client = this.initWebClient();
    JsonNode json = client.get()

        .uri(uriBuilder -> uriBuilder.path("controller/rest").path("/applications/")
            .path(app.getName()).path("/request-snapshots").queryParams(queryParams)

            .build())
        .retrieve().bodyToMono(JsonNode.class).block();
    Iterator<JsonNode> iter = json.iterator();
    while (iter.hasNext()) {
      JsonNode row = iter.next();
      String traceId = row.get("requestGUID").asText();
      
      String txId = row.get("businessTransactionId").asText();
    
      

      APMTrace trace = new APMTrace();
      APMPerformanceEntity entity =txMap.get(txId);
      trace.setTraceType("Snapshot");
      trace.setEntity(entity);
      trace.setTraceId(traceId);
      trace.setName(entity.getName());
      trace.setStartTime(row.get("serverStartTime").asLong());
      trace.setDuration(row.get("timeTakenInMilliSecs").asLong()); 
      trace.setResource("");
      trace.setResourceType("N/A");
      String resourceType="URL";
      JsonNode  resource=row.get(resourceType);
      
      if(resource != null) {
        trace.setResource(resource.asText());
        trace.setResourceType(resourceType);
      }
      trace.setFullTrace(row.get("hasDeepDiveData").asBoolean(false));
      trace.setError(row.get("errorOccured").asBoolean(false));
      String userExperience= row.get("userExperience").asText();
      trace.setStatusName(userExperience);
      int statusCode=0;
      switch(userExperience) {
        case "NORMAL":
          statusCode=STATUS_NORMAL;
          break;
        case "SLOW":
          statusCode=STATUS_SLOW;
          break;
        case "VERY_SLOW":
          statusCode=STATUS_VERY_SLOW;
          break;
        case "ERROR":
          statusCode=STATUS_ERROR;
          break;
        case "STALL":
          statusCode=STATUS_STALL;
          break;
        
      }
      trace.setStatusCode(statusCode);
      //String format = "%s/controller/#location=APP_SNAPSHOT_VIEWER&application=%s&requestGUID=%s&rsdTime=Custom_Time_Range.BETWEEN_TIMES.%d.%d.60";
      String format="%s/controller/#/location=APP_SNAPSHOT_VIEWER&requestGUID=%s&application=%s&rsdTime=Custom_Time_Range.BETWEEN_TIMES.%d.%d.%d&tab=overview&dashboardMode=force";
      long minutes=30;
      long fromTime =trace.getStartTime() ;
      long toTime =fromTime - (MsToMinute*minutes);
      String drillDownLink = String.format(format, baseUrl,trace.getTraceId(),app.getId(),fromTime,toTime,minutes);
      trace.setDrillDownLink(drillDownLink);
      
      traces.add(trace);
    }

    return traces;
  }

}

