package com.faralot.core.ui.fragments;

import android.R.layout;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.string;
import com.faralot.core.lib.MultiSpinner;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.Picture;
import com.faralot.core.rest.model.Scope;
import com.faralot.core.rest.model.location.Address;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.rest.model.location.Opening;
import com.faralot.core.rest.model.location.Prices;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.ui.holder.LocationOpeningEditHolder;
import com.faralot.core.ui.holder.LocationPicturesEditHolder;
import com.faralot.core.ui.holder.LocationPricesEditHolder;
import com.faralot.core.utils.Util;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.parceler.Parcels;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationEditFragment extends Fragment {

  protected EditText name;
  protected EditText description;
  protected TextView locale;
  protected ListView pictures;
  protected ActionProcessButton submit;
  // Erweiterungen
  protected LinearLayout suitableLayout;
  protected Spinner suitableFrom;
  protected Spinner suitableTo;
  protected LinearLayout categoryLayout;
  protected Spinner category;
  protected LinearLayout openingLayout;
  protected ListView openings;
  protected LinearLayout pricesLayout;
  protected ListView prices;
  protected LinearLayout featuresLayout;
  protected MultiSpinner features;
  private List<Opening> openingList;
  private List<Prices> pricesList;
  private Location location;

  public LocationEditFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_location_edit, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.edit));
    }
    location = Parcels.unwrap(getArguments().getParcelable("location"));
    generateContent();
    return view;
  }

  private void initElements(View view) {
    name = (EditText) view.findViewById(id.name);
    description = (EditText) view.findViewById(id.description);
    locale = (TextView) view.findViewById(id.locale);
    pictures = (ListView) view.findViewById(id.pictures);
    submit = (ActionProcessButton) view.findViewById(id.submit);
    submit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onSubmit();
      }
    });
    // Erweiterungen
    suitableLayout = (LinearLayout) view.findViewById(id.layout_suitable);
    suitableFrom = (Spinner) view.findViewById(id.suitable_from);
    suitableTo = (Spinner) view.findViewById(id.suitable_to);
    categoryLayout = (LinearLayout) view.findViewById(id.layout_category);
    category = (Spinner) view.findViewById(id.category);
    openingLayout = (LinearLayout) view.findViewById(id.layout_opening);
    openings = (ListView) view.findViewById(id.openings);
    Button addOpening = (Button) view.findViewById(id.add_opening);
    addOpening.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onAddOpening();
      }
    });
    pricesLayout = (LinearLayout) view.findViewById(id.layout_prices);
    prices = (ListView) view.findViewById(id.prices);
    Button addPrices = (Button) view.findViewById(id.add_prices);
    addPrices.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onAddPrices();
      }
    });
    featuresLayout = (LinearLayout) view.findViewById(id.layout_features);
    features = (MultiSpinner) view.findViewById(id.features);
  }

  private void initFeaturesSpinner(List<String> featureList) {
    List<String> list = App.config.getLocation().getFeatures();
    features.setItems(list, featureList, "", -1, null);
  }

  private void initSuitable(Spinner spinner, int selected) {
    List<Integer> range = new ArrayList<>();
    for (int x = 0; x < 100; ++x) {
      range.add(x);
    }
    ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), layout.simple_spinner_item, range);
    arrayAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
    spinner.setAdapter(arrayAdapter);
    spinner.setSelection(selected);
  }

  private void initCategorySpinner(String selected) {
    ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), layout.simple_spinner_item,
        App.config
            .getLocation()
            .getCategories());
    arrayAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
    category.setAdapter(arrayAdapter);
    category.setSelection(Util.getIndexFromSpinner(category, selected));
  }

  private void generateContent() {
    name.setText(location.getName());
    description.setText(location.getDescription());
    Coord coord = location.getGeolocation().getCoord();
    Address address = location.getGeolocation().getAddress();
    locale.setText(
        String.format(getString(string.address_format), address.getStreet(), address
            .getStreetNumber(), address.getPostalcode(), address.getCity()) + "\n" +
            String.format(getString(string.coordinates_format), coord.getLatitude(), coord
                .getLongitude()));
    // Pictures
    List<Picture> pictureList = location.getPictures();
    pictures.setAdapter(new LocationPicturesEditHolder(getActivity(), pictureList, location
        .getLid()));
    Util.expandListView(pictures);
    // Erweiterungen
    if (App.fields.contains("category")) {
      categoryLayout.setVisibility(View.VISIBLE);
      category.setSelection(Util.getIndexFromSpinner(category, location.getCategory()));
    }
    if (App.fields.contains("prices")) {
      pricesLayout.setVisibility(View.VISIBLE);
      prices.setAdapter(new LocationPricesEditHolder(getActivity(), location.getPrices(), location
          .getLid()));
      pricesList = location.getPrices();
      Util.expandListView(prices);
    }
    if (App.fields.contains("opening")) {
      openingLayout.setVisibility(View.VISIBLE);
      openings.setAdapter(new LocationOpeningEditHolder(getActivity(), location.getOpening(), location
          .getLid()));
      openingList = location.getOpening();
      Util.expandListView(openings);
    }
    if (App.fields.contains("features")) {
      featuresLayout.setVisibility(View.VISIBLE);
      initFeaturesSpinner(location.getFeatures());
    }
    if (App.fields.contains("category")) {
      categoryLayout.setVisibility(View.VISIBLE);
      initCategorySpinner(location.getCategory());
    }
    if (App.fields.contains("suitable")) {
      suitableLayout.setVisibility(View.VISIBLE);
      initSuitable(suitableFrom, location.getSuitable().getFrom());
      initSuitable(suitableTo, location.getSuitable().getTo());
    }
  }

  protected void onAddOpening() {
    final DialogPlus dialog = DialogPlus.newDialog(getActivity())
        .setContentHolder(new ViewHolder(R.layout.dialog_location_opening))
        .setGravity(Gravity.CENTER)
        .setContentHeight(LayoutParams.MATCH_PARENT)
        .setExpanded(true)
        .setPadding(10, 10, 10, 10)
        .setCancelable(true)
        .setFooter(null)
        .create();
    final ActionProcessButton submit = (ActionProcessButton) dialog.findViewById(id.submit);
    final Spinner weekday = (Spinner) dialog.findViewById(id.day);
    final Spinner start = (Spinner) dialog.findViewById(id.start);
    final Spinner end = (Spinner) dialog.findViewById(id.end);
    initOpeningDay(weekday);
    initOpeningTime(start);
    initOpeningTime(end);
    submit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        submit.setMode(Mode.ENDLESS);
        submit.setProgress(1);
        final Opening request = new Opening();
        request.setWeekday(weekday.getSelectedItemPosition());
        request.setBegin(start.getSelectedItem().toString());
        request.setEnd(end.getSelectedItem().toString());
        App.rest.location.postOpening(location.getLid(), request)
            .enqueue(new Callback<vResponse>() {
              @Override
              public void onResponse(Response<vResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                  submit.setProgress(100);
                  openingList.add(request);
                  openings.setAdapter(new LocationOpeningEditHolder(LocationEditFragment.this
                      .getActivity(), openingList, location
                      .getLid()));
                  generateContent();
                } else {
                  submit.setProgress(-1);
                }
                dialog.dismiss();
              }

              @Override
              public void onFailure(Throwable t) {
                dialog.dismiss();
                submit.setProgress(-1);
                Util.getMessageDialog(t.getMessage(), getActivity());
              }
            });
      }
    });
    dialog.show();
  }

  protected void onAddPrices() {
    final Dialog dialog = new Dialog(getActivity());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_location_prices);
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    final ActionProcessButton submit = (ActionProcessButton) dialog.findViewById(id.submit);
    final EditText description = (EditText) dialog.findViewById(id.description);
    final EditText price = (EditText) dialog.findViewById(id.price);
    final Spinner from = (Spinner) dialog.findViewById(id.from);
    final Spinner to = (Spinner) dialog.findViewById(id.to);
    initSuitable(from, 0);
    initSuitable(to, 0);
    submit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        submit.setMode(Mode.ENDLESS);
        submit.setProgress(1);
        final Prices request = new Prices();
        if (description.getText().length() == 0 || price.getText().length() == 0) {
          Util.getMessageDialog("Bitte alle Felder ausfüllen", getActivity());
        } else {
          request.setAgegroup(new Scope(Integer.valueOf(from.getSelectedItem()
              .toString()), Integer.valueOf(to
              .getSelectedItem()
              .toString())));
          request.setDescription(description.getText().toString());
          request.setPrice(Float.parseFloat(price.getText().toString()));
          App.rest.location.postPrices(location.getLid(), request)
              .enqueue(new Callback<vResponse>() {
                @Override
                public void onResponse(Response<vResponse> response, Retrofit retrofit) {
                  if (response.isSuccess()) {
                    submit.setProgress(100);
                    pricesList.add(request);
                    prices.setAdapter(new LocationPricesEditHolder(getActivity(), pricesList, location
                        .getLid()));
                    generateContent();
                  } else {
                    submit.setProgress(-1);
                  }
                  dialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                  submit.setProgress(-1);
                  dialog.dismiss();
                  Util.getMessageDialog(t.getMessage(), getActivity());
                }
              });
        }
      }
    });
    dialog.show();
  }

  private void initOpeningTime(Spinner spinner) {
    List<String> times = new ArrayList<>();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.GERMAN);
    Calendar cal = Calendar.getInstance();
    cal.set(2000, 1, 1, 0, 0, 0);
    for (int i = 0; i < 288; ++i) {
      cal.add(Calendar.MINUTE, 5);
      times.add(dateFormat.format(cal.getTime()));
    }
    ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), layout.simple_spinner_item, times);
    arrayAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
    spinner.setAdapter(arrayAdapter);
  }

  private void initOpeningDay(Spinner weekday) {
    List<String> days = new ArrayList<>();
    for (int x = 0; x < 7; ++x) {
      days.add(Util.getWeekday(x));
    }
    ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), layout.simple_spinner_item, days);
    arrayAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item);
    weekday.setAdapter(arrayAdapter);
  }

  protected final void onSubmit() {
    submit.setMode(Mode.ENDLESS);
    submit.setProgress(1);
    com.faralot.core.rest.model.request.Location request = new com.faralot.core.rest.model.request.Location();
    request.setName(name.getText().toString());
    request.setDescription(description.getText().toString());
    request.setCategory(category.getSelectedItem().toString());
    request.setSuitable(new Scope(Integer.valueOf(suitableFrom.getSelectedItem()
        .toString()), Integer.valueOf(suitableTo
        .getSelectedItem()
        .toString())));
    request.setFeatures(Arrays.asList(Util.trimArray(features.getSelectedItem()
        .toString()
        .split(", "))));
    App.rest.location.updateLocation(location.getLid(), request).enqueue(new Callback<Location>() {
      @Override
      public void onResponse(Response<Location> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          submit.setProgress(100);
          Fragment newFragment = new LocationDetailsFragment();
          Bundle bundle = new Bundle();
          bundle.putString(Location.LID, location.getLid());
          newFragment.setArguments(bundle);
          Util.changeFragment(LocationDetailsFragment.class.getSimpleName()
                  + location.getLid(), newFragment, id.frame_container,
              getActivity(), Util.STACK_ONE);
        } else {
          try {
            submit.setProgress(-1);
            Util.getMessageDialog(response.errorBody().string(), getActivity());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onFailure(Throwable t) {
        submit.setProgress(-1);
        Util.getMessageDialog(t.getMessage(), getActivity());
      }
    });
  }
}
