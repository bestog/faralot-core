package com.faralot.core.ui.holder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faralot.core.R.drawable;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.lib.RatingBarView;
import com.faralot.core.rest.model.Location;
import com.faralot.core.ui.fragments.LocationDetailsFragment;
import com.faralot.core.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationsHolder extends BaseAdapter {
  private final Context mContext;
  private List<Location> mData = new ArrayList<>();

  public LocationsHolder(Context context, List<Location> data) {
    mData = data;
    mContext = context;
  }

  @Override
  public int getCount() {
    return mData.size();
  }

  @Override
  public Location getItem(int position) {
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
    final Location item = getItem(position);
    if (vi == null) {
      vi = LayoutInflater.from(mContext).inflate(layout.adapter_location, parent, false);
      holder = new ViewHolder();
      holder.root = (CardView) vi.findViewById(id.root);
      holder.name = (TextView) vi.findViewById(id.name);
      holder.picture = (ImageView) vi.findViewById(id.image);
      holder.ratingBar = (RatingBarView) vi.findViewById(id.rating);
      holder.distance = (TextView) vi.findViewById(id.distance);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    holder.name.setText(item.getName());
    holder.ratingBar.setStar((int) item.getRating().getAverage());
    if (item.getDistance() > 0) {
      holder.distance.setText(Util.humanDistance(item.getDistance()));
    } else {
      holder.distance.setVisibility(View.GONE);
    }
    Picasso.with(mContext).cancelRequest(holder.picture);
    holder.picture.setImageDrawable(mContext.getResources()
        .getDrawable(drawable.img_picture_empty));
    if (item.getPictures().size() > 0) {
      String picUrl = item.getPictures().get(0).getUrl();
      Picasso.with(mContext)
          .load(picUrl)
          .placeholder(drawable.img_picture_empty)
          .error(drawable.img_picture_empty)
          .resize(200, 200)
          .centerCrop()
          .tag(mContext)
          .into(holder.picture);
    }
    holder.root.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment newFragment = new LocationDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Location.LID, item.getLid());
        newFragment.setArguments(bundle);
        Util.changeFragment(
            LocationDetailsFragment.class.getSimpleName() + item.getLid(), newFragment,
            id.frame_container, (Activity) mContext, null);
      }

    });
    return vi;
  }

  static class ViewHolder {
    CardView root;
    ImageView picture;
    TextView name;
    RatingBarView ratingBar;
    TextView distance;
  }
}
