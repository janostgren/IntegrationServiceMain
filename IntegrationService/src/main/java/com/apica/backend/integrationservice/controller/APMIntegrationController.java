package com.apica.backend.integrationservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.apica.backend.integrationservice.adapter.RemoteAPMAdapter;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.dto.APMPerformanceEntity;
import com.apica.backend.integrationservice.dto.APMTrace;
import com.apica.backend.integrationservice.dto.TimeRange;
import com.apica.backend.integrationservice.dto.TraceFilter;
import com.apica.backend.integrationservice.dto.TraceRequestBody;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.service.IntegrationProfileService;
import com.apica.backend.integrationservice.service.impl.PlatformCache;
import com.apica.backend.integrationservice.validation.IntegrationProfileValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/apm")
@Api(value = "Integration with back-end APM Systems.")
public class APMIntegrationController {
  @Autowired
  private PlatformCache platformCache;
  @Autowired
  private IntegrationProfileService integrationProfileService;
  
  @ApiOperation(value = "Get all applications from APM system  with IntegrationProfile id")
  @GetMapping(value = "/applications/{profileId}")
  List<APMBasicEnity> getApplications(
      @ApiParam(value = "The Integration Profile id",required = true)
      @PathVariable("profileId") String profileId) {
    IntegrationProfile profile = integrationProfileService.findById(profileId);
    RemoteAPMAdapter adapter = platformCache.getAPMAdapter(profile.getPlatformId());
    return adapter.getApplications(profile, true);
  }
  
  @ApiOperation(
      value = "Get all applications from APM system. Used for validation before IntegrationProfile is created")
  @PostMapping(value = "/verify/profile")
  List<APMBasicEnity> verifyProfille(@RequestBody IntegrationProfile profile) {
    IntegrationProfileValidator.validateIntegrationProfile(profile, false, false);
    RemoteAPMAdapter adapter = platformCache.getAPMAdapter(profile.getPlatformId());
    return adapter.getApplications(profile, true);
  }
  
  @ApiOperation(value = "Get all performance objects from APM system  with IntegrationProfile id")
  @GetMapping(value = "/performanceEntities/{profileId}")
  List<APMPerformanceEntity> getPerformanceEntities(
      @ApiParam(value = "The Integration Profile id",required = true)
      @PathVariable("profileId") String profileId) {
    IntegrationProfile profile = integrationProfileService.findById(profileId);
    RemoteAPMAdapter adapter = platformCache.getAPMAdapter(profile.getPlatformId());
    return adapter.getPerformanceEntities(profile, true);
  }
  
  @ApiOperation(value = "Get traces from APM system with IntegrationProfile id")
  @PostMapping(value = "/traces/{profileId}")
  List<APMTrace> traces(
      @ApiParam(value = "The Integration Profile id",required = true)
      @PathVariable("profileId") String profileId,
      @RequestBody TraceRequestBody body) {
    IntegrationProfile profile = integrationProfileService.findById(profileId);
    RemoteAPMAdapter adapter = platformCache.getAPMAdapter(profile.getPlatformId());
    TraceFilter traceFilter = body.getTraceFilter();
    TimeRange timeRange = body.getTimeRange();
    return adapter.getTraces(profile, timeRange, traceFilter);
  }
}
