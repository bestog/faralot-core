package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.rest.model.location.Opening;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationOpeningEditHolder extends BaseAdapter {
  private final LayoutInflater mLayoutInflater;
  private List<Opening> mData = new ArrayList<>();
  private final String mLocationId;

  public LocationOpeningEditHolder(Activity context, List<Opening> data, String lid) {
    mData = data;
    mLocationId = lid;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public Opening getItem(int position) {
    return mData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    View vi = view;
    ViewHolder holder;
    if (vi == null) {
      vi = mLayoutInflater.inflate(layout.adapter_location_opening_edit, parent, false);
      holder = new ViewHolder();
      holder.day = (TextView) vi.findViewById(id.day);
      holder.start = (TextView) vi.findViewById(id.start);
      holder.end = (TextView) vi.findViewById(id.end);
      holder.delete = (ImageView) vi.findViewById(id.delete);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    final Opening item = getItem(position);
    holder.day.setText(Util.getWeekday(item.getWeekday()));
    holder.start.setText(item.getBegin());
    holder.end.setText(item.getEnd());
    holder.delete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        App.rest.location.deleteOpening(mLocationId, item.getWeekday())
            .enqueue(new Callback<vResponse>() {
              @Override
              public void onResponse(Response<vResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                  mData.remove(mData.indexOf(item));
                  notifyDataSetChanged();
                }
              }

              @Override
              public void onFailure(Throwable t) {
              }
            });
      }

    });
    return vi;
  }

  static class ViewHolder {
    TextView day;
    TextView start;
    TextView end;
    ImageView delete;
  }
}
