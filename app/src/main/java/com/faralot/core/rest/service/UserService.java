package com.faralot.core.rest.service;

import com.faralot.core.rest.RestClient;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.Picture;
import com.faralot.core.rest.model.User;
import com.faralot.core.rest.model.user.Visits;
import com.faralot.core.rest.model.vResponse;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Interface: Benutzer Routen
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public interface UserService {

  @GET("users/login")
  Call<vResponse> getLogin(@Header(RestClient.API_USER) String name,
      @Header(RestClient.API_KEY) String pwd);

  @FormUrlEncoded
  @POST("users")
  Call<vResponse> postRegister(@Field(User.NAME_USER) String user,
      @Field("password") String password, @Field(User.EMAIL) String email);

  @GET("me")
  Call<User> getMyProfile();

  @PUT("me")
  Call<vResponse> putProfile(@Body com.faralot.core.rest.model.request.User user);

  @GET("me/picture")
  Call<Picture> getPicture();

  @Multipart
  @POST("me/picture")
  Call<vResponse> postPicture(@Part("picture\"; filename=\"pp.jpg\" ") RequestBody picture);

  @DELETE("me/picture")
  Call<vResponse> deletePicture();

  @GET("me/favorites")
  Call<List<Location>> getFavorites();

  @GET("me/favorites/{lid}")
  Call<Location> getFavorite(@Path("lid") String lid);

  @FormUrlEncoded
  @POST("me/favorites")
  Call<vResponse> postFavorite(@Field("lid") String lid);

  @DELETE("me/favorites/{lid}")
  Call<vResponse> deleteFavorite(@Path("lid") String lid);

  @GET("me/visits")
  Call<List<Visits>> getVisits();

  @FormUrlEncoded
  @POST("me/visits")
  Call<vResponse> postVisit(@Field("lid") String lid);

  @DELETE("me")
  Call<vResponse> deleteAccount();

  @PUT("me/token")
  Call<vResponse> updateToken();

}
