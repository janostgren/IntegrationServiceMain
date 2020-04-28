package com.apica.backend.integrationservice.adapter;

import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.model.Platform;
import java.util.List;
import com.apica.backend.integrationservice.dto.APMBasicEnity;
import com.apica.backend.integrationservice.dto.APMPerformanceEntity;
import com.apica.backend.integrationservice.dto.APMTrace;
import com.apica.backend.integrationservice.dto.TimeRange;
import com.apica.backend.integrationservice.dto.TraceFilter;

public interface RemoteAPMAdapter extends RemoteSystemAdapter {
  public Platform getPlatform();
  
  public List<APMBasicEnity> getApplications(IntegrationProfile profile, boolean active);
  
  public List<APMPerformanceEntity> getPerformanceEntities(IntegrationProfile profile,
      boolean active);
  
  public List<APMTrace> getTraces(IntegrationProfile profile, TimeRange timeRange,
      TraceFilter traceFilter);
}
