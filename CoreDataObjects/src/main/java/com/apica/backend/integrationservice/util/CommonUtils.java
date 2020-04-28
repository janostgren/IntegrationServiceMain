package com.apica.backend.integrationservice.util;

import java.net.URL;

public abstract class CommonUtils {

  
  public static boolean notEmpty(String str) {
    if (str != null && !str.trim().isEmpty()) {
      return true;
    } else {
      return false;
    }
  }
  
  public static boolean isEmpty(String str) {
    return !CommonUtils.notEmpty(str);
  }
  
  public static boolean isNull(Object obj) {
    if (obj != null ) {
      return false;
    } else {
      return true;
    }
  }
  
  public static boolean isURL(String url) 
  { 
      /* Try creating a valid URL */
      try { 
          new URL(url).toURI(); 
          return true; 
      } 
        
      // If there was an Exception 
      // while creating URL object 
      catch (Exception e) { 
          return false; 
      } 
  } 

}
