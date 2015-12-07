package com.faralot.core.rest.model.user;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.User;

import org.parceler.Parcel;

@Parcel
public class Favorites {

  @SerializedName(User.FAVORITES_LOCATION)
  protected String lid;
  @SerializedName(User.FAVORITES_STARRED)
  protected String starred;

  public Favorites() {}

  public final String getLid() {
    return this.lid;
  }

  public final void setLid(String lid) {
    this.lid = lid;
  }

  public final String getStarred() {
    return this.starred;
  }

  public final void setStarred(String starred) {
    this.starred = starred;
  }

  @Override
  public final String toString() {
    return "Favorites{" +
        "lid='" + this.lid + '\'' +
        ", starred='" + this.starred + '\'' +
        '}';
  }
}
