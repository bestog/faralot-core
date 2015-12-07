package com.faralot.core.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class vResponse {

  @SerializedName("value")
  protected String value;

  public vResponse() {}

  public final String getValue() {
    return this.value;
  }

  public final void setValue(String value) {
    this.value = value;
  }

  @Override
  public final String toString() {
    return "vResponse{" +
        "value='" + this.value + '\'' +
        '}';
  }
}
