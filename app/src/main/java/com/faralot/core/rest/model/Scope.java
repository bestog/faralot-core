package com.faralot.core.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Scope {

  @SerializedName("from")
  protected int from;
  @SerializedName("to")
  protected int to;

  public Scope() {}

  public Scope(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public final int getFrom() {
    return this.from;
  }

  public final void setFrom(int from) {
    this.from = from;
  }

  public final int getTo() {
    return this.to;
  }

  public final void setTo(int to) {
    this.to = to;
  }

  @Override
  public final String toString() {
    return "Scope{" +
        "from=" + this.from +
        ", to=" + this.to +
        '}';
  }
}
