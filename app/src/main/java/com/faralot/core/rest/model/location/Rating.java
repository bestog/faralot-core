package com.faralot.core.rest.model.location;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Location;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Rating {

  @SerializedName(Location.RATING_AVERAGE)
  protected float average;
  @SerializedName(Location.RATING_REVIEWS)
  protected List<RatingReviews> reviews;

  public Rating() {}

  public final List<RatingReviews> getReviews() {
    return this.reviews;
  }

  public final void setReviews(List<RatingReviews> reviews) {
    this.reviews = reviews;
  }

  public final float getAverage() {
    return this.average;
  }

  public final void setAverage(float average) {
    this.average = average;
  }

  @Override
  public final String toString() {
    return "Rating{" +
        "average=" + this.average +
        ", reviews=" + this.reviews +
        '}';
  }
}
