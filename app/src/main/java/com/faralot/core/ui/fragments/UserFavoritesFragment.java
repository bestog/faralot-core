package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.PullRefreshLayout.OnRefreshListener;
import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.Location;
import com.faralot.core.ui.holder.LocationsHolder;
import com.faralot.core.utils.Util;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserFavoritesFragment extends Fragment {

  protected TextView noResult;
  protected ListView listView;
  protected PullRefreshLayout frameLayout;

  private List<Location> locations;

  public UserFavoritesFragment() {}

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_user_favorites, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.my_favorites));
    }
    frameLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        generateContent();
      }
    });
    frameLayout.setRefreshing(true);
    generateContent();
    return view;
  }

  private void initElements(View view) {
    noResult = (TextView) view.findViewById(id.favorites_no_result);
    listView = (ListView) view.findViewById(id.favorites_list);
    frameLayout = (PullRefreshLayout) view.findViewById(id.favorites_refresh);
  }

  private void generateContent() {
    App.rest.user.getFavorites().enqueue(new Callback<List<Location>>() {
      @Override
      public void onResponse(Response<List<Location>> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          locations = response.body();
          if (locations.size() > 0) {
            listView.setAdapter(new LocationsHolder(UserFavoritesFragment.this
                .getActivity(), locations));
            noResult.setVisibility(View.GONE);
          } else {
            noResult.setVisibility(View.VISIBLE);
          }
        }
        frameLayout.setRefreshing(false);
      }

      @Override
      public void onFailure(Throwable t) {
        Util.getMessageDialog(t.getMessage(), getActivity());
      }
    });
  }
}
