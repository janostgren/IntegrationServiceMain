package com.apica.backend.integrationservice.adapter;

import com.apica.backend.integrationservice.model.Platform;
import java.util.Map;
import com.apica.backend.integrationservice.model.HttpHeader;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.model.Property;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractAPMAdapter implements RemoteAPMAdapter {
  protected static final long MsToMinute = (1000 * 60);
  private static final Logger logger = LoggerFactory.getLogger(AbstractAPMAdapter.class);
  
  public Map<String, String> authProperties(IntegrationProfile profile) {
    Map<String, String> props = profile.getAuthentication().getProperties().stream()
        .collect(Collectors.toMap(Property::getKey, Property::getValue));
    return props;
  }
  
  public ExchangeFilterFunction logRequest = (request, nextFilter) -> {
    logger.debug("Logrequest: {} {}, paramters={}", request.method().name(), request.url(),
        request.url().getQuery());
    if (request.body() != null) {
      logger.debug("body={}", request.body().toString());
    }
    return nextFilter.exchange(request);
  };
  
  abstract protected WebClient initWebClient(IntegrationProfile profile);
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.apica.backend.integrationservice.remote.apm.RemoteAPMAdapter#setPlatform(com.apica.backend.
   * integrationservice.model.Platform)
   */
  private Platform platform;
  
  @Override
  public void setPlatform(Platform platform) {
    // TODO Auto-generated method stub
    this.platform = platform;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see com.apica.backend.integrationservice.remote.apm.RemoteAPMAdapter#getPlatorm()
   */
  @Override
  public Platform getPlatform() {
    // TODO Auto-generated method stub
    return this.platform;
  }
}
