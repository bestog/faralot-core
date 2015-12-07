package com.faralot.core.rest.model.config;

import com.faralot.core.rest.model.Scope;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Location {

  @SerializedName("max_radius")
  protected int maxRadius;
  @SerializedName("coord_precision")
  protected int coordPrecision;
  @SerializedName("picture_size")
  protected int pictureSize;
  @SerializedName("rate")
  protected Scope rate;
  @SerializedName("categories")
  protected List<String> categories;
  @SerializedName("features")
  protected List<String> features;
  @SerializedName("fields")
  protected List<String> fields;

  public Location() {}

  public int getMaxRadius() {
    return this.maxRadius;
  }

  public void setMaxRadius(int maxRadius) {
    this.maxRadius = maxRadius;
  }

  public int getCoordPrecision() {
    return this.coordPrecision;
  }

  public void setCoordPrecision(int coordPrecision) {
    this.coordPrecision = coordPrecision;
  }

  public int getPictureSize() {
    return this.pictureSize;
  }

  public void setPictureSize(int pictureSize) {
    this.pictureSize = pictureSize;
  }

  public Scope getRate() {
    return this.rate;
  }

  public void setRate(Scope rate) {
    this.rate = rate;
  }

  public List<String> getCategories() {
    return this.categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public List<String> getFeatures() {
    return this.features;
  }

  public void setFeatures(List<String> features) {
    this.features = features;
  }

  public List<String> getFields() {
    return this.fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }
}
