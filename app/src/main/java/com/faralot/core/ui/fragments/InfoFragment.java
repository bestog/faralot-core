package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.faralot.core.R;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.ui.holder.InfoHolder;
import com.faralot.core.utils.LibInfo;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

  protected TextView appVersion;
  protected ListView list;

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_info, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.app_info));
    }
    generateContent();
    return view;
  }

  private void initElements(View view) {
    appVersion = (TextView) view.findViewById(R.id.app_and_version);
    list = (ListView) view.findViewById(R.id.listview);
  }

  private void generateContent() {
    try {
      PackageInfo pInfo = getActivity()
          .getPackageManager()
          .getPackageInfo(getActivity().getPackageName(), 0);
      appVersion.setText(String.format(getString(R.string.app_name_version), getString(string.app_name), pInfo.versionName));
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }
    List<LibInfo> libraries = new ArrayList<>(20);
    libraries.add(new LibInfo(
            "PullRefreshLayout",
            "1.2.0",
            "This component like SwipeRefreshLayout, it is more beautiful than SwipeRefreshLayout.",
            "MIT License",
            "https://github.com/baoyongzhang/android-PullRefreshLayout")
    );
    libraries.add(new LibInfo(
            "AndroidImageSlider",
            "1.1.5",
            "An amazing and convenient Android image slider.",
            "",
            "https://github.com/daimajia/AndroidImageSlider")
    );
    libraries.add(new LibInfo(
            "FABProgressCircle",
            "1.01",
            "Material progress circle around any FloatingActionButton.",
            "Apache License, Version 2.0",
            "https://github.com/JorgeCastilloPrz/FABProgressCircle")
    );
    libraries.add(new LibInfo(
            "android-process-button",
            "1.0.4",
            "Android Buttons With Built-in Progress Meters.",
            "MIT License",
            "http://dmytrodanylyk.com/pages/portfolio/portfolio-process-button.html")
    );
    libraries.add(new LibInfo(
            "SimpleCropView",
            "1.0.9",
            "A simple image cropping library for Android.",
            "MIT License",
            "https://github.com/IsseiAoki/SimpleCropView")
    );
    libraries.add(new LibInfo(
            "MaterialSearchView",
            "1.3.0",
            "Cute library to implement SearchView in a Material Design Approach",
            "Apache License, Version 2.0",
            "http://miguelcatalan.info/2015/09/23/MaterialSearchView/")
    );
    libraries.add(new LibInfo(
            "NineOldAndroids",
            "2.4.0",
            "Android library for using the Honeycomb animation API on all versions of the platform back to 1.0!",
            "Apache License, Version 2.0",
            "http://nineoldandroids.com")
    );
    libraries.add(new LibInfo(
            "dialogplus",
            "1.10",
            "Advanced dialog solution for android",
            "Apache License, Version 2.0",
            "https://github.com/orhanobut/dialogplus")
    );
    libraries.add(new LibInfo(
            "okhttp",
            "2.5.0",
            "An HTTP+SPDY client for Android and Java applications.",
            "Apache License, Version 2.0",
            "https://square.github.io/okhttp")
    );
    libraries.add(new LibInfo(
            "Picasso",
            "2.5.2",
            "A powerful image downloading and caching library for Android",
            "Apache License, Version 2.0",
            "http://square.github.io/picasso")
    );
    libraries.add(new LibInfo(
            "Retrofit",
            "2.0.0-beta2",
            "Type-safe HTTP client for Android and Java by Square, Inc.",
            "Apache License, Version 2.0",
            "http://square.github.io/retrofit")
    );
    libraries.add(new LibInfo(
            "CircleImageView",
            "2.0.0",
            "A circular ImageView for Android",
            "Apache License, Version 2.0",
            "https://github.com/hdodenhof/CircleImageView")
    );
    libraries.add(new LibInfo(
            "osmdroid",
            "5.0.1",
            "OpenStreetMap-Tools for Android",
            "Apache License, Version 2.0",
            "https://github.com/osmdroid/osmdroid")
    );
    libraries.add(new LibInfo(
            "osmbonuspack",
            "5.5",
            "A third-party library of (very) useful additional objects for osmdroid",
            "LGPL licence",
            "https://github.com/MKergall/osmbonuspack")
    );
    libraries.add(new LibInfo(
            "Paper",
            "1.0",
            "Paper is a fast NoSQL data storage for Android that lets you save/restore Java objects by using efficient Kryo serialization and handling data structure changes automatically.",
            "Apache License, Version 2.0",
            "https://github.com/pilgr/Paper")
    );
    libraries.add(new LibInfo(
            "Parceler",
            "1.0.3",
            "Android Parcelables made easy through code generation.",
            "Apache License, Version 2.0",
            "http://parceler.org")
    );
    list.setAdapter(new InfoHolder(getActivity(), libraries));
  }
}
