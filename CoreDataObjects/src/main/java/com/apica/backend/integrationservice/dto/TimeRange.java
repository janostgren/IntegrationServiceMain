package com.apica.backend.integrationservice.dto;

import java.io.Serializable;
import javax.validation.constraints.Min;


public class TimeRange implements Serializable {
  private static final long serialVersionUID = -1L;
  long toTime;
  @Min(value = 1, message = "Duration must be 1 minute of more")
  int durationInMinutes;
  
  public TimeRange() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  public TimeRange(long toTime, int durationInMinutes) {
    this();
    this.toTime = toTime;
    this.durationInMinutes = durationInMinutes;
  }
  
  /**
   * @return the toTime
   */
  public long getToTime() {
    return toTime;
  }
  
  /**
   * @param toTime the toTime to set
   */
  public void setToTime(long toTime) {
    this.toTime = toTime;
  }
  
  /**
   * @return the durationInMinutes
   */
  public int getDurationInMinutes() {
    return durationInMinutes;
  }
  
  /**
   * @param durationInMinutes the durationInMinutes to set
   */
  public void setDurationInMinutes(int durationInMinutes) {
    this.durationInMinutes = durationInMinutes;
  }
}
