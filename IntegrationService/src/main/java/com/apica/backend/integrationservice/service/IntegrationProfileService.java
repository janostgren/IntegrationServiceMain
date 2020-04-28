package com.apica.backend.integrationservice.service;

import java.util.List;
import com.apica.backend.integrationservice.model.IntegrationProfile;;

public interface IntegrationProfileService {
  List<IntegrationProfile> findAll();
  
  IntegrationProfile findById(String id);
  
  IntegrationProfile save(IntegrationProfile profile);
  
  IntegrationProfile insert(IntegrationProfile profile);
  
  void deleteById(String id);
  
  List<IntegrationProfile> findByCustomerId(String customerId);
  
  List<IntegrationProfile> findByCustomerIdAndUserId(String customerId, String userId);
}
