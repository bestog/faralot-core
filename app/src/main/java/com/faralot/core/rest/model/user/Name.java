package com.faralot.core.rest.model.user;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.User;

import org.parceler.Parcel;

@Parcel
public class Name {

  @SerializedName(User.NAME_FIRST)
  protected String first;
  @SerializedName(User.NAME_LAST)
  protected String last;
  @SerializedName(User.NAME_USER)
  protected String user;

  public Name() {
  }

  public Name(String first, String last, String user) {
    this.first = first;
    this.last = last;
    this.user = user;
  }

  public final String getFirst() {
    return this.first;
  }

  public final void setFirst(String first) {
    this.first = first;
  }

  public final String getLast() {
    return this.last;
  }

  public final void setLast(String last) {
    this.last = last;
  }

  public final String getUser() {
    return this.user;
  }

  public final void setUser(String user) {
    this.user = user;
  }

  @Override
  public final String toString() {
    return "Name{" +
        "first='" + this.first + '\'' +
        ", last='" + this.last + '\'' +
        ", user='" + this.user + '\'' +
        '}';
  }
}
