package com.faralot.core.rest.model.location;

import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.Scope;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Prices {

  @SerializedName(Location.PRICES_AGEGROUP)
  protected Scope agegroup;
  @SerializedName(Location.PRICES_DESCRIPTION)
  protected String description;
  @SerializedName(Location.PRICES_PRICE)
  protected float price;

  public Prices() {}

  public Scope getAgegroup() {
    return agegroup;
  }

  public void setAgegroup(Scope agegroup) {
    this.agegroup = agegroup;
  }

  public final String getDescription() {
    return this.description;
  }

  public final void setDescription(String description) {
    this.description = description;
  }

  public final float getPrice() {
    return this.price;
  }

  public final void setPrice(float price) {
    this.price = price;
  }

  @Override
  public final String toString() {
    return "Prices{" +
        "agegroup=" + this.agegroup +
        ", description='" + this.description + '\'' +
        ", price=" + this.price +
        '}';
  }
}
