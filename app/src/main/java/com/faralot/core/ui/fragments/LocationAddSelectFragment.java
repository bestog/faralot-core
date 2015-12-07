package com.faralot.core.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.drawable;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.lib.LocalizationSystem;
import com.faralot.core.lib.LocalizationSystem.Listener;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.ui.holder.LocalizationHolder;
import com.faralot.core.utils.Dimension;
import com.faralot.core.utils.Localization;
import com.faralot.core.utils.Util;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationAddSelectFragment extends Fragment {

  protected MapView mapView;
  protected TextView latitude;
  protected ProgressBar latitudeProgress;
  protected TextView longitude;
  protected ProgressBar longitudeProgress;
  protected ImageButton submit;

  private boolean forceZoom;

  public LocationAddSelectFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_location_add_select, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.find_coordinates));
    }
    onChoose();
    return view;
  }

  private void initElements(View view) {
    mapView = (MapView) view.findViewById(id.map);
    latitude = (TextView) view.findViewById(id.result_lat);
    latitudeProgress = (ProgressBar) view.findViewById(id.progress_latitude);
    longitude = (TextView) view.findViewById(id.result_lon);
    longitudeProgress = (ProgressBar) view.findViewById(id.progress_longitude);
    Button back = (Button) view.findViewById(id.back);
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clearResultLayout();
        onChoose();
        onChoose();
      }
    });
    submit = (ImageButton) view.findViewById(id.submit);
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onSubmit();
      }
    });
  }

  private void generateContent(float lat, float lon) {
    GeoPoint actPosition = new GeoPoint((double) lat, (double) lon);
    Util.setMapViewSettings(mapView, actPosition, forceZoom);
    ArrayList<OverlayItem> locations = new ArrayList<>(1);
    OverlayItem actPositionItem = new OverlayItem(getString(string.here),
        getString(string.my_position), actPosition);
    actPositionItem.setMarker(Util.resizeDrawable(getResources()
        .getDrawable(drawable.ic_marker_dark), getResources(), new Dimension(45, 45)));
    locations.add(actPositionItem);
    // Bonuspack
    MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(getActivity(),
        new MapEventsReceiver() {
          @Override
          public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
            InfoWindow.closeAllInfoWindowsOn(mapView);
            return true;
          }

          @Override
          public boolean longPressHelper(GeoPoint geoPoint) {
            InfoWindow.closeAllInfoWindowsOn(mapView);
            setResultLayout((float) geoPoint.getLatitude(), (float) geoPoint
                .getLongitude());
            return true;
          }
        });
    mapView.getOverlays().add(0, mapEventsOverlay);
    mapView.getOverlays().add(new ItemizedIconOverlay<>(getActivity(), locations, null));
  }

  protected final void onChoose() {
    clearResultLayout();
    forceZoom = true;
    ArrayList<Localization> localizations = new ArrayList<>(4);
    localizations.add(new Localization(drawable.ic_gps_dark, getString(string.gps_coordinates), Localization.GPS));
    localizations.add(new Localization(drawable.ic_camera_dark, getString(string.coord_photo_exif), Localization.PIC_COORD));
    localizations.add(new Localization(drawable.ic_manual_dark, getString(string.add_manual), Localization.MANUAL));
    if (App.palsActive) {
      localizations.add(new Localization(drawable.ic_pals_dark, getString(string.pals_provider), Localization.PALS));
    }
    DialogPlus dialog = DialogPlus.newDialog(getActivity())
        .setAdapter(new LocalizationHolder(getActivity(), localizations))
        .setOnItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
            Listener listener = new Listener() {
              @Override
              public void onComplete(Coord coord, boolean valid) {
                if (valid) {
                  setResultLayout(coord.getLatitude(), coord.getLongitude());
                }
              }
            };
            dialog.dismiss();
            if (position == 0) {
              App.localization.getCoords(LocalizationSystem.GPS, listener);
            } else if (position == 1) {
              searchExif();
            } else if (position == 2) {
              searchManual();
            } else if (position == 3) {
              App.localization.getCoords(LocalizationSystem.PALS, listener);
            }
          }
        })
        .setContentWidth(LayoutParams.WRAP_CONTENT)
        .setContentHeight(LayoutParams.WRAP_CONTENT)
        .create();
    dialog.show();
  }

  void searchManual() {
    Builder alertDialog = new Builder(getActivity());
    final View layout = getActivity().getLayoutInflater()
        .inflate(R.layout.dialog_location_add_manual, null);
    alertDialog.setView(layout);
    alertDialog.setPositiveButton(getString(string.confirm), new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        EditText latitude = (EditText) layout.findViewById(id.latitude);
        EditText longitude = (EditText) layout.findViewById(id.longitude);
        setResultLayout(Float.parseFloat(latitude.getText()
            .toString()), Float.parseFloat(longitude.getText().toString()));
      }
    });
    alertDialog.show();
  }

  void searchExif() {
    Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent, 0);
  }

  void setResultLayout(float lat, float lon) {
    if (lat != 0 && lon != 0) {
      submit.setVisibility(View.VISIBLE);
    }
    latitudeProgress.setVisibility(View.GONE);
    latitude.setText(String.valueOf(lat));
    longitudeProgress.setVisibility(View.GONE);
    longitude.setText(String.valueOf(lon));
    generateContent(lat, lon);
    forceZoom = false;
  }

  private void clearResultLayout() {
    submit.setVisibility(View.GONE);
    latitudeProgress.setVisibility(View.VISIBLE);
    latitudeProgress.setIndeterminate(true);
    latitude.setText(null);
    longitudeProgress.setVisibility(View.VISIBLE);
    longitudeProgress.setIndeterminate(true);
    longitude.setText(null);
  }

  protected final void onSubmit() {
    float lat = Float.parseFloat(latitude.getText().toString());
    float lon = Float.parseFloat(longitude.getText().toString());
    if (lat != 0 && lon != 0) {
      Fragment fragment = new LocationAddFragment();
      Bundle bundle = new Bundle();
      bundle.putFloat(Location.GEOLOCATION_COORD_LATITUDE, lat);
      bundle.putFloat(Location.GEOLOCATION_COORD_LONGITUDE, lon);
      fragment.setArguments(bundle);
      Util.changeFragment(null, fragment, id.frame_container, getActivity(), null);
    } else {
      Util.getMessageDialog(getString(string.no_valid_coords), getActivity());
    }
  }

  /**
   * Bild wird ausgesucht und uebergeben
   */
  @Override
  public final void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
      String path = Util.getPathFromCamera(data, getActivity());
      if (path != null) {
        RequestBody typedFile = RequestBody.create(MediaType.parse("image/*"), new File(path));
        App.rest.location.getLocationByExif(typedFile).enqueue(new Callback<Coord>() {
          @Override
          public void onResponse(Response<Coord> response, Retrofit retrofit) {
            if (response.isSuccess()) {
              setResultLayout(response.body().getLatitude(), response
                  .body()
                  .getLongitude());
            } else {
              setResultLayout(0, 0);
            }
          }

          @Override
          public void onFailure(Throwable t) {
            Util.getMessageDialog(t.getMessage(), getActivity());
          }
        });
      }
    }
  }
}


