package com.apica.backend.integrationservice.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apica.backend.integrationservice.exception.DocumentNotFoundException;
import com.apica.backend.integrationservice.exception.PlatformNotFoundException;
import com.apica.backend.integrationservice.model.Platform;
import com.apica.backend.integrationservice.repository.PlatformRepository;
import com.apica.backend.integrationservice.service.PlatformService;


@Service
public class PlatformServiceIimpl implements PlatformService {

  @Autowired
  private PlatformRepository platformRepository;

  @Override
  public List<Platform> findAll() {
    return platformRepository.findAll();
  }

  @Override
  public Platform findById(String id) {
   
    return platformRepository.findById(id).orElseThrow(() -> new PlatformNotFoundException(id));
   
  }

}


