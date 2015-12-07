package com.faralot.core.rest;

import com.faralot.core.App;
import com.faralot.core.rest.service.LocationService;
import com.faralot.core.rest.service.UserService;
import com.faralot.core.rest.service.UtilService;
import com.faralot.core.rest.util.CustomInterceptor;
import com.faralot.core.rest.util.RestTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Retrofit.Builder;

/**
 * Client: Rest-Client (Retrofit) verbindet sich mit der REST-API
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class RestClient {

  public final LocationService location;
  public final UserService user;
  public final UtilService util;
  public static final String API_TOKEN = "x-api-token";
  public static final String API_USER = "x-api-user";
  public static final String API_KEY = "x-api-key";
  public static final String API_APP = "x-application-name";

  public RestClient() {
    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new RestTypeAdapterFactory()).create();
    OkHttpClient client = new OkHttpClient();
    client.interceptors().add(new CustomInterceptor());
    Retrofit restAdapter = new Builder().baseUrl(App.API_URL + App.API_VERSION + '/')
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build();
    this.location = restAdapter.create(LocationService.class);
    this.user = restAdapter.create(UserService.class);
    this.util = restAdapter.create(UtilService.class);
  }
}
