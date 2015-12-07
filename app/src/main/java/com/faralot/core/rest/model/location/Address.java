package com.faralot.core.rest.model.location;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Location;

import org.parceler.Parcel;

@Parcel
public class Address {

  @SerializedName(Location.GEOLOCATION_ADDRESS_STREET)
  protected String street;
  @SerializedName(Location.GEOLOCATION_ADDRESS_STREETNUMBER)
  protected String streetNumber;
  @SerializedName(Location.GEOLOCATION_ADDRESS_POSTALCODE)
  protected String postalcode;
  @SerializedName(Location.GEOLOCATION_ADDRESS_CITY)
  protected String city;
  @SerializedName(Location.GEOLOCATION_ADDRESS_COUNTRY)
  protected String country;

  public Address() {}

  public final String getStreet() {
    return this.street;
  }

  public final void setStreet(String street) {
    this.street = street;
  }

  public final String getStreetNumber() {
    return this.streetNumber;
  }

  public final void setStreetNumber(String streetNumber) {
    this.streetNumber = streetNumber;
  }

  public final String getPostalcode() {
    return this.postalcode;
  }

  public final void setPostalcode(String postalcode) {
    this.postalcode = postalcode;
  }

  public final String getCity() {
    return this.city;
  }

  public final void setCity(String city) {
    this.city = city;
  }

  public final String getCountry() {
    return this.country;
  }

  public final void setCountry(String country) {
    this.country = country;
  }

  @Override
  public final String toString() {
    return "Address{" +
        "street='" + this.street + '\'' +
        ", streetNumber='" + this.streetNumber + '\'' +
        ", postalcode='" + this.postalcode + '\'' +
        ", city='" + this.city + '\'' +
        ", country='" + this.country + '\'' +
        '}';
  }
}
