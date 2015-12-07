package com.faralot.core.rest.service;

import com.faralot.core.rest.model.Config;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Interface: Konfiguration Routen
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public interface UtilService {

  @GET("config")
  Call<Config> getConfig();
}
