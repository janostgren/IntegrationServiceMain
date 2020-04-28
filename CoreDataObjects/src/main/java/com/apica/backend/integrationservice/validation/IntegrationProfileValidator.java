package com.apica.backend.integrationservice.validation;

import com.apica.backend.integrationservice.exception.InvalidReqestException;
import com.apica.backend.integrationservice.model.IntegrationProfile;
import com.apica.backend.integrationservice.util.CommonUtils;

public class IntegrationProfileValidator {
  private IntegrationProfileValidator() {}
  
  public static boolean validateIntegrationProfile(IntegrationProfile profile, boolean complete,
      boolean idPresent) {
    String missingTemplate = "Invalid Integration profile. Property[%s] is missing or not valid";
    if (CommonUtils.isNull(profile)) {
      throw new InvalidReqestException("Invalid Integration Profile [null] ");
    }
    if (CommonUtils.isEmpty(profile.getPlatformId())) {
      throw new InvalidReqestException(String.format(missingTemplate, "platformId"));
    }
    if (CommonUtils.isEmpty(profile.getUrl())) {
      throw new InvalidReqestException(String.format(missingTemplate, "url"));
    }
    if (!CommonUtils.isURL(profile.getUrl())) {
      throw new InvalidReqestException(String.format(missingTemplate, "url"));
    }
    if (CommonUtils.isEmpty(profile.getAccount())) {
      throw new InvalidReqestException(String.format(missingTemplate, "account"));
    }
    if (CommonUtils.isNull(profile.getAuthentication())
        || CommonUtils.isNull(profile.getAuthentication().getProperties())
        || profile.getAuthentication().getProperties().isEmpty()) {
      throw new InvalidReqestException(String.format(missingTemplate, "authentication"));
    }
    if (complete) {
      boolean idok = (CommonUtils.notEmpty(profile.getId()) && idPresent)
          || (CommonUtils.isEmpty(profile.getId()) && !idPresent);
      if (!idok)
        throw new InvalidReqestException(String.format(
            "Internal Id should %s exist in IntegrationProfile object", idPresent ? "" : "not"));
      if (CommonUtils.isEmpty(profile.getCustomerId())) {
        throw new InvalidReqestException(String.format(missingTemplate, "customerId"));
      }
      if (CommonUtils.isEmpty(profile.getUserId())) {
        throw new InvalidReqestException(String.format(missingTemplate, "userId"));
      }
      if (CommonUtils.isEmpty(profile.getName())) {
        throw new InvalidReqestException(String.format(missingTemplate, "name"));
      }
      if (CommonUtils.isEmpty(profile.getApplication())) {
        throw new InvalidReqestException(String.format(missingTemplate, "application"));
      }
      if (CommonUtils.isEmpty(profile.getApplicationId())) {
        throw new InvalidReqestException(String.format(missingTemplate, "applicationId"));
      }
    }
    return true;
  }
}
