package com.apica.backend.integrationservice.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.apica.backend.integrationservice.service.PlatformService;
import com.apica.backend.integrationservice.service.impl.PlatformCache;
import com.apica.backend.integrationservice.exception.DocumentNotFoundException;
import com.apica.backend.integrationservice.exception.PlatformNotFoundException;
import com.apica.backend.integrationservice.model.Platform;

@RestController
@RequestMapping(value = "/platforms")
public class PlatformController {
  @Autowired
  private PlatformCache platformCache;
  
  @GetMapping(value = "/{id}")
  public Platform findById(@PathVariable("id") String id) {
    Platform platform = platformCache.getPlatform(id);
    if (platform == null)
      throw new PlatformNotFoundException(id);
    return platform;
  }
  
  @GetMapping(value = "")
  public List<Platform> findAll() {
    // return platformService.findAll();
    return platformCache.getPlatforms();
  }
}
