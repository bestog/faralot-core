package com.faralot.core.rest.model.location;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Location;

import org.parceler.Parcel;

@Parcel
public class Opening {

  @SerializedName(Location.OPENING_WEEKDAY)
  protected int weekday;
  @SerializedName(Location.OPENING_BEGIN)
  protected String begin;
  @SerializedName(Location.OPENING_END)
  protected String end;
  @SerializedName(Location.OPENING_24HOUR)
  protected boolean open;

  public Opening() {
  }

  public int getWeekday() {
    return this.weekday;
  }

  public void setWeekday(int weekday) {
    this.weekday = weekday;
  }

  public String getBegin() {
    return this.begin;
  }

  public void setBegin(String begin) {
    this.begin = begin;
  }

  public String getEnd() {
    return this.end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public boolean isOpen() {
    return this.open;
  }

  public void setOpen(boolean open) {
    this.open = open;
  }
}
