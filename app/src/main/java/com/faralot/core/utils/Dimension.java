package com.faralot.core.utils;

/**
 * Klasse: Höhe und Breite als Objekt zusammengefasst (2D)
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class Dimension {

  private int width;
  private int height;

  public Dimension() {}

  public Dimension(int w, int h) {
    width = w;
    height = h;
  }

  public final int getWidth() {
    return this.width;
  }

  public final void setWidth(int width) {
    this.width = width;
  }

  public final int getHeight() {
    return this.height;
  }

  public final void setHeight(int height) {
    this.height = height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Dimension dimension = (Dimension) o;
    return this.width == dimension.width && this.height == dimension.height;
  }

  @Override
  public int hashCode() {
    int result = this.width;
    result = 31 * result + this.height;
    return result;
  }

  @Override
  public String toString() {
    return "Dimension{" +
        "width=" + this.width +
        ", height=" + this.height +
        '}';
  }
}
