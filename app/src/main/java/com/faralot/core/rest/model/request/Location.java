package com.faralot.core.rest.model.request;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Scope;
import com.faralot.core.rest.model.location.Geolocation;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Location {

  @SerializedName(com.faralot.core.rest.model.Location.NAME)
  protected String name;
  @SerializedName(com.faralot.core.rest.model.Location.CATEGORY)
  protected String category;
  @SerializedName(com.faralot.core.rest.model.Location.DESCRIPTION)
  protected String description;
  @SerializedName(com.faralot.core.rest.model.Location.SUITABLE)
  protected Scope suitable;
  @SerializedName(com.faralot.core.rest.model.Location.GEOLOCATION)
  protected Geolocation geolocation;
  @SerializedName(com.faralot.core.rest.model.Location.FEATURES)
  protected List<String> features;

  public Location() {
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Geolocation getGeolocation() {
    return this.geolocation;
  }

  public void setGeolocation(Geolocation geolocation) {
    this.geolocation = geolocation;
  }

  public Scope getSuitable() {
    return this.suitable;
  }

  public void setSuitable(Scope suitable) {
    this.suitable = suitable;
  }

  public List<String> getFeatures() {
    return this.features;
  }

  public void setFeatures(List<String> features) {
    this.features = features;
  }

  @Override
  public String toString() {
    return "Location{" +
        "name='" + this.name + '\'' +
        ", category='" + this.category + '\'' +
        ", description='" + this.description + '\'' +
        ", suitable=" + this.suitable +
        ", geolocation=" + this.geolocation +
        ", features=" + this.features +
        '}';
  }
}