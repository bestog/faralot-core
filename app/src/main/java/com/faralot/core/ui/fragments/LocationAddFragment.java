package com.faralot.core.ui.fragments;

import android.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.string;
import com.faralot.core.lib.MultiSpinner;
import com.faralot.core.rest.model.Scope;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.rest.model.location.Geolocation;
import com.faralot.core.rest.model.request.Location;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationAddFragment extends Fragment {

  protected EditText name;
  protected EditText description;
  protected Spinner category;
  protected MultiSpinner features;
  protected Spinner suitableFrom;
  protected Spinner suitableTo;
  protected ActionProcessButton button;
  float lat, lon;

  public LocationAddFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_location_add, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.add_location));
    }
    Bundle bundle = getArguments();
    if (bundle != null) {
      lat = bundle.getFloat(com.faralot.core.rest.model.Location.GEOLOCATION_COORD_LATITUDE);
      lon = bundle.getFloat(com.faralot.core.rest.model.Location.GEOLOCATION_COORD_LONGITUDE);
    }
    initCategorySpinner();
    initFeaturesSpinner();
    initSuitable(suitableFrom);
    initSuitable(suitableTo);
    return view;
  }

  private void initElements(View view) {
    name = (EditText) view.findViewById(id.name);
    description = (EditText) view.findViewById(id.description);
    category = (Spinner) view.findViewById(id.category);
    features = (MultiSpinner) view.findViewById(id.features);
    suitableFrom = (Spinner) view.findViewById(id.suitable_from);
    suitableTo = (Spinner) view.findViewById(id.suitable_to);
    button = (ActionProcessButton) view.findViewById(id.submit);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onSubmit();
      }
    });
  }

  private void initFeaturesSpinner() {
    List<String> list = App.config.getLocation().getFeatures();
    features.setItems(list, null, "", -1, null);
  }

  private void initCategorySpinner() {
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), layout.simple_spinner_item, App.config
        .getLocation()
        .getCategories());
    dataAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
    category.setAdapter(dataAdapter);
  }

  private void initSuitable(Spinner spinner) {
    List<Integer> range = new ArrayList<>();
    for (int x = 0; x < 100; ++x) {
      range.add(x);
    }
    ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), layout.simple_spinner_item, range);
    arrayAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
    spinner.setAdapter(arrayAdapter);
  }

  public void onSubmit() {
    button.setMode(Mode.ENDLESS);
    button.setProgress(1);
    Location request = new Location();
    request.setName(name.getText().toString());
    request.setCategory(String.valueOf(category.getSelectedItem()));
    request.setDescription(description.getText().toString());
    request.setGeolocation(new Geolocation(new Coord(lat, lon)));
    request.setSuitable(new Scope(Integer.parseInt(suitableFrom.getSelectedItem()
        .toString()), Integer.parseInt(suitableTo.getSelectedItem().toString())));
    request.setFeatures(Arrays.asList(Util.trimArray(features.getSelectedItem()
        .toString()
        .split(", "))));
    App.rest.location.postLocation(request).enqueue(new Callback<vResponse>() {
      @Override
      public void onResponse(Response<vResponse> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          button.setProgress(100);
          Util.changeFragment(LocationListFragment.class.getSimpleName(), new LocationListFragment(), id.frame_container, getActivity(), Util.STACK_FULL);
        } else {
          Util.getMessageDialog("Location konnte nicht hinzugefügt werden", getActivity());
        }
      }

      @Override
      public void onFailure(Throwable t) {
        button.setProgress(-1);
        Util.getMessageDialog(t.getMessage(), getActivity());
      }
    });
  }
}


