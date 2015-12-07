package com.faralot.core.rest.model.request;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.user.Name;

import org.parceler.Parcel;

@Parcel
public class User {

  @SerializedName(com.faralot.core.rest.model.User.NAME)
  protected Name name;
  @SerializedName(com.faralot.core.rest.model.User.EMAIL)
  protected String email;
  @SerializedName("password")
  protected String password;

  public User() {}

  public Name getName() {
    return this.name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" +
        "name=" + this.name +
        ", email='" + this.email + '\'' +
        ", password='" + this.password + '\'' +
        '}';
  }
}