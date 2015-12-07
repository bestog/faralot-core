package com.faralot.core.rest.model;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.config.Location;
import com.faralot.core.rest.model.config.User;

import org.parceler.Parcel;

@Parcel
public class Config {

  @SerializedName("name")
  protected String name;
  @SerializedName("type")
  protected String type;
  @SerializedName("location")
  protected Location location;
  @SerializedName("user")
  protected User user;

  public Config() {
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
