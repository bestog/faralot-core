package com.faralot.core;

import android.app.Application;

import com.faralot.core.lib.LocalizationSystem;
import com.faralot.core.rest.RestClient;
import com.faralot.core.rest.model.Config;
import com.faralot.core.rest.model.User;

import java.util.List;

import io.paperdb.Paper;

/**
 * Applikation
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class App extends Application {
  /**
   * Konstanten
   */
  // REST-API
  public static String API_APP = "faralot";
  public static final String API_URL = "https://faralot.com/api/";
  public static final String API_VERSION = "v1";
  // GLOBAL
  public static final int PICTURE_QUALITY = 85;
  /**
   * Globale Variablen
   */
  // Values
  public static float latitude;
  public static float longitude;
  public static int radius;
  public static String filterFeatures = "";
  public static boolean isReady;
  // Objekte
  public static RestClient rest;
  public static Config config = new Config();
  public static User user;
  public static LocalizationSystem localization;
  public static List<String> fields;
  public static boolean palsActive;

  public App() {
  }

  @Override
  public final void onCreate() {
    super.onCreate();
    Paper.init(this);
  }

  public void setRest(String appName) {
    App.API_APP = appName;
    App.rest = new RestClient();
  }

}
