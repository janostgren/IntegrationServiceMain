package com.apica.backend.integrationservice.dto;

import java.io.Serializable;

public class TraceRequestBody implements Serializable {
 
  private static final long serialVersionUID = 7754741401024207928L;
  private TraceFilter traceFilter;
  private TimeRange timeRange;
  /**
   * @return the traceFilter
   */
  public TraceFilter getTraceFilter() {
    return traceFilter;
  }
  /**
   * @param traceFilter the traceFilter to set
   */
  public void setTraceFilter(TraceFilter traceFilter) {
    this.traceFilter = traceFilter;
  }
  /**
   * @return the timeRange
   */
  public TimeRange getTimeRange() {
    return timeRange;
  }
  /**
   * @param timeRange the timeRange to set
   */
  public void setTimeRange(TimeRange timeRange) {
    this.timeRange = timeRange;
  }
  
  

}
