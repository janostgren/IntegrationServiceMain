package com.apica.backend.integrationservice.exception;

public class IntegrationProfileNotFoundException extends DocumentNotFoundException {

  public IntegrationProfileNotFoundException(String id) {
    super(String.format("IntegrationProfile with id %s not found",id));
  }
 
 

}
