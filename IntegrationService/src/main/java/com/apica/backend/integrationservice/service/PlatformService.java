package com.apica.backend.integrationservice.service;

import java.util.List;
import com.apica.backend.integrationservice.model.Platform;

public interface PlatformService {
  List<Platform> findAll();
  
  Platform findById(String id);
}
