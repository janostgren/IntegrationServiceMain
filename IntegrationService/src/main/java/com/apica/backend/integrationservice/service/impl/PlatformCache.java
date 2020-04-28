package com.apica.backend.integrationservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.stereotype.Service;
import com.apica.backend.integrationservice.adapter.RemoteAPMAdapter;
import com.apica.backend.integrationservice.adapter.RemoteMetricsSupport;
import com.apica.backend.integrationservice.adapter.RemoteSystemAdapter;
import com.apica.backend.integrationservice.exception.InvalidReqestException;
import com.apica.backend.integrationservice.exception.PlatformNotFoundException;
import com.apica.backend.integrationservice.model.Platform;
import com.apica.backend.integrationservice.service.PlatformService;
import com.apica.backend.integrationservice.util.CommonUtils;

@Service
public class PlatformCache {
  @Autowired
  private PlatformService platformService;
  private static final Logger logger = LoggerFactory.getLogger(PlatformCache.class);
  private HashMap<String, Platform> platformMap = new HashMap<String, Platform>();
  private HashMap<String, RemoteSystemAdapter> adapterMap =
      new HashMap<String, RemoteSystemAdapter>();
  
  public Platform getPlatform(String platformId) {
    Platform platform = platformMap.get(platformId);
    if (CommonUtils.isNull(platform))
      throw new PlatformNotFoundException(platformId);
    return platform;
  }
  
  public List<Platform> getPlatforms() {
    return platformMap.values().stream().collect(Collectors.toList());
  }
  
  public RemoteSystemAdapter getAdapter(String platformId) {
    RemoteSystemAdapter adapter = adapterMap.get(platformId);
    if (CommonUtils.isNull(adapter)) {
      getPlatform(platformId);
      throw new InvalidReqestException(String.format("No adapter for platform %s", platformId));
    }
    return adapter;
  }
  
  public RemoteAPMAdapter getAPMAdapter(String platformId) {
    RemoteSystemAdapter adapter = getAdapter(platformId);
    if (adapter instanceof RemoteAPMAdapter)
      return (RemoteAPMAdapter) adapter;
    throw new InvalidReqestException(String.format("No APM adapter for platform %s", platformId));
  }
  
  @PostConstruct
  public void init() {
    /*
     * Get all platforms from repository
     */
    List<Platform> platforms = platformService.findAll();
    for (Platform platform : platforms) {
      logger.info("platform: id={}", platform.getId());
      String adapterClassName = String
          .format("com.apica.backend.integrationservice.adapter.impl.%sAdapter", platform.getId());
      try {
        @SuppressWarnings("unchecked")
        Class<RemoteSystemAdapter> adapterClass =
            (Class<RemoteSystemAdapter>) ClassUtils.forName(adapterClassName, null);
        RemoteSystemAdapter adapter = adapterClass.newInstance();
        adapter.setPlatform(platform);
        platform.setApmPlatform(adapter instanceof RemoteAPMAdapter);
        platform.setMetricsSupport(adapter instanceof RemoteMetricsSupport);
        logger.info("RemoteSystem Adapter {} class={}, apm={}, metricssupport={}",
            adapter.getAdapterId(), adapter.getClass().getName(), platform.getApmPlatform(),
            platform.getMetricsSupport());
        platformMap.put(platform.getId(), platform);
        adapterMap.put(platform.getId(), adapter);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        logger.error(e.getMessage(), e);
      }
    }
  }
}
