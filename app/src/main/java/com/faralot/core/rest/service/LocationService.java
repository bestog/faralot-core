package com.faralot.core.rest.service;

import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.rest.model.location.Opening;
import com.faralot.core.rest.model.location.Prices;
import com.faralot.core.rest.model.location.Rating;
import com.faralot.core.rest.model.vResponse;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Interface: Location Routen
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public interface LocationService {

  @GET("locations/{lid}")
  Call<Location> getLocation(@Path(Location.LID) String id);

  @GET("locations/near")
  Call<List<Location>> getNearLocations(@Query("query") String query, @Query("radius") int radius);

  @GET("locations/near")
  Call<List<Location>> getNearLocations(@Query(Location.GEOLOCATION_COORD_LATITUDE) float lat,
      @Query(Location.GEOLOCATION_COORD_LONGITUDE) float lon,
      @Query("radius") int radius, @Query("filter[features]") String features);

  @POST("locations")
  Call<vResponse> postLocation(@Body com.faralot.core.rest.model.request.Location location);

  @PUT("locations/{lid}")
  Call<Location> updateLocation(@Path(Location.LID) String id,
      @Body com.faralot.core.rest.model.request.Location location);

  @Multipart
  @POST("locations/{lid}/pictures")
  Call<vResponse> postPicture(
      @Path(Location.LID) String lid, @Part("pictures\"; filename=\"pp.jpg\" ") RequestBody file,
      @Part(Location.PICTURES_TITLE) String title);

  @DELETE("locations/{lid}/pictures")
  Call<vResponse> deletePicture(@Path(Location.LID) String lid,
      @Query(Location.PICTURES_FILE) String file);

  @GET("locations/{lid}/rating")
  Call<Rating> getReviews(@Path(Location.LID) String lid);

  @FormUrlEncoded
  @POST("locations/{lid}/rating")
  Call<vResponse> postReview(
      @Path(Location.LID) String lid, @Field(Location.RATING_REVIEWS_RATE) int rate,
      @Field(Location.RATING_REVIEWS_TEXT) String text);

  @POST("locations/{lid}/opening")
  Call<vResponse> postOpening(@Path(Location.LID) String lid, @Body Opening opening);

  @DELETE("locations/{lid}/opening")
  Call<vResponse> deleteOpening(
      @Path(Location.LID) String lid, @Query(Location.OPENING_WEEKDAY) int day);

  @POST("locations/{lid}/prices")
  Call<vResponse> postPrices(@Path(Location.LID) String lid, @Body Prices prices);

  @DELETE("locations/{lid}/prices")
  Call<vResponse> deletePrices(@Path(Location.LID) String lid, @Query("pos") int pos);

  @Multipart
  @POST("locations/coord")
  Call<Coord> getLocationByExif(@Part("image\"; filename=\"pp.jpg\" ") RequestBody file);

  @FormUrlEncoded
  @POST("locations/{lid}/report")
  Call<vResponse> postReport(@Path(Location.LID) String lid, @Field("report") String text);
}
