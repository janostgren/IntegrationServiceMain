package com.apica.backend.integrationservice.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apica.backend.integrationservice.exception.DocumentNotFoundException;
import com.apica.backend.integrationservice.exception.IntegrationProfileNotFoundException;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.repository.IntegrationProfileRepository;
import com.apica.backend.integrationservice.service.IntegrationProfileService;


@Service
public class IntegrationProfileServiceIimpl implements IntegrationProfileService {

  @Autowired
  private IntegrationProfileRepository integrationProfileRepository;

  @Override
  public List<IntegrationProfile> findAll() {
    return integrationProfileRepository.findAll();
  }

  @Override
  public IntegrationProfile findById(String id) {
 
    return integrationProfileRepository.findById(id).orElseThrow(() -> new IntegrationProfileNotFoundException(id)); 
  }

  @Override
  public List<IntegrationProfile> findByCustomerId(String customerId) {
    return integrationProfileRepository.findByCustomerId(customerId);
  }

  @Override
  public List<IntegrationProfile> findByCustomerIdAndUserId(String customerId, String userId) {
    return integrationProfileRepository.findByCustomerIdAndUserId(customerId, userId);
  }

  @Override
  public IntegrationProfile save(IntegrationProfile profile) {
    return integrationProfileRepository.save(profile);
    
  }

  @Override
  public IntegrationProfile insert(IntegrationProfile profile) {
    return integrationProfileRepository.save(profile);
  }

  @Override
  public void deleteById(String id) {
    integrationProfileRepository.deleteById(id);
    
  }
}


