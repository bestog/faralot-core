package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.lib.MultiSpinner;

import java.util.Arrays;
import java.util.List;

public class LocationFilterFragment extends Fragment {

  protected SeekBar radius;
  protected TextView radiusText;
  protected MultiSpinner features;

  public LocationFilterFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_location_filter, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.filter_results));
    }
    initRadius();
    initFeaturesSpinner();
    return view;
  }

  private void initElements(View view) {
    radius = (SeekBar) view.findViewById(id.radius);
    radiusText = (TextView) view.findViewById(id.radius_text);
    features = (MultiSpinner) view.findViewById(id.features);
    Button submit = (Button) view.findViewById(id.filter);
    submit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onFilter();
      }
    });
  }

  private void initFeaturesSpinner() {
    List<String> list = App.config.getLocation().getFeatures();
    List<String> selected = Arrays.asList(App.filterFeatures.split(", "));
    features.setItems(list, selected, "", -1, null);
  }

  private void initRadius() {
    radius.setProgress(App.radius);
    radius.setMax(App.config.getLocation().getMaxRadius());
    radiusText.setText(String.format(getString(string.value_km), App.radius));
    radius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        App.radius = progress;
        radiusText.setText(String.format(getString(string.value_km), App.radius));
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
  }

  protected void onFilter() {
    App.radius = radius.getProgress();
    App.filterFeatures = features.getSelectedItem().toString();
    getActivity().getFragmentManager().popBackStack();
  }
}
