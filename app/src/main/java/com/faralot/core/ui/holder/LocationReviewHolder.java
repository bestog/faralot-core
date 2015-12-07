package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faralot.core.R;
import com.faralot.core.R.color;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.lib.RatingBarView;
import com.faralot.core.rest.model.location.RatingReviews;
import com.faralot.core.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationReviewHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<RatingReviews> mData = new ArrayList<>();

  public LocationReviewHolder(Activity context, List<RatingReviews> data) {
    mData = data;
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public RatingReviews getItem(int position) {
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
      vi = mLayoutInflater.inflate(layout.adapter_location_reviews, parent, false);
      holder = new ViewHolder();
      holder.text = (TextView) vi.findViewById(id.description);
      holder.info = (TextView) vi.findViewById(id.info);
      holder.stars = (RatingBarView) vi.findViewById(id.stars);
      holder.picture = (ImageView) vi.findViewById(id.picture);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    RatingReviews item = getItem(position);
    if (item.getText().isEmpty()) {
      holder.text.setText(mContext.getString(string.no_comment));
      holder.text.setTextColor(mContext.getResources().getColor(color.greyscale900));
    } else {
      holder.text.setText(item.getText());
    }
    holder.stars.setStar(item.getRate());
    holder.info.setText(String.format(mContext.getString(R.string.review_sub), item.getUser(), Util.formatDateTime(item
        .getCreated(), "dd.MM.yy - HH:mm")));
    if (!item.getPicture().isEmpty()) {
      Picasso.with(mContext)
          .load(item.getPicture())
          .resize(100, 100)
          .centerCrop()
          .into(holder.picture);
    }
    return vi;
  }

  static class ViewHolder {
    TextView text;
    TextView info;
    RatingBarView stars;
    ImageView picture;
  }
}
