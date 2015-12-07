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
import com.faralot.core.R.string;
import com.faralot.core.rest.model.Picture;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationPicturesEditHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<Picture> mData = new ArrayList<>();
  private final String mLocationId;

  public LocationPicturesEditHolder(Activity context, List<Picture> data, String lid) {
    mData = data;
    mContext = context;
    mLocationId = lid;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public Picture getItem(int position) {
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
      vi = mLayoutInflater.inflate(layout.adapter_location_pictures_edit, parent, false);
      holder = new ViewHolder();
      holder.user = (TextView) vi.findViewById(id.user);
      holder.picture = (ImageView) vi.findViewById(id.picture);
      holder.date = (TextView) vi.findViewById(id.date);
      holder.delete = (ImageView) vi.findViewById(id.delete);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    final Picture item = getItem(position);
    holder.date.setText(String.format(mContext.getString(string.upload_at), Util.formatDateTime(item
        .getUploaded(), "dd.MM.yy - HH:mm")));
    holder.user.setText(String.format(mContext.getString(string.from_name), item.getFrom()));
    Picasso.with(mContext).load(item.getUrl()).resize(200, 200).centerCrop().into(holder.picture);
    holder.delete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        App.rest.location.deletePicture(mLocationId, item.getFile())
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
    ImageView picture;
    TextView date;
    TextView user;
    ImageView delete;
  }
}
