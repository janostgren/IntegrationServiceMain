package com.apica.backend.integrationservice.adapter.dto.instana;

import java.io.Serializable;

public class TimeFrame implements Serializable {

  private long windowSize;
  private long to;
  private final static long serialVersionUID = -8980303768424395203L;

  /**
   * No args constructor for use in serialization
   *
   */
  public TimeFrame() {}

  /**
   *
   * @param windowSize
   * @param to
   */
  public TimeFrame(long windowSize, long to) {
    super();
    this.windowSize = windowSize;
    this.to = to;
  }

  public long getWindowSize() {
    return windowSize;
  }

  public void setWindowSize(long windowSize) {
    this.windowSize = windowSize;
  }

  public TimeFrame withWindowSize(long windowSize) {
    this.windowSize = windowSize;
    return this;
  }

  public long getTo() {
    return to;
  }

  public void setTo(long to) {
    this.to = to;
  }

  public TimeFrame withTo(long to) {
    this.to = to;
    return this;
  }

}
