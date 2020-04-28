package com.apica.backend.integrationservice.adapter.dto.instana;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InstanaRequestFilter implements Serializable {
  private TimeFrame timeFrame;
  private List<TagFilter> tagFilters = new ArrayList<TagFilter>();
  private final static long serialVersionUID = 7708216543145640583L;
  
  /**
   * No args constructor for use in serialization
   *
   */
  public InstanaRequestFilter() {}
  
  /**
   *
   * @param tagFilters
   * @param timeFrame
   */
  public InstanaRequestFilter(TimeFrame timeFrame, List<TagFilter> tagFilters) {
    super();
    this.timeFrame = timeFrame;
    this.tagFilters = tagFilters;
  }
  
  public TimeFrame getTimeFrame() {
    return timeFrame;
  }
  
  public void setTimeFrame(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
  }
  
  public InstanaRequestFilter withTimeFrame(TimeFrame timeFrame) {
    this.timeFrame = timeFrame;
    return this;
  }
  
  public List<TagFilter> getTagFilters() {
    return tagFilters;
  }
  
  public void setTagFilters(List<TagFilter> tagFilters) {
    this.tagFilters = tagFilters;
  }
  
  public InstanaRequestFilter withTagFilters(List<TagFilter> tagFilters) {
    this.tagFilters = tagFilters;
    return this;
  }
}
