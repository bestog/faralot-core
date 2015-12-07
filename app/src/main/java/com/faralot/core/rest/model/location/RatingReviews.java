package com.faralot.core.rest.model.location;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Location;

import org.parceler.Parcel;

@Parcel
public class RatingReviews {

  @SerializedName(Location.RATING_REVIEWS_RATE)
  protected int rate;
  @SerializedName(Location.RATING_REVIEWS_USER)
  protected String user;
  @SerializedName(Location.RATING_REVIEWS_PICTURE)
  protected String picture;
  @SerializedName(Location.RATING_REVIEWS_TEXT)
  protected String text;
  @SerializedName(Location.RATING_REVIEWS_CREATED)
  protected String created;

  public RatingReviews() {
  }

  public RatingReviews(int rate, String text) {
    this.rate = rate;
    this.text = text;
  }

  public final int getRate() {
    return this.rate;
  }

  public final void setRate(int rate) {
    this.rate = rate;
  }

  public final String getUser() {
    return this.user;
  }

  public final void setUser(String user) {
    this.user = user;
  }

  public final String getPicture() {
    return this.picture;
  }

  public final void setPicture(String picture) {
    this.picture = picture;
  }

  public final String getText() {
    return this.text;
  }

  public final void setText(String text) {
    this.text = text;
  }

  public final String getCreated() {
    return this.created;
  }

  public final void setCreated(String created) {
    this.created = created;
  }

  @Override
  public final String toString() {
    return "RatingReviews{" +
        "rate=" + this.rate +
        ", user='" + this.user + '\'' +
        ", picture='" + this.picture + '\'' +
        ", text='" + this.text + '\'' +
        ", created='" + this.created + '\'' +
        '}';
  }
}
