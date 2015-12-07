package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.rest.model.location.Prices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationPricesHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<Prices> mData = new ArrayList<>();

  public LocationPricesHolder(Activity context, List<Prices> data) {
    mData = data;
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public Prices getItem(int position) {
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
      vi = mLayoutInflater.inflate(layout.adapter_location_prices, parent, false);
      holder = new ViewHolder();
      holder.description = (TextView) vi.findViewById(id.description);
      holder.agegroup = (TextView) vi.findViewById(id.agegroup);
      holder.price = (TextView) vi.findViewById(id.price);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    Prices item = getItem(position);
    holder.description.setText(item.getDescription());
    holder.agegroup.setText(String.format(mContext.getString(R.string.agegroup_year), item.getAgegroup()
        .getFrom(), item.getAgegroup().getTo()));
    holder.price.setText(DecimalFormat.getCurrencyInstance(Locale.GERMANY).format(item.getPrice()));
    return vi;
  }

  static class ViewHolder {
    TextView description;
    TextView agegroup;
    TextView price;
  }
}
