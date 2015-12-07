package com.faralot.core.lib;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.faralot.core.App;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.utils.Util;

import java.util.Calendar;

/**
 * Localization System for your position
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class LocalizationSystem {

  /**
   * LocationSystems
   */
  public static final String GPS = "GPS";
  public static final String PALS = "PALS";
  /**
   * Variables
   */
  private final Activity activity;
  private Listener listener;

  /**
   * constructor
   *
   * @param activity Activity
   */
  public LocalizationSystem(Activity activity) {
    this.activity = activity;
  }

  /**
   * Get coordinates
   *
   * @param listener Listener
   */
  public final void getCoords(Listener listener) {
    this.getCoords(this.activity.getSharedPreferences("settings", Context.MODE_PRIVATE)
        .getString("localization", LocalizationSystem.GPS), listener);
  }

  /**
   * Get coordinates and choose system
   *
   * @param use Which system
   * @param listener Listener
   */
  public final void getCoords(String use, Listener listener) {
    this.listener = listener;
    switch (use) {
      case LocalizationSystem.GPS:
        this.getFromGps();
        break;
      case LocalizationSystem.PALS:
        if (App.palsActive) {
          this.getFromPals();
        } else {
          this.getFromGps();
        }
        break;
      default:
        this.getFromGps();
        break;
    }
  }

  /**
   * Get coordinates from pals system
   */
  private void getFromPals() {
    if (App.palsActive) {
      final Intent serviceIntent = new Intent("com.bestog.pals.Service");
      final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { }

        @Override
        public void onServiceDisconnected(ComponentName name) { }
      };
      BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          Bundle bundle = intent.getExtras();
          if (bundle != null) {
            LocalizationSystem.this.listener.onComplete(new Coord(bundle.getFloat("lat"), bundle.getFloat("lon")), true);
          } else {
            LocalizationSystem.this.listener.onComplete(new Coord(0, 0), false);
          }
          LocalizationSystem.this.activity.unregisterReceiver(this);
          LocalizationSystem.this.activity.stopService(serviceIntent);
          LocalizationSystem.this.activity.unbindService(serviceConnection);
        }
      };
      this.activity.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
      this.activity.registerReceiver(broadcastReceiver, new IntentFilter("com.bestog.pals.receiver"));
    } else {
      Toast.makeText(this.activity, "PALS ist nicht installiert.", Toast.LENGTH_LONG).show();
    }
  }

  /**
   * Get coordinates from GPS system
   */
  private void getFromGps() {
    final LocationManager locationManager = (LocationManager) this.activity.getSystemService(Context.LOCATION_SERVICE);
    Location location = null;
    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      try {
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
      } catch (SecurityException e) {
        //@todo: better logging
        complete(new Coord(0, 0), false);
        e.printStackTrace();
      }
      if (location != null
          && location.getTime() > Calendar.getInstance().getTimeInMillis() - (long) (2 * 60
          * 1000)) {
        float lat = Util.round(location.getLatitude(), App.config.getLocation()
            .getCoordPrecision());
        float lon = Util.round(location.getLongitude(), App.config.getLocation()
            .getCoordPrecision());
        complete(new Coord(lat, lon), lat != 0 && lon != 0);
      } else {
        try {
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0F,
              new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                  if (location != null) {
                    LocalizationSystem.this.getFromGps();
                    try {
                      locationManager.removeUpdates(this);
                    } catch (SecurityException e) {
                      e.printStackTrace();
                      // @todo better logging
                    }
                  }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }

                @Override
                public void onProviderEnabled(String provider) { }

                @Override
                public void onProviderDisabled(String provider) { }
              });
        } catch (SecurityException e) {
          complete(new Coord(0, 0), false);
          e.printStackTrace();
        }
      }
    } else {
      Builder builder = new Builder(this.activity).setMessage(this.activity.getString(string.gps_enable_question))
          .setCancelable(false)
          .setPositiveButton(this.activity.getString(string.yes), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              LocalizationSystem.this.activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
          })
          .setNegativeButton(this.activity.getString(string.no), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              LocalizationSystem.this.complete(new Coord(0, 0), false);
            }
          });
      builder.create().show();
    }
  }

  /**
   * if complete
   *
   * @param coord Coordinates
   * @param available valid coordinates?
   */
  private void complete(Coord coord, boolean available) {
    if (this.listener != null) {
      this.listener.onComplete(coord, available);
    }
  }

  /**
   * Interface Listener
   */
  public interface Listener {
    void onComplete(Coord coord, boolean valid);
  }
}


