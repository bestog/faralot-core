package com.faralot.core.ui.fragments;

import android.R.layout;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.drawable;
import com.faralot.core.R.id;
import com.faralot.core.R.plurals;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.Picture;
import com.faralot.core.rest.model.location.Address;
import com.faralot.core.rest.model.location.Coord;
import com.faralot.core.rest.model.user.Visits;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.ui.holder.LocationOpeningHolder;
import com.faralot.core.ui.holder.LocationPricesHolder;
import com.faralot.core.utils.Dimension;
import com.faralot.core.utils.Util;
import com.isseiaoki.simplecropview.CropImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.parceler.Parcels;
import org.parceler.apache.commons.lang.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationDetailsFragment extends Fragment {

  protected View root;
  protected SliderLayout pictures;
  protected TextView name;
  protected TextView description;
  protected TextView locationAddress;
  protected TextView geolocation;
  protected ImageView toGoogleMaps;
  protected MapView mapView;
  // Erweiterungen
  protected TextView category;
  protected LinearLayout featuresLayout;
  protected TextView features;
  protected LinearLayout ratingLayout;
  protected TextView stars;
  protected TextView starsCount;
  protected LinearLayout visitsLayout;
  protected LinearLayout pricesLayout;
  protected ListView prices;
  protected TextView visits;
  protected LinearLayout openingLayout;
  protected ListView opening;
  protected TextView suitable;

  private String lid;
  private boolean isFavorite;
  private List<String> visitCount;
  MenuItem favoriteMenuItem;
  Location location;

  public LocationDetailsFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_location_detail, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.location_details));
    }
    setHasOptionsMenu(true);
    lid = getArguments().getString(Location.LID);
    generateContent();
    return view;
  }

  private void initElements(View view) {
    root = view.findViewById(id.root);
    pictures = (SliderLayout) view.findViewById(id.pictures);
    name = (TextView) view.findViewById(id.name);
    description = (TextView) view.findViewById(id.description);
    locationAddress = (TextView) view.findViewById(id.address);
    geolocation = (TextView) view.findViewById(id.coordinates);
    toGoogleMaps = (ImageView) view.findViewById(id.to_google);
    toGoogleMaps.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onRoute();
      }
    });
    mapView = (MapView) view.findViewById(id.map);
    ImageButton upload = (ImageButton) view.findViewById(id.add_picture);
    upload.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
      }
    });
    Button report = (Button) view.findViewById(id.fragment_detail_location_report);
    report.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onReport();
      }
    });
    // Erweiterungen
    category = (TextView) view.findViewById(id.category);
    featuresLayout = (LinearLayout) view.findViewById(id.features_layout);
    features = (TextView) view.findViewById(id.features_list);
    ratingLayout = (LinearLayout) view.findViewById(id.rating_layout);
    ratingLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onStar();
      }
    });
    stars = (TextView) view.findViewById(id.stars);
    starsCount = (TextView) view.findViewById(id.stars_count);
    visitsLayout = (LinearLayout) view.findViewById(id.visits_layout);
    visitsLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onVisits();
      }
    });
    visits = (TextView) view.findViewById(id.visits);
    pricesLayout = (LinearLayout) view.findViewById(id.prices_layout);
    prices = (ListView) view.findViewById(id.prices_list);
    openingLayout = (LinearLayout) view.findViewById(id.opening_layout);
    opening = (ListView) view.findViewById(id.opening_list);
    suitable = (TextView) view.findViewById(id.suitable);
  }

  /**
   * Zeige den Favoriten-Status an
   */
  private void getLocationFavorite() {
    App.rest.user.getFavorite(lid).enqueue(new Callback<Location>() {
      @Override
      public void onResponse(Response<Location> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          favoriteMenuItem.setIcon(drawable.ic_star_full);
          isFavorite = true;
        }
      }

      @Override
      public void onFailure(Throwable t) {
      }
    });
  }

  private void fillContent() {
    Address address = location.getGeolocation().getAddress();
    Coord coord = location.getGeolocation().getCoord();
    // Allgemein
    name.setText(location.getName());
    description.setText(location.getDescription());
    // Adresse
    if (address.getStreet() != null && !address.getStreet().equals("")) {
      locationAddress.setText(
          String.format(getString(string.address_format), address.getStreet(), address
              .getStreetNumber(), address.getPostalcode(), address.getCity()));
    }
    geolocation.setText(
        String.format(getString(string.coordinates_format), coord.getLatitude(), coord
            .getLongitude()));
    // OSM Map
    GeoPoint actPosition = new GeoPoint((double) coord.getLatitude(), (double) coord.getLongitude());
    mapView.setTileSource(TileSourceFactory.MAPNIK);
    mapView.getOverlayManager().clear();
    mapView.getController().setZoom(17);
    mapView.getController().setCenter(actPosition);
    ArrayList<OverlayItem> markerLocations = new ArrayList<>(2);
    OverlayItem actPositionItem = new OverlayItem(getString(string.here),
        getString(string.my_position),
        new GeoPoint((double) App.latitude, (double) App.longitude));
    actPositionItem.setMarker(Util.resizeDrawable(getResources().getDrawable(drawable.ic_marker_dark), this
        .getResources(), new Dimension(45, 45)));
    OverlayItem oi = new OverlayItem(location.getLid(), location.getName(),
        new GeoPoint((double) coord
            .getLatitude(), (double) coord.getLongitude()));
    oi.setMarker(Util.resizeDrawable(getResources().getDrawable(drawable.ic_marker_accent), this
        .getResources(), new Dimension(55, 55)));
    markerLocations.add(oi);
    markerLocations.add(actPositionItem);
    mapView.getOverlays().add(new ItemizedIconOverlay<>(getActivity(), markerLocations, null));
    try {
      toGoogleMaps.setImageDrawable(getActivity().getPackageManager()
          .getApplicationIcon("com.google.android.apps.maps"));
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      //@todo: better logging
    }
    // Bewertung
    if (App.fields.contains("rating")) {
      ratingLayout.setVisibility(View.VISIBLE);
      int reviewCount = location.getRating().getReviews().size();
      starsCount.setText(getResources()
          .getQuantityString(plurals.review_count, reviewCount, reviewCount));
      stars.setText(String.valueOf(location.getRating().getAverage()));
    }
    // Category
    if (App.fields.contains("category")) {
      category.setVisibility(View.VISIBLE);
      category.setText(String.format(getString(string.category_value), location.getCategory()));
    }
    // Pictures
    pictures.removeAllSliders();
    if (location.getPictures().size() > 0) {
      for (Picture picture : location.getPictures()) {
        TextSliderView textSliderView = new TextSliderView(getActivity());
        textSliderView.image(picture.getUrl()).description(picture.getTitle())
            .setScaleType(ScaleType.CenterInside)
            .setOnSliderClickListener(new OnSliderClickListener() {
              @Override
              public void onSliderClick(BaseSliderView slider) {
                // @todo
              }
            });
        pictures.addSlider(textSliderView);
      }
    } else {
      TextSliderView textSliderView = new TextSliderView(getActivity());
      textSliderView
          .image(drawable.img_picture_empty)
          .setScaleType(ScaleType.FitCenterCrop);
      pictures.addSlider(textSliderView);
    }
    pictures.stopAutoCycle();
    // Features
    if (App.fields.contains("features")) {
      featuresLayout.setVisibility(View.VISIBLE);
      if (location.getFeatures().size() > 0) {
        features.setText(StringUtils.join(location.getFeatures(), ", "));
      } else {
        features.setText(string.no_features_found);
      }
    }
    // Opening
    if (App.fields.contains("opening")) {
      openingLayout.setVisibility(View.VISIBLE);
      if (location.getOpening().size() > 0) {
        opening.setAdapter(new LocationOpeningHolder(getActivity(), location.getOpening()));
        Util.expandListView(opening);
      }
    }
    // Prices
    if (App.fields.contains("prices")) {
      pricesLayout.setVisibility(View.VISIBLE);
      if (location.getPrices().size() > 0) {
        prices.setAdapter(new LocationPricesHolder(getActivity(), location.getPrices()));
        Util.expandListView(prices);
      }
    }
    // Suitable
    if (App.fields.contains("suitable")) {
      suitable.setVisibility(View.VISIBLE);
      suitable.setText(String.format(getString(string.suitable_from_to), location.getSuitable()
          .getFrom(), location
          .getSuitable()
          .getTo()));
    }
  }

  /**
   * Generiere die Detailseite
   */
  private void generateContent() {
    App.rest.location.getLocation(lid).enqueue(new Callback<Location>() {
      @Override
      public void onResponse(Response<Location> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          location = response.body();
          fillContent();
        } else {
          try {
            Util.getSnackbar(root, response.errorBody().string());
          } catch (IOException e) {
            e.printStackTrace();
            //@todo: better logging
          }
        }
      }

      @Override
      public void onFailure(Throwable t) {
        Util.getSnackbar(root, t.getMessage());
      }
    });
  }

  /**
   * Bild wird ausgesucht und uebergeben
   */
  @Override
  public final void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
      String path = Util.getPathFromCamera(data, getActivity());
      if (path != null) {
        DialogPlus dialog = DialogPlus.newDialog(getActivity())
            .setContentHolder(new ViewHolder(R.layout.dialog_location_pictures))
            .setGravity(Gravity.CENTER)
            .setExpanded(true)
            .setCancelable(true)
            .setContentWidth(LayoutParams.MATCH_PARENT)
            .setContentHeight(LayoutParams.MATCH_PARENT)
            .setFooter(null)
            .create();
        final CropImageView image = (CropImageView) dialog.findViewById(id.upload_image);
        image.setImageBitmap(Util.decodeScaledBitmapFromSdCard(path, App.config.getUser()
            .getPictureSize()));
        Util.setCrop(getResources(), image);
        ActionProcessButton submit = (ActionProcessButton) dialog.findViewById(id.submit);
        ImageButton rotateRightButton = (ImageButton) dialog.findViewById(id.rotate_right);
        rotateRightButton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            image.setImageBitmap(Util.rotateImage(image.getImageBitmap(), new Dimension(image.getWidth(), image
                .getHeight()), 90));
          }
        });
        submit.setOnClickListener(new UploadImageListener(dialog, image));
        dialog.show();
      }
    }
  }

  /**
   * Route zu Location anzeigen bei Google
   */
  protected final void onRoute() {
    Coord coord = location.getGeolocation().getCoord();
    Intent intent = new Intent(Intent.ACTION_VIEW,
        Uri.parse("http://maps.google.com/maps?" + "daddr=" + coord
            .getLatitude() + ',' + coord.getLongitude()));
    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
    startActivity(intent);
  }

  /**
   * Location melden
   */
  protected final void onReport() {
    final DialogPlus dialog = DialogPlus.newDialog(getActivity())
        .setContentHolder(new ViewHolder(R.layout.dialog_location_report))
        .setGravity(Gravity.CENTER)
        .setExpanded(true)
        .setPadding(10, 10, 10, 10)
        .setCancelable(true)
        .setFooter(null)
        .create();
    dialog.show();
    final ActionProcessButton button = (ActionProcessButton) dialog.findViewById(
        id.dialog_report_submit);
    final TextView text = (TextView) dialog.findViewById(id.dialog_report_text);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (text.getText().toString().isEmpty()) {
          text.setError(getString(string.not_empty_message));
        } else {
          button.setMode(Mode.ENDLESS);
          button.setProgress(1);
          App.rest.location.postReport(lid, text.getText().toString())
              .enqueue(new Callback<vResponse>() {
                @Override
                public void onResponse(Response<vResponse> response, Retrofit retrofit) {
                  if (response.isSuccess()) {
                    generateContent();
                    button.setProgress(100);
                    Util.getSnackbar(root, getActivity().getString(string.report_success));
                    dialog.dismiss();
                  } else {
                    button.setProgress(-1);
                  }
                }

                @Override
                public void onFailure(Throwable t) {
                  button.setProgress(-1);
                }
              });
        }
      }
    });
  }

  protected final void onStar() {
    Fragment newFragment = new LocationReviewFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Location.LID, lid);
    newFragment.setArguments(bundle);
    Util.changeFragment(
        LocationReviewFragment.class.getSimpleName() + lid, newFragment, id.frame_container,
        getActivity(), null);
  }

  protected final void onVisits() {
    DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
        .setAdapter(new ArrayAdapter<>(getActivity(), layout.simple_list_item_1, visitCount
            .toArray(new String[visitCount.size()])))
        .setCancelable(true)
        .setOnDismissListener(null)
        .setOnCancelListener(null)
        .setOnBackPressListener(null)
        .create();
    dialogPlus.show();
  }

  @Override
  public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.menu_details, menu);
    favoriteMenuItem = menu.findItem(id.action_favorite);
    if (!App.fields.contains("visits")) {
      menu.findItem(id.action_visit).setVisible(false);
    }
    getLocationFavorite();
    getLocationVisits();
    super.onCreateOptionsMenu(menu, inflater);
  }

  private void getLocationVisits() {
    App.rest.user.getVisits().enqueue(new Callback<List<Visits>>() {
      @Override
      public void onResponse(Response<List<Visits>> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          visitCount = new ArrayList<>();
          visitsLayout.setVisibility(View.VISIBLE);
          for (Visits visit : response.body()) {
            if (visit.getLocation().equals(lid)) {
              visits.setText(String.format(getString(string.x_visits), visit.getTimes().size()));
              visitCount = visit.getTimes();
              return;
            }
          }
          visits.setText(String.format(getString(string.x_visits), 0));
        }
      }

      @Override
      public void onFailure(Throwable t) {
        Util.getSnackbar(root, t.getMessage());
      }
    });
  }

  @Override
  public final boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_favorite) {
      if (isFavorite) {
        App.rest.user.deleteFavorite(lid).enqueue(new Callback<vResponse>() {
          @Override
          public void onResponse(Response<vResponse> response, Retrofit retrofit) {
            if (response.isSuccess()) {
              Util.getSnackbar(root, getString(string.delete_favorite_success));
              favoriteMenuItem.setIcon(drawable.ic_star_empty);
              isFavorite = false;
            }
          }

          @Override
          public void onFailure(Throwable t) {
          }
        });
      } else {
        App.rest.user.postFavorite(lid).enqueue(new Callback<vResponse>() {
          @Override
          public void onResponse(Response<vResponse> response, Retrofit retrofit) {
            if (response.isSuccess()) {
              Util.getSnackbar(root, getString(string.add_favorite_success));
              favoriteMenuItem.setIcon(drawable.ic_star_full);
              isFavorite = true;
            }
          }

          @Override
          public void onFailure(Throwable t) {
          }
        });
      }
    } else if (id == R.id.action_visit) {
      App.rest.user.postVisit(lid).enqueue(new Callback<vResponse>() {
        @Override
        public void onResponse(Response<vResponse> response, Retrofit retrofit) {
          if (response.isSuccess()) {
            Util.getSnackbar(root, getString(string.add_visit_success));
            getLocationVisits();
          }
        }

        @Override
        public void onFailure(Throwable t) {
        }
      });
    } else if (id == R.id.action_edit) {
      Fragment newFragment = new LocationEditFragment();
      Bundle bundle = new Bundle();
      bundle.putParcelable("location", Parcels.wrap(location));
      newFragment.setArguments(bundle);
      Util.changeFragment(
          LocationEditFragment.class.getSimpleName() + lid, newFragment, R.id.frame_container,
          getActivity(), null);
    } else {
      Log.e("Error", "ID not found.");
    }
    return super.onOptionsItemSelected(item);
  }

  private class UploadImageListener implements OnClickListener {

    private final DialogPlus dialog;
    private final CropImageView image;

    public UploadImageListener(DialogPlus dp, CropImageView iv) {
      this.dialog = dp;
      this.image = iv;
    }

    @Override
    public void onClick(View v) {
      final ActionProcessButton submit = (ActionProcessButton) dialog.findViewById(id.submit);
      submit.setMode(Mode.ENDLESS);
      submit.setProgress(1);
      Bitmap out = Util.resizeImage(image.getCroppedBitmap(), App.config.getLocation()
          .getPictureSize());
      final File resizedFile = new File(getActivity().getFilesDir(), "resize.jpg");
      try {
        OutputStream fOut = new BufferedOutputStream(new FileOutputStream(resizedFile));
        out.compress(CompressFormat.JPEG, App.PICTURE_QUALITY, fOut);
        fOut.flush();
        fOut.close();
        out.recycle();
      } catch (IOException e) {
        e.printStackTrace();
        // @todo better logging
      }
      RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(resizedFile.getPath()));
      App.rest.location.postPicture(lid, file, "").enqueue(new Callback<vResponse>() {
        @Override
        public void onResponse(Response<vResponse> response, Retrofit retrofit) {
          submit.setProgress(100);
          dialog.dismiss();
          boolean result = resizedFile.delete();
          Log.d(LocationDetailsFragment.class.getSimpleName(), String.valueOf(result));
          generateContent();
        }

        @Override
        public void onFailure(Throwable t) {
          submit.setProgress(-1);
          Util.getSnackbar(root, t.getMessage());
        }
      });

    }

  }
}
