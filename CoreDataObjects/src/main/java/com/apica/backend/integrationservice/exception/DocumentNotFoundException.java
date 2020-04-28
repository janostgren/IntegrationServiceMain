package com.apica.backend.integrationservice.exception;

public abstract class DocumentNotFoundException extends RuntimeException {
    
    public DocumentNotFoundException(String m) {
      super(m);
  }
    
    

}