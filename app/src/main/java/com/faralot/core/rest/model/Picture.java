package com.faralot.core.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Picture {

  public static final String PICTURE_FILE = "file";
  public static final String PICTURE_URL = "url";
  public static final String PICTURE_TITLE = "title";
  public static final String PICTURE_FROM = "from";
  public static final String PICTURE_UPLOADED = "uploaded";
  public static final String PICTURE_VALID = "valid";
  @SerializedName(Picture.PICTURE_URL)
  protected String url;
  @SerializedName(Picture.PICTURE_TITLE)
  protected String title;
  @SerializedName(Picture.PICTURE_FILE)
  protected String file;
  @SerializedName(Picture.PICTURE_FROM)
  protected String from;
  @SerializedName(Picture.PICTURE_UPLOADED)
  protected String uploaded;
  @SerializedName(Picture.PICTURE_VALID)
  protected boolean valid;

  public Picture() {}

  public final String getUrl() {
    return this.url;
  }

  public final void setUrl(String url) {
    this.url = url;
  }

  public final String getTitle() {
    return this.title;
  }

  public final void setTitle(String title) {
    this.title = title;
  }

  public final String getFile() {
    return this.file;
  }

  public final void setFile(String file) {
    this.file = file;
  }

  public final String getFrom() {
    return this.from;
  }

  public final void setFrom(String from) {
    this.from = from;
  }

  public final String getUploaded() {
    return this.uploaded;
  }

  public final void setUploaded(String uploaded) {
    this.uploaded = uploaded;
  }

  public final boolean isValid() {
    return this.valid;
  }

  public final void setValid(boolean valid) {
    this.valid = valid;
  }

  @Override
  public final String toString() {
    return "Picture{" +
        "url='" + this.url + '\'' +
        ", title='" + this.title + '\'' +
        ", file='" + this.file + '\'' +
        ", from='" + this.from + '\'' +
        ", uploaded='" + this.uploaded + '\'' +
        ", valid=" + this.valid +
        '}';
  }
}
