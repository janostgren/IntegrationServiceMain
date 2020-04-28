package com.apica.backend.integrationservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.apica.backend.integrationservice.service.IntegrationProfileService;
import com.apica.backend.integrationservice.service.impl.PlatformCache;
import com.apica.backend.integrationservice.util.CommonUtils;
import com.apica.backend.integrationservice.validation.IntegrationProfileValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.apica.backend.integrationservice.adapter.RemoteAPMAdapter;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.exception.InvalidReqestException;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.model.Platform;

@RestController
@RequestMapping(value = "/integrationprofiles")
@Api(value = "Basic managment of Integration Profiles (Crud Operations")
public class IntegrationProfileController {
  @Autowired
  private IntegrationProfileService integrationProfileService;
  @Autowired
  private PlatformCache platformCache;
  
  @GetMapping(value = "/{id}")
  @ApiOperation(value = "Get one Integration Profile")
  public IntegrationProfile findById( @ApiParam(value = "The generated unique id",required = true) @PathVariable("id") String id) {
    return integrationProfileService.findById(id);
  }
  
  @ApiOperation(value = "Create a new Integration Profile")
  @PostMapping(value = "")
  public IntegrationProfile insert(@RequestBody IntegrationProfile profile) {
    IntegrationProfileValidator.validateIntegrationProfile(profile, true, false);
    Platform platform = platformCache.getPlatform(profile.getPlatformId());
    if (platform.getApmPlatform()) {
      validateAPMApplication(profile);
    }
    return integrationProfileService.insert(profile);
  }
  
  @ApiOperation(value = "Update one Integration Profile")
  @PutMapping(value = "")
  public IntegrationProfile save(@RequestBody IntegrationProfile profile) {
    IntegrationProfileValidator.validateIntegrationProfile(profile, true, true);
    Platform platform = platformCache.getPlatform(profile.getPlatformId());
    if (platform.getApmPlatform()) {
      validateAPMApplication(profile);
    }
    return integrationProfileService.save(profile);
  }
  
  @ApiOperation(value = "Delete one Integration Profile")
  @DeleteMapping(value = "/{id}")
  public void deleteById(@PathVariable("id") String id) {
    integrationProfileService.deleteById(id);
  }
  
  @ApiOperation(value = "Get all  Integration Profiles")
  @GetMapping(value = "")
  public List<IntegrationProfile> findAll() {
    return integrationProfileService.findAll();
  }
  
  @ApiOperation(value = "Get all Integration Profiles for customer or customer and user")
  @GetMapping(value = "/customer")
  public List<IntegrationProfile> findByCustomer(  
      @ApiParam(value = "The customer id in ASM",required = true)
      @RequestParam String customerId,
      @RequestParam(required = false) 
      @ApiParam(value = "The user id in ASM",required = false)
      String userId) {
    if (userId == null || userId.isEmpty()) {
      return integrationProfileService.findByCustomerId(customerId);
    } else {
      return integrationProfileService.findByCustomerIdAndUserId(customerId, userId);
    }
  }
  
  private boolean validateAPMApplication(IntegrationProfile profile) {
    RemoteAPMAdapter adapter = platformCache.getAPMAdapter(profile.getPlatformId());
    for (APMBasicEnity app : adapter.getApplications(profile, true)) {
      if (profile.getApplication().equals(app.getName())) {
        if (!app.getId().equals(profile.getApplicationId()))
          profile.setApplicationId(app.getId());
        return true;
      }
    }
    throw new InvalidReqestException(
        String.format("Application [%s] not found in apm back-end", profile.getApplication()));
  }
}
