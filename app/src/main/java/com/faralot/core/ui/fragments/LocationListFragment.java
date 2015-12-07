package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.PullRefreshLayout.OnRefreshListener;
import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.lib.LocalizationSystem.Listener;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.ui.holder.LocationsHolder;
import com.faralot.core.utils.Util;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.MaterialSearchView.OnQueryTextListener;

import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationListFragment extends Fragment {

  protected View root;
  protected TextView resultCount;
  protected TextView myPosition;
  protected ListView listView;
  protected PullRefreshLayout frameLayout;
  protected TextView noResult;
  MaterialSearchView searchView;
  String myAddress;

  public LocationListFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_location_list, container, false);
    initElements(view);
    setHasOptionsMenu(true);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.app_name));
    }
    initList();
    initSearchView();
    return view;
  }

  private void initElements(View view) {
    root = view.findViewById(id.root);
    resultCount = (TextView) view.findViewById(id.result_count);
    myPosition = (TextView) view.findViewById(id.my_position);
    listView = (ListView) view.findViewById(id.list);
    frameLayout = (PullRefreshLayout) view.findViewById(id.list_update);
    noResult = (TextView) view.findViewById(id.no_result);
  }

  private void initSearchView() {
    searchView = (MaterialSearchView) getActivity().findViewById(id.search_view);
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        Util.setNewCoords(query, getActivity());
        myAddress = query;
        generateContent();
        Util.getSnackbar(root, String.format(LocationListFragment.this
            .getString(string.new_position), query));
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        searchView.setSuggestions(Util.getAddressesFromQuery(newText, LocationListFragment.this
            .getActivity()));
        return false;
      }
    });
  }

  private void initList() {
    frameLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        App.rest.location.getNearLocations(App.latitude, App.longitude, App.radius, App.filterFeatures)
            .enqueue(new Callback<List<Location>>() {
              @Override
              public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                  setList(response.body());
                }
              }

              @Override
              public void onFailure(Throwable t) {
                Util.getMessageDialog(t.getMessage(), getActivity());
              }
            });
      }
    });
    generateContent();
  }

  protected final void generateContent() {
    if (App.longitude == 0 && App.latitude == 0) {
      frameLayout.setRefreshing(true);
      App.localization.getCoords(new Listener() {
        @Override
        public void onComplete(Coord coord, boolean valid) {
          if (valid) {
            App.latitude = coord.getLatitude();
            App.longitude = coord.getLongitude();
            fillContent();
          }
        }
      });
    } else {
      fillContent();
    }
  }

  private void fillContent() {
    try {
      Geocoder geocoder = new Geocoder(getActivity());
      List<Address> addresses = geocoder.getFromLocation(App.latitude,
          App.longitude, 1);
      if (addresses.size() > 0) {
        myAddress = Util.addressToString(addresses.get(0));
        myPosition.setText(myAddress);
      }
    } catch (NullPointerException | IOException e) {
      e.printStackTrace();
    }
    App.rest.location.getNearLocations(App.latitude, App.longitude, App.radius, App.filterFeatures)
        .enqueue(new Callback<List<Location>>() {
          @Override
          public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
            setList(response.body());
          }

          @Override
          public void onFailure(Throwable t) {
            Util.getMessageDialog(t.getMessage(), getActivity());
          }
        });
  }

  @Override
  public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.menu_overview_list, menu);
    MenuItem item = menu.findItem(id.action_search);
    searchView.setMenuItem(item);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public final boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_change_to_map) {
      Util.changeFragment(LocationMapFragment.class.getSimpleName(), new LocationMapFragment(), R.id.frame_container, this
          .getActivity(), Util.STACK_FULL);
    } else if (id == R.id.action_filter) {
      Util.changeFragment(LocationFilterFragment.class.getSimpleName(), new LocationFilterFragment(), R.id.frame_container, this
          .getActivity(), null);
    } else if (id == R.id.action_add_location) {
      Util.changeFragment(LocationAddSelectFragment.class.getSimpleName(), new LocationAddSelectFragment(), R.id.frame_container, this
          .getActivity(), null);
    } else {
      Log.e("Error", "ID not found.");
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onResume() {
    super.onResume();
    initList();
  }

  private void setList(List<Location> locations) {
    if (locations.size() > 0) {
      if (isAdded()) {
        resultCount.setText(String.format(getString(string.results_found), locations
            .size()));
        frameLayout.setVisibility(View.VISIBLE);
        listView.setAdapter(new LocationsHolder(getActivity(), locations));
      }
    } else {
      frameLayout.setVisibility(View.GONE);
      noResult.setVisibility(View.VISIBLE);
    }
    myPosition.setText(myAddress);
    frameLayout.setRefreshing(false);
  }

}
