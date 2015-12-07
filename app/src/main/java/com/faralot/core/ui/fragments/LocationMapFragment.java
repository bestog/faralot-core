package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.drawable;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.lib.LocalizationSystem.Listener;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.utils.Dimension;
import com.faralot.core.utils.Util;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.MaterialSearchView.OnQueryTextListener;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationMapFragment extends Fragment {

  protected View root;
  protected MapView mapView;
  protected FloatingActionButton refresh;
  protected FABProgressCircle refreshProgress;
  private MaterialSearchView searchView;

  private List<Location> locations;

  public LocationMapFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_location_map, container, false);
    initElements(view);
    setHasOptionsMenu(true);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(string.map);
    }
    initRefreshButton();
    initSearchView();
    return view;
  }

  private void initElements(View view) {
    root = view.findViewById(id.root);
    mapView = (MapView) view.findViewById(id.map);
    refresh = (FloatingActionButton) view.findViewById(id.refresh);
    refreshProgress = (FABProgressCircle) view.findViewById(id.refresh_progress);

  }

  @Override
  public final void onResume() {
    super.onResume();
    generateContent();
  }

  private void initRefreshButton() {
    refresh.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        refreshProgress.show();
        App.localization.getCoords(new Listener() {
          @Override
          public void onComplete(Coord coord, boolean valid) {
            App.latitude = coord.getLatitude();
            App.longitude = coord.getLongitude();
            generateContent();
            refreshProgress.hide();
          }
        });
      }
    });
  }

  private void initSearchView() {
    searchView = (MaterialSearchView) getActivity().findViewById(id.search_view);
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Util.setNewCoords(query, getActivity());
        generateContent();
        refreshProgress.hide();
        Util.getSnackbar(root, String.format(LocationMapFragment.this
            .getString(string.new_position), query));
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        searchView.setSuggestions(Util.getAddressesFromQuery(newText, LocationMapFragment.this
            .getActivity()));
        return false;
      }
    });
  }

  private void generateContent() {
    App.rest.location.getNearLocations(App.latitude, App.longitude, App.config.getLocation()
        .getMaxRadius(), "")
        .enqueue(new Callback<List<Location>>() {
          @Override
          public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
            locations = response.body();
            updateMap();
          }

          @Override
          public void onFailure(Throwable t) {
            Util.getMessageDialog(t.getMessage(), getActivity());
          }
        });
  }

  private void updateMap() {
    GeoPoint actPosition = new GeoPoint((double) App.latitude, (double) App.longitude);
    Util.setMapViewSettings(mapView, actPosition, true);
    RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(getActivity());
    Drawable clusterIconD = Util.resizeDrawable(getResources()
        .getDrawable(drawable.ic_cluster), this
        .getResources(), new Dimension(60, 60));
    Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
    poiMarkers.getTextPaint().setTextSize(15.0f);
    poiMarkers.setIcon(clusterIcon);
    mapView.getOverlays().add(poiMarkers);
    // Set locations & actual position
    if (locations.size() > 0) {
      for (Location location : locations) {
        Coord coord = location.getGeolocation().getCoord();
        Marker marker = new Marker(mapView);
        Drawable markIcon = getResources().getDrawable(drawable.ic_marker_accent);
        markIcon = Util.resizeDrawable(markIcon, getResources(), new Dimension(55, 55));
        marker.setIcon(markIcon);
        marker.setTitle(location.getName());
        marker.setSnippet(location.getDescription()
            .substring(0, Math.min(location.getDescription().length(), 200)) + "...");
        marker.setSubDescription(Util.humanDistance(location.getDistance()));
        marker.setPosition(new GeoPoint(coord.getLatitude(), coord.getLongitude()));
        marker.setInfoWindow(new LocationInfoWindow(mapView, location.getLid()));
        poiMarkers.add(marker);
      }
    } else {
      Util.getSnackbar(root, getString(string.no_location_found_please_click));
    }
    ArrayList<OverlayItem> markers = new ArrayList<>();
    OverlayItem actual = new OverlayItem(getString(string.my_position), "", actPosition);
    actual.setMarker(Util.resizeDrawable(getResources()
        .getDrawable(drawable.ic_marker_dark), this
        .getResources(), new Dimension(45, 45)));
    markers.add(actual);
    mapView.getOverlays().add(new ItemizedIconOverlay<>(getActivity(), markers, null));
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
            App.latitude = (float) geoPoint.getLatitude();
            App.longitude = (float) geoPoint.getLongitude();
            generateContent();
            return true;
          }
        });
    mapView.getOverlays().add(0, mapEventsOverlay);
    // ScaleBarOverlay
    mapView.getOverlays().add(new ScaleBarOverlay(getActivity()));
    // Rotation Gesture
    RotationGestureOverlay gestureOverlay = new RotationGestureOverlay(getActivity(), mapView);
    gestureOverlay.setEnabled(true);
    mapView.setMultiTouchControls(true);
    mapView.getOverlays().add(gestureOverlay);
  }

  class LocationInfoWindow extends MarkerInfoWindow {

    public LocationInfoWindow(MapView mapView, final String lid) {
      super(layout.window_location_info, mapView);
      Button btn = (Button) mView.findViewById(id.bubble_moreinfo);
      btn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (!lid.isEmpty()) {
            Fragment newFragment = new LocationDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Location.LID, lid);
            newFragment.setArguments(bundle);
            Util.changeFragment(LocationDetailsFragment.class.getSimpleName() + lid,
                newFragment, id.frame_container, getActivity(), null);
          }
        }
      });
    }

    @Override
    public void onOpen(Object item) {
      super.onOpen(item);
      InfoWindow.closeAllInfoWindowsOn(mapView);
      mView.findViewById(id.bubble_moreinfo).setVisibility(View.VISIBLE);
    }
  }

  @Override
  public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.menu_overview_map, menu);
    MenuItem item = menu.findItem(id.action_search);
    searchView.setMenuItem(item);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public final boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_change_to_list) {
      Util.changeFragment(LocationListFragment.class.getSimpleName(), new LocationListFragment(),
          R.id.frame_container, getActivity(), Util.STACK_FULL);
    } else if (id == R.id.action_add_location) {
      Util.changeFragment(LocationAddFragment.class.getSimpleName(), new LocationAddSelectFragment(),
          R.id.frame_container, getActivity(), null);
    } else if (id == R.id.action_filter) {
      Util.changeFragment(LocationFilterFragment.class.getSimpleName(), new LocationFilterFragment(),
          R.id.frame_container, getActivity(), null);
    } else {
      Log.e("Error", "ID not found.");
    }
    return super.onOptionsItemSelected(item);
  }
}
