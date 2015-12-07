package com.faralot.core.utils;

/**
 * Klasse: Verwendete Bibliotheken als Objekt
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class LibInfo {

  private String title;
  private String version;
  private String description;
  private String license;
  private String link;

  public LibInfo() {
  }

  public LibInfo(String title, String version, String description, String license, String link) {
    this.title = title;
    this.version = version;
    this.description = description;
    this.license = license;
    this.link = link;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLicense() {
    return this.license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getLink() {
    return this.link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override
  public String toString() {
    return "LibInfo{" +
        "title='" + this.title + '\'' +
        ", version='" + this.version + '\'' +
        ", description='" + this.description + '\'' +
        ", license='" + this.license + '\'' +
        ", link='" + this.link + '\'' +
        '}';
  }
}
