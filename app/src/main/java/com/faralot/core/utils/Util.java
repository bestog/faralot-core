package com.faralot.core.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.provider.MediaStore.MediaColumns;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R.color;
import com.faralot.core.R.id;
import com.faralot.core.R.string;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.isseiaoki.simplecropview.CropImageView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Klasse: Nuetzliche Funktionen fuer die Applikation
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public final class Util {

  // Loesche alle Fragments im BackStack
  public static final String STACK_FULL = "FULL";
  // Loesche das letzte Fragment im BackStack
  public static final String STACK_ONE = "ONE";

  /**
   * Wandle InputStream in String um
   *
   * @param inputStream InpuStream
   * @return string
   */
  public static String streamToString(InputStream inputStream) {
    return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
  }

  /**
   * Aendere anzeigendes Fragment und loesche gegebenfalls etwas aus dem BackStack
   *
   * @param tag tag
   * @param frag Fragment
   * @param container FrameLayout (id)
   * @param activity Context
   */
  public static void changeFragment(String tag, Fragment frag, int container, Activity activity, String backstack) {
    FragmentManager fragmentManager = activity.getFragmentManager();
    // Backstack
    if (backstack != null) {
      if (backstack.equals(Util.STACK_ONE)) {
        activity.onBackPressed();
      }
    }
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    Fragment fragment = fragmentManager.findFragmentByTag(tag);
    // Replace
    if (fragment == null || tag.isEmpty()) {
      fragmentTransaction.replace(container, frag, tag);
    } else {
      fragmentTransaction.replace(container, fragment, tag);
    }
    fragmentTransaction.addToBackStack(null).commit();
  }

  /**
   * Human readable Distanz Ausgabe
   *
   * @param distance in Meter
   * @return String
   */
  public static String humanDistance(int distance) {
    return distance >= 1000 ? String.format("%.1f", (float) (distance / 1000)) + "km"
        : distance + "m";
  }

  /**
   * Hole Position von gesuchten Element in einem Spinner
   *
   * @param spinner Spinner
   * @param myString String
   * @return int
   */
  public static int getIndexFromSpinner(Spinner spinner, String myString) {
    int index = 0;
    for (int i = 0; i < spinner.getCount(); i++) {
      if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
        index = i;
        break;
      }
    }
    return index;
  }

  /**
   * Runde double-Werte, mit geringen Fehler
   *
   * @param value values
   * @param places Nachkommastellen
   * @return double
   */
  public static float round(double value, int places) {
    if (places < 0) {
      throw new IllegalArgumentException("Nachkommastellen müssen größer gleich 0 sein.");
    }
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.floatValue();
  }

  /**
   * Veraendere die Bildgroesse
   *
   * @param bitmap Bild
   * @param maxSize maximal Pixel (standard: 1200x1200)
   * @return Bitmap
   */
  public static Bitmap resizeImage(Bitmap bitmap, int maxSize) {
    Matrix matrix = new Matrix();
    matrix.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, maxSize, maxSize), ScaleToFit.CENTER);
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }

  /**
   * Veraendere die Groesse eines Drawable
   *
   * @param image Bild
   * @param resources Resources
   * @param dimension Dimension
   * @return Drawable
   */
  public static Drawable resizeDrawable(Drawable image, Resources resources, Dimension dimension) {
    Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
    Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, dimension.getWidth(), dimension.getHeight(), false);
    return new BitmapDrawable(resources, bitmapResized);
  }

  /**
   * Entferne Leerzeichen aus String Array
   *
   * @param list StringArray
   * @return StringArray
   */
  public static String[] trimArray(String... list) {
    for (int i = 0; i < list.length; i++) {
      list[i] = list[i].trim();
    }
    return list;
  }

  /**
   * Dekodiere skaliertes Bitmap von der SD-Karte
   *
   * @param filePath path on sd-card
   * @param maxSize Dimension
   * @return Bitmap
   */
  public static Bitmap decodeScaledBitmapFromSdCard(String filePath, int maxSize) {
    Options options = new Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filePath, options);
    options.inSampleSize = Util.calculateInSampleSize(options, maxSize);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(filePath, options);
  }

  /**
   * Berechne in Sample Size fuer die decodeScaledBitmapFromSdCard Funktion
   *
   * @param options Optionen
   * @param req Dimension
   * @return int
   */
  private static int calculateInSampleSize(Options options, int req) {
    int height = options.outHeight;
    int width = options.outWidth;
    int inSampleSize = 1;
    if (height > req || width > req) {
      int heightRatio = Math.round((float) (height / req));
      int widthRatio = Math.round((float) (width / req));
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }
    return inSampleSize;
  }

  /**
   * Klappe eine ListView in die volle Laenge aus
   *
   * @param listView ListView
   */
  public static void expandListView(ListView listView) {
    ListAdapter mAdapter = listView.getAdapter();
    int totalHeight = 0;
    for (int i = 0; i < mAdapter.getCount(); i++) {
      View mView = mAdapter.getView(i, null, listView);
      mView.measure(
          MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
          MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
      totalHeight += mView.getMeasuredHeight();
    }
    LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + listView.getDividerHeight() * (mAdapter.getCount() - 1);
    listView.setLayoutParams(params);
    listView.requestLayout();
  }

  /**
   * Hole Pfad von Kamera
   *
   * @param intent intent
   * @param activity context
   * @return String
   */
  public static String getPathFromCamera(Intent intent, Context activity) {
    String[] filePathColumn = { MediaColumns.DATA };
    Cursor cursor = activity.getContentResolver()
        .query(intent.getData(), filePathColumn, null, null, null);
    String picturePath = "";
    if (cursor != null) {
      cursor.moveToFirst();
      picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
      cursor.close();
    }
    return picturePath;
  }

  /**
   * Drehe ein Bild im Uhrzeigersinn
   *
   * @param bitmap Bitmap
   * @param dimension Dimension
   * @param rotation Rotation
   * @return Bitmap
   */
  public static Bitmap rotateImage(Bitmap bitmap, Dimension dimension, int rotation) {
    Matrix matrix = new Matrix();
    rotation = (rotation + 1) % 3 * 90;
    matrix.postRotate((float) rotation, (float) dimension.getWidth(),
        (float) dimension.getHeight());
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
  }

  /**
   * Setze Einstellungen fuer die CropImageView
   *
   * @param resources Resources
   * @param image CropImageView
   */
  public static void setCrop(Resources resources, CropImageView image) {
    image.setHandleSizeInDp(8);
    image.setTouchPaddingInDp(20);
    image.setGuideColor(resources.getColor(color.imagecrop_guidelines));
    image.setFrameColor(resources.getColor(color.imagecrop_framelines));
    image.setHandleColor(resources.getColor(color.imagecrop_handlepoints));
  }

  /**
   * Ist das Format der Email-Adresse richtig?
   *
   * @param email E-mail Adresse
   * @return boolean
   */
  public static boolean isValidEmail(CharSequence email) {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email)
        .matches();
  }

  /**
   * Hole Wochentag-Namen anhand des Wochentags
   *
   * @param day int 0-7
   * @return String
   */
  public static String getWeekday(int day) {
    List<String> days = Arrays.asList("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag",
        "Samstag", "Sonntag");
    return days.get(day);
  }

  /**
   * Formatiere Datetime in angegebenes Format um
   *
   * @param value datetime
   * @param format format
   * @return String
   */
  public static String formatDateTime(String value, String format) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN);
    String out = null;
    try {
      Date date = dateFormat.parse(value);
      dateFormat = new SimpleDateFormat(format, Locale.GERMAN);
      out = dateFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
      //@todo: better logging
    }
    return out;
  }

  /**
   * Geocoder-Address zu String
   *
   * @param address Addresse
   * @return String
   */
  public static String addressToString(Address address) {
    StringBuilder stringBuilder = new StringBuilder(100);
    stringBuilder.setLength(0);
    int addressLineSize = address.getMaxAddressLineIndex();
    for (int i = 0; i < addressLineSize; i++) {
      stringBuilder.append(address.getAddressLine(i));
      if (i != addressLineSize - 1) {
        stringBuilder.append(", ");
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Generiere eine Fehlermeldung und zeige sie an
   *
   * @param error Nachricht
   * @param ctx Context
   */
  public static void getMessageDialog(String error, Context ctx) {
    new Builder(ctx)
        .setTitle(ctx.getString(string.error_message))
        .setMessage(error)
        .setCancelable(false)
        .setPositiveButton("ok", new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        }).create().show();
  }

  /**
   * Generiere eine Snackbar und zeige sie an
   *
   * @param root View
   * @param message Nachricht
   */
  public static void getSnackbar(View root, String message) {
    Snackbar snackbar = Snackbar.make(root, message, Snackbar.LENGTH_LONG);
    View view = snackbar.getView();
    TextView tv = (TextView) view.findViewById(id.snackbar_text);
    tv.setTextColor(Color.WHITE);
    snackbar.show();
  }

  /**
   * Setze neue globale Koordinaten
   */
  public static void setNewCoords(String query, Context ctx) {
    Geocoder geocoder = new Geocoder(ctx);
    try {
      List<Address> address = geocoder.getFromLocationName(query, 1);
      App.latitude = round(address.get(0).getLatitude(), App.config.getLocation()
          .getCoordPrecision());
      App.longitude = round(address.get(0).getLongitude(), App.config.getLocation()
          .getCoordPrecision());
    } catch (IOException e) {
      e.printStackTrace();
      //@todo: better logging
    }
  }

  /**
   * Hole alle moeglichen Adressen zu einem Suchtext
   *
   * @param query Suchbegriff
   * @param ctx Context
   * @return String[]
   */
  public static String[] getAddressesFromQuery(String query, Context ctx) {
    List<String> result = new ArrayList<>(5);
    Geocoder geocoder = new Geocoder(ctx);
    if (query == null || query.length() < 3) {
      return result.toArray(new String[result.size()]);
    }
    List<Address> addressList = null;
    try {
      addressList = geocoder.getFromLocationName(query, 5);
    } catch (IOException e) {
      e.printStackTrace();
      //@todo: better logging
    }
    if (addressList == null) {
      addressList = new ArrayList<>(5);
    }
    for (Address address : addressList) {
      result.add(address == null ? "" : Util.addressToString(address));
    }
    return result.toArray(new String[result.size()]);
  }

  /**
   * Setze Einstellungen fuer die MapView
   *
   * @param mapView MapView (osmdroid)
   * @param geoPoint GeoPoint (center)
   */
  public static void setMapViewSettings(MapView mapView, GeoPoint geoPoint, boolean forceZoom) {
    mapView.setTileSource(TileSourceFactory.MAPNIK);
    mapView.setBuiltInZoomControls(true);
    mapView.setMultiTouchControls(true);
    mapView.getOverlayManager().clear();
    if (forceZoom) {
      mapView.getController().setZoom(15);
    }
    mapView.getController().setCenter(geoPoint);
  }

  /**
   * Ueberpruefe, ob der String ein valides JSON ist
   *
   * @param sequence String
   * @return boolean
   */
  public static boolean isJsonValid(String sequence) {
    try {
      new Gson().fromJson(sequence, Object.class);
      return true;
    } catch (JsonSyntaxException ex) {
      ex.printStackTrace();
      // @todo: better logging
      return false;
    }
  }

  /**
   * Ueberpruefe, ob eine App auf dem Smartphone installiert ist
   *
   * @param uri package
   * @param activity Activity
   * @return boolean
   */
  public static boolean isAppInstalled(String uri, Activity activity) {
    PackageManager pm = activity.getPackageManager();
    boolean app_installed;
    try {
      pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
      app_installed = true;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      app_installed = false;
    }
    return app_installed;
  }
}

