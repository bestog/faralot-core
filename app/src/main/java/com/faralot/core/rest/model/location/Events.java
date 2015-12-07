package com.faralot.core.rest.model.location;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.Scope;

import org.parceler.Parcel;

@Parcel
public class Events {

  @SerializedName(Location.EVENTS_BEGIN)
  protected String begin;
  @SerializedName(Location.EVENTS_END)
  protected String end;
  @SerializedName(Location.EVENTS_TITLE)
  protected String title;
  @SerializedName(Location.EVENTS_DESCRIPTION)
  protected String description;
  @SerializedName(Location.EVENTS_PRICE)
  protected String price;
  @SerializedName(Location.EVENTS_SUITABLE)
  protected Scope suitable;
  @SerializedName(Location.EVENTS_URL)
  protected String url;

  public Events() {}

  public final String getBegin() {
    return this.begin;
  }

  public final void setBegin(String begin) {
    this.begin = begin;
  }

  public final String getEnd() {
    return this.end;
  }

  public final void setEnd(String end) {
    this.end = end;
  }

  public final String getTitle() {
    return this.title;
  }

  public final void setTitle(String title) {
    this.title = title;
  }

  public final String getDescription() {
    return this.description;
  }

  public final void setDescription(String description) {
    this.description = description;
  }

  public final String getPrice() {
    return this.price;
  }

  public final void setPrice(String price) {
    this.price = price;
  }

  public final Scope getSuitable() {
    return this.suitable;
  }

  public final void setSuitable(Scope suitable) {
    this.suitable = suitable;
  }

  public final String getUrl() {
    return this.url;
  }

  public final void setUrl(String url) {
    this.url = url;
  }

  @Override
  public final String toString() {
    return "Events{" +
        "begin='" + this.begin + '\'' +
        ", end='" + this.end + '\'' +
        ", title='" + this.title + '\'' +
        ", description='" + this.description + '\'' +
        ", price='" + this.price + '\'' +
        ", suitable=" + this.suitable +
        ", url='" + this.url + '\'' +
        '}';
  }
}
