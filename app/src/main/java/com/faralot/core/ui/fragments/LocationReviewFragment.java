package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.plurals;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.location.Rating;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.ui.holder.LocationReviewHolder;
import com.faralot.core.utils.Util;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationReviewFragment extends Fragment {

  protected TextView count;
  protected ListView list;
  protected FloatingActionButton add;
  private String lid;

  public LocationReviewFragment() {}

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_location_reviews, container, false);
    lid = getArguments().getString(Location.LID);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.ratings));
    }
    generateContent();
    return view;
  }

  private void initElements(View view) {
    count = (TextView) view.findViewById(id.count);
    list = (ListView) view.findViewById(id.list);
    add = (FloatingActionButton) view.findViewById(id.add);
    add.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onAdd();
      }
    });
  }

  private void generateContent() {
    App.rest.location.getReviews(lid).enqueue(new Callback<Rating>() {
      @Override
      public void onResponse(Response<Rating> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          Rating rating = response.body();
          list.setAdapter(new LocationReviewHolder(LocationReviewFragment.this
              .getActivity(), rating.getReviews()));
          int size = rating.getReviews().size();
          count.setText(getResources()
              .getQuantityString(plurals.review_count, size, size));
        }

      }

      @Override
      public void onFailure(Throwable t) {
        Util.getMessageDialog(t.getMessage(), getActivity());
      }
    });
  }

  protected final void onAdd() {
    add.setVisibility(View.GONE);
    final DialogPlus dialog = DialogPlus.newDialog(getActivity())
        .setContentHolder(new ViewHolder(layout.dialog_location_review))
        .setGravity(Gravity.CENTER)
        .setExpanded(true)
        .setCancelable(true)
        .setFooter(null)
        .setOnDismissListener(new OnDismissListener() {
          @Override
          public void onDismiss(DialogPlus dialog) {
            add.setVisibility(View.VISIBLE);
          }
        })
        .create();
    final ActionProcessButton dialogButton = (ActionProcessButton) dialog.findViewById(id.dialog_review_submit);
    final EditText text = (EditText) dialog.findViewById(id.dialog_review_text);
    final RatingBar rate = (RatingBar) dialog.findViewById(id.dialog_review_rate);
    rate.setNumStars(Math.abs(
        App.config.getLocation().getRate().getFrom() - App.config.getLocation().getRate().getTo()));
    dialogButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dialogButton.setMode(ActionProcessButton.Mode.ENDLESS);
        dialogButton.setProgress(1);
        App.rest.location.postReview(lid, rate.getProgress(), text.getText().toString())
            .enqueue(new Callback<vResponse>() {
              @Override
              public void onResponse(Response<vResponse> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                  generateContent();
                  dialogButton.setProgress(100);
                  add.setVisibility(View.VISIBLE);
                } else {
                  try {
                    Util.getMessageDialog(response.errorBody()
                        .string(), getActivity());
                    dialogButton.setProgress(-1);
                  } catch (IOException e) {
                    e.printStackTrace();
                    //@todo: better logging
                  }
                }
              }

              @Override
              public void onFailure(Throwable t) {
                dialog.dismiss();
                Util.getMessageDialog(t.getMessage(), getActivity());
              }
            });
      }
    });
    dialog.show();
  }
}
