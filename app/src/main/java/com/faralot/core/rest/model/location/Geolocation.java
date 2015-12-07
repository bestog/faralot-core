package com.faralot.core.rest.model.location;

import com.faralot.core.rest.model.Location;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Geolocation {

  @SerializedName(Location.GEOLOCATION_COORD)
  protected Coord coord;
  @SerializedName(Location.GEOLOCATION_ADDRESS)
  protected Address address;

  public Geolocation() { }

  public Geolocation(Coord coord) {
    this.coord = coord;
    address = null;
  }

  public final Coord getCoord() {
    return this.coord;
  }

  public final void setCoord(Coord coord) {
    this.coord = coord;
  }

  public final Address getAddress() {
    return this.address;
  }

  public final void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public final String toString() {
    return "Geolocation{" +
        "coord=" + this.coord +
        ", address=" + this.address +
        '}';
  }
}
