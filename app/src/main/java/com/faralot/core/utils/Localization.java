package com.faralot.core.utils;

/**
 * Klasse: Moeglichkeiten um Koordinaten zu erhalten (fuer Auswahl)
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class Localization {

  public static final String GPS = "gps";
  public static final String PALS = "pals";
  public static final String PIC_COORD = "pic-coord";
  public static final String MANUAL = "manual";
  private int icon;
  private String title;
  private String id;

  public Localization() {
  }

  public Localization(int icon, String title, String id) {
    this.icon = icon;
    this.title = title;
    this.id = id;
  }

  public int getIcon() {
    return this.icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
