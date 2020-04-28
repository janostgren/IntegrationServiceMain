package com.apica.backend.integrationservice.adapter;

import com.apica.backend.integrationservice.model.Platform;

public interface RemoteSystemAdapter {
  public String getAdapterId();
  
  public void setPlatform(Platform platform);
}
