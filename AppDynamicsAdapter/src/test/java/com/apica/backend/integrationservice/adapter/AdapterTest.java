package com.apica.backend.integrationservice.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.dto.APMPerformanceEntity;
import com.apica.backend.integrationservice.dto.APMTrace;
import com.apica.backend.integrationservice.dto.TimeRange;
import com.apica.backend.integrationservice.dto.TraceFilter;
import com.apica.backend.integrationservice.model.Authentication;
import com.apica.backend.integrationservice.model.HttpHeader;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.model.Property;
import com.apica.backend.integrationservice.util.CommonUtils;
import com.apica.backend.integrationservice.validation.IntegrationProfileValidator;
import com.apica.backend.integrationservice.adapter.impl.AppDynamicsAdapter;

public class AdapterTest {
  AppDynamicsAdapter adapter;
  IntegrationProfile profile;
  
  @BeforeEach
  void initData() {
    adapter = new AppDynamicsAdapter();
    profile = new IntegrationProfile();
    profile.setPlatformId("AppDynamics");
    profile.setUrl("https://apica-nfr.saas.appdynamics.com");
    profile.setAccount("apica-nfr");
    profile.setApplication("TM-Dev");
    profile.setApplicationId("6001");
    @SuppressWarnings("serial")
    List<Property> props = new ArrayList<Property>() {
      {
        add(new Property("user", "ticketmonster"));
        add(new Property("password", "8BOAK37P"));
      }
    };
    profile.setAuthentication(new Authentication(props));
  }
  
  @DisplayName("Test Data")
  @Order(1)
  @Test
  void testData() {
    IntegrationProfileValidator.validateIntegrationProfile(profile, false, false);
    String id = this.adapter.getAdapterId();
    assertNotNull(id, "Id must be something");
  }
  
  @DisplayName("Get Applications")
  @Order(2)
  @Test
  void getApplications() {
    List<APMBasicEnity> apps = adapter.getApplications(profile, true);
    assertFalse(apps.isEmpty(), "Must return some applications");
    for (APMBasicEnity app : apps) {
      assertAll("applications", () -> assertFalse(CommonUtils.isEmpty(app.getId())),
          () -> assertFalse(CommonUtils.isEmpty(app.getName())));
    }
  }
  
  @DisplayName("Get Traces")
  @Order(3)
  @Test
  void getTaces() {
    TimeRange timeRange = new TimeRange(0, 60);
    TraceFilter traceFilter = new TraceFilter();
    traceFilter.setIncludeNormal(true);
    List<APMTrace> traces = adapter.getTraces(profile, timeRange, traceFilter);
    assertFalse(traces.isEmpty(), "Must return some traces");
    for (APMTrace trace : traces) {
      assertAll("applications", () -> assertFalse(CommonUtils.isEmpty(trace.getTraceId())),
          () -> assertFalse(CommonUtils.isEmpty(trace.getName())),
          () -> assertEquals("Snapshot", trace.getTraceType()),
          () -> assertNotNull(trace.getEntity()),
          () -> assertFalse(CommonUtils.isEmpty(trace.getEntity().getId())),
          () -> assertFalse(CommonUtils.isEmpty(trace.getEntity().getName()))
          
      );
    }
  }
}
