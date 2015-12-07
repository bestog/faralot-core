package com.faralot.core.rest.model.user;

import com.faralot.core.rest.model.User;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Visits {

  @SerializedName(User.VISITS_TIMES)
  protected List<String> times;
  @SerializedName(User.VISITS_LOCATION)
  protected String location;
  @SerializedName(User.VISITS_PICTURE)
  protected String picture;
  @SerializedName(User.VISITS_NAME)
  protected String name;

  public Visits() {}

  public final List<String> getTimes() {
    return this.times;
  }

  public final void setTimes(List<String> times) {
    this.times = times;
  }

  public final String getLocation() {
    return this.location;
  }

  public final void setLocation(String location) {
    this.location = location;
  }

  public String getPicture() {
    return this.picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
