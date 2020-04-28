package com.apica.backend.integrationservice.repository;


import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.apica.backend.integrationservice.model.IntegrationProfile;


public interface IntegrationProfileRepository extends MongoRepository<IntegrationProfile, String> {

  List <IntegrationProfile> findByCustomerId(String customerId);
  List <IntegrationProfile> findByCustomerIdAndUserId(String customerId,String userId);
}
