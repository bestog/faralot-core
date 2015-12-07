package com.faralot.core.rest.model.config;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class User {
  @SerializedName("picture_size")
  protected int pictureSize;

  public int getPictureSize() {
    return this.pictureSize;
  }

  public void setPictureSize(int pictureSize) {
    this.pictureSize = pictureSize;
  }
}
