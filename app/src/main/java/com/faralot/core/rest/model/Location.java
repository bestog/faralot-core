package com.faralot.core.rest.model;

import com.google.gson.annotations.SerializedName;
import com.faralot.core.rest.model.location.Events;
import com.faralot.core.rest.model.location.Geolocation;
import com.faralot.core.rest.model.location.Opening;
import com.faralot.core.rest.model.location.Prices;
import com.faralot.core.rest.model.location.Rating;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Location {

  public static final String LID = "lid";
  public static final String NAME = "name";
  public static final String CATEGORY = "category";
  public static final String DESCRIPTION = "description";
  public static final String GEOLOCATION = "geolocation";
  public static final String GEOLOCATION_COORD = "coord";
  public static final String GEOLOCATION_COORD_LATITUDE = "latitude";
  public static final String GEOLOCATION_COORD_LONGITUDE = "longitude";
  public static final String GEOLOCATION_ADDRESS = "address";
  public static final String GEOLOCATION_ADDRESS_STREET = "street";
  public static final String GEOLOCATION_ADDRESS_STREETNUMBER = "street_number";
  public static final String GEOLOCATION_ADDRESS_POSTALCODE = "postal_code";
  public static final String GEOLOCATION_ADDRESS_CITY = "city";
  public static final String GEOLOCATION_ADDRESS_COUNTRY = "country";
  public static final String SUITABLE = "suitable";
  public static final String SUITABLE_FROM = "from";
  public static final String SUITABLE_TO = "to";
  public static final String OPENING = "opening";
  public static final String OPENING_WEEKDAY = "weekday";
  public static final String OPENING_BEGIN = "begin";
  public static final String OPENING_END = "end";
  public static final String OPENING_24HOUR = "open";
  public static final String PRICES = "prices";
  public static final String PRICES_AGEGROUP = "agegroup";
  public static final String PRICES_AGEGROUP_FROM = "from";
  public static final String PRICES_AGEGROUP_TO = "to";
  public static final String PRICES_DESCRIPTION = "description";
  public static final String PRICES_PRICE = "price";
  public static final String PICTURES = "pictures";
  public static final String PICTURES_FILE = "file";
  public static final String PICTURES_URL = "url";
  public static final String PICTURES_TITLE = "title";
  public static final String PICTURES_FROM = "from";
  public static final String PICTURES_UPLOADED = "uploaded";
  public static final String PICTURES_VALID = "valid";
  public static final String FEATURES = "features";
  public static final String RATING = "rating";
  public static final String RATING_AVERAGE = "average";
  public static final String RATING_REVIEWS = "reviews";
  public static final String RATING_REVIEWS_RATE = "rate";
  public static final String RATING_REVIEWS_USER = "user";
  public static final String RATING_REVIEWS_PICTURE = "picture";
  public static final String RATING_REVIEWS_TEXT = "text";
  public static final String RATING_REVIEWS_CREATED = "created";
  public static final String EVENTS = "events";
  public static final String EVENTS_BEGIN = "begin";
  public static final String EVENTS_END = "end";
  public static final String EVENTS_TITLE = "title";
  public static final String EVENTS_DESCRIPTION = "description";
  public static final String EVENTS_PRICE = "price";
  public static final String EVENTS_SUITABLE = "suitable";
  public static final String EVENTS_SUITABLE_FROM = "from";
  public static final String EVENTS_SUITABLE_TO = "to";
  public static final String EVENTS_URL = "url";
  public static final String CREATED = "created";
  public static final String UPDATED = "updated";
  public static final String DISTANCE = "distance";
  @SerializedName(LID)
  protected String lid;
  @SerializedName(NAME)
  protected String name;
  @SerializedName(CATEGORY)
  protected String category;
  @SerializedName(DESCRIPTION)
  protected String description;
  @SerializedName(GEOLOCATION)
  protected Geolocation geolocation;
  @SerializedName(SUITABLE)
  protected Scope suitable;
  @SerializedName(OPENING)
  protected List<Opening> opening;
  @SerializedName(PRICES)
  protected List<Prices> prices;
  @SerializedName(PICTURES)
  protected List<Picture> pictures;
  @SerializedName(FEATURES)
  protected List<String> features;
  @SerializedName(RATING)
  protected Rating rating;
  @SerializedName(EVENTS)
  protected List<Events> events;
  @SerializedName(CREATED)
  protected String created;
  @SerializedName(UPDATED)
  protected String updated;
  @SerializedName(DISTANCE)
  protected int distance;

  public Location() { }

  public Location(String title, String desc) {
    name = title;
    description = desc;
  }

  public final String getLid() {
    return this.lid;
  }

  public final void setLid(String lid) {
    this.lid = lid;
  }

  public final String getName() {
    return this.name;
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final String getCategory() {
    return this.category;
  }

  public final void setCategory(String category) {
    this.category = category;
  }

  public final String getDescription() {
    return this.description;
  }

  public final void setDescription(String description) {
    this.description = description;
  }

  public final Geolocation getGeolocation() {
    return this.geolocation;
  }

  public final void setGeolocation(Geolocation geolocation) {
    this.geolocation = geolocation;
  }

  public final Scope getSuitable() {
    return this.suitable;
  }

  public final void setSuitable(Scope suitable) {
    this.suitable = suitable;
  }

  public final List<Opening> getOpening() {
    return this.opening;
  }

  public final void setOpening(List<Opening> opening) {
    this.opening = opening;
  }

  public final List<Prices> getPrices() {
    return this.prices;
  }

  public final void setPrices(List<Prices> prices) {
    this.prices = prices;
  }

  public final List<Picture> getPictures() {
    return this.pictures;
  }

  public final void setPictures(List<Picture> pictures) {
    this.pictures = pictures;
  }

  public final List<String> getFeatures() {
    return this.features;
  }

  public final void setFeatures(List<String> features) {
    this.features = features;
  }

  public final Rating getRating() {
    return this.rating;
  }

  public final void setRating(Rating rating) {
    this.rating = rating;
  }

  public final List<Events> getEvents() {
    return this.events;
  }

  public final void setEvents(List<Events> events) {
    this.events = events;
  }

  public final String getCreated() {
    return this.created;
  }

  public final void setCreated(String created) {
    this.created = created;
  }

  public final String getUpdated() {
    return this.updated;
  }

  public final void setUpdated(String updated) {
    this.updated = updated;
  }

  public final int getDistance() {
    return this.distance;
  }

  public final void setDistance(int distance) {
    this.distance = distance;
  }

  @Override
  public final String toString() {
    return "Location{" +
        "lid='" + this.lid + '\'' +
        ", name='" + this.name + '\'' +
        ", category='" + this.category + '\'' +
        ", description='" + this.description + '\'' +
        ", geolocation=" + this.geolocation +
        ", suitable=" + this.suitable +
        ", opening=" + this.opening +
        ", prices=" + this.prices +
        ", pictures=" + this.pictures +
        ", features=" + this.features +
        ", rating=" + this.rating +
        ", events=" + this.events +
        ", created='" + this.created + '\'' +
        ", updated='" + this.updated + '\'' +
        ", distance=" + this.distance +
        '}';
  }
}
