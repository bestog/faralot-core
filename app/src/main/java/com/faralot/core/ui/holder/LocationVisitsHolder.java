package com.faralot.core.ui.holder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.rest.model.Location;
import com.faralot.core.rest.model.user.Visits;
import com.faralot.core.ui.fragments.LocationDetailsFragment;
import com.faralot.core.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationVisitsHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<Visits> mData = new ArrayList<>();

  public LocationVisitsHolder(Activity context, List<Visits> data) {
    mData = data;
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public Visits getItem(int position) {
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
      vi = mLayoutInflater.inflate(layout.adapter_location_visits, parent, false);
      holder = new ViewHolder();
      holder.root = (CardView) vi.findViewById(id.root);
      holder.name = (TextView) vi.findViewById(id.name);
      holder.picture = (ImageView) vi.findViewById(id.image);
      holder.times = (TextView) vi.findViewById(id.times);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    final Visits item = getItem(position);
    holder.name.setText(item.getName());
    holder.times.setText(String.valueOf(item.getTimes().size()));
    if (!item.getPicture().equals("")) {
      holder.picture.setImageDrawable(mContext.getResources()
          .getDrawable(R.drawable.img_no_picture_white));
      Picasso.with(mContext)
          .load(item.getPicture())
          .resize(200, 200)
          .centerCrop()
          .into(holder.picture);
    }
    holder.root.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment newFragment = new LocationDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Location.LID, item.getLocation());
        newFragment.setArguments(bundle);
        Util.changeFragment(
            LocationDetailsFragment.class.getSimpleName() + item.getLocation(), newFragment,
            id.frame_container, mContext, null);
      }

    });
    return vi;
  }

  static class ViewHolder {
    CardView root;
    ImageView picture;
    TextView name;
    TextView times;
  }
}
