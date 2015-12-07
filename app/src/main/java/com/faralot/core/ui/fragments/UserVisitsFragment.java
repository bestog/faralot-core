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
import com.faralot.core.rest.model.user.Visits;
import com.faralot.core.ui.holder.LocationVisitsHolder;
import com.faralot.core.utils.Util;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserVisitsFragment extends Fragment {

  protected TextView noResult;
  protected ListView listView;
  protected PullRefreshLayout frameLayout;

  public UserVisitsFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_user_visits, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.my_visits));
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
    noResult = (TextView) view.findViewById(id.visits_no_result);
    listView = (ListView) view.findViewById(id.visits_list);
    frameLayout = (PullRefreshLayout) view.findViewById(id.visits_refresh);
  }

  private void generateContent() {
    App.rest.user.getVisits().enqueue(new Callback<List<Visits>>() {
      @Override
      public void onResponse(Response<List<Visits>> response, Retrofit retrofit) {
        List<Visits> visits = response.body();
        if (visits.size() > 0) {
          listView.setAdapter(new LocationVisitsHolder(UserVisitsFragment.this
              .getActivity(), visits));
          noResult.setVisibility(View.GONE);
        } else {
          noResult.setVisibility(View.VISIBLE);
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
