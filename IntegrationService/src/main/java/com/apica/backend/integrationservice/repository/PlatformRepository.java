package com.apica.backend.integrationservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.apica.backend.integrationservice.model.Platform;

public interface PlatformRepository extends MongoRepository<Platform, String> {

  
 

}
