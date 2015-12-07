package com.faralot.core.rest.model;

import com.faralot.core.rest.model.user.Favorites;
import com.faralot.core.rest.model.user.Name;
import com.faralot.core.rest.model.user.Visits;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class User {

  public static final String UID = "uid";
  public static final String NAME = "name";
  public static final String NAME_FIRST = "first";
  public static final String NAME_LAST = "last";
  public static final String NAME_USER = "user";
  public static final String TOKEN = "token";
  public static final String EMAIL = "email";
  public static final String POINTS = "points";
  public static final String PICTURE = "picture";
  public static final String FAVORITES = "favorites";
  public static final String FAVORITES_LOCATION = "lid";
  public static final String FAVORITES_STARRED = "starred";
  public static final String VISITS = "visits";
  public static final String VISITS_LOCATION = "lid";
  public static final String VISITS_NAME = "name";
  public static final String VISITS_PICTURE = "picture";
  public static final String VISITS_TIMES = "times";
  public static final String LASTREQUEST = "last_request";
  public static final String REGISTERED = "registered";
  @SerializedName(UID)
  protected String uid;
  @SerializedName(NAME)
  protected Name name;
  @SerializedName(TOKEN)
  protected String token;
  @SerializedName(EMAIL)
  protected String email;
  @SerializedName(POINTS)
  protected int points;
  @SerializedName(PICTURE)
  protected Picture picture;
  @SerializedName(FAVORITES)
  protected List<Favorites> favorites;
  @SerializedName(VISITS)
  protected List<Visits> visits;
  @SerializedName(LASTREQUEST)
  protected String lastRequest;
  @SerializedName(REGISTERED)
  protected String registered;

  public User() { }

  public final String getUid() {
    return this.uid;
  }

  public final void setUid(String uid) {
    this.uid = uid;
  }

  public final Name getName() {
    return this.name;
  }

  public final void setName(Name name) {
    this.name = name;
  }

  public final String getToken() {
    return this.token;
  }

  public final void setToken(String token) {
    this.token = token;
  }

  public final String getEmail() {
    return this.email;
  }

  public final void setEmail(String email) {
    this.email = email;
  }

  public final int getPoints() {
    return this.points;
  }

  public final void setPoints(int points) {
    this.points = points;
  }

  public final Picture getPicture() {
    return this.picture;
  }

  public final void setPicture(Picture picture) {
    this.picture = picture;
  }

  public final List<Favorites> getFavorites() {
    return this.favorites;
  }

  public final void setFavorites(List<Favorites> favorites) {
    this.favorites = favorites;
  }

  public final List<Visits> getVisits() {
    return this.visits;
  }

  public final void setVisits(List<Visits> visits) {
    this.visits = visits;
  }

  public final String getLastRequest() {
    return this.lastRequest;
  }

  public final void setLastRequest(String lastRequest) {
    this.lastRequest = lastRequest;
  }

  public final String getRegistered() {
    return this.registered;
  }

  public final void setRegistered(String registered) {
    this.registered = registered;
  }

  @Override
  public final String toString() {
    return "User{" +
        "uid='" + this.uid + '\'' +
        ", name=" + this.name +
        ", token='" + this.token + '\'' +
        ", email='" + this.email + '\'' +
        ", points=" + this.points +
        ", picture=" + this.picture +
        ", favorites=" + this.favorites +
        ", visits=" + this.visits +
        ", lastRequest='" + this.lastRequest + '\'' +
        ", registered='" + this.registered + '\'' +
        '}';
  }
}
