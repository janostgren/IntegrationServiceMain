package com.apica.backend.integrationservice.exception;

public class PlatformNotFoundException extends DocumentNotFoundException {

  public PlatformNotFoundException(String id) {
    super(String.format("Platform with id %s not found",id));
    // TODO Auto-generated constructor stub
  }
 
  
   

}
