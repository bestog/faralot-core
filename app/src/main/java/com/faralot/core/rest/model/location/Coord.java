package com.faralot.core.rest.model.location;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Location;

import org.parceler.Parcel;

@Parcel
public class Coord {

  @SerializedName(Location.GEOLOCATION_COORD_LATITUDE)
  protected float latitude;
  @SerializedName(Location.GEOLOCATION_COORD_LONGITUDE)
  protected float longitude;

  public Coord() { }

  public Coord(float lat, float lon) {
    latitude = lat;
    longitude = lon;
  }

  public final float getLatitude() {
    return this.latitude;
  }

  public final void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  public final float getLongitude() {
    return this.longitude;
  }

  public final void setLongitude(float longitude) {
    this.longitude = longitude;
  }

  @Override
  public final String toString() {
    return "Coord{" +
        "latitude=" + this.latitude +
        ", longitude=" + this.longitude +
        '}';
  }
}
