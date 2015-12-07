package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.rest.model.location.Prices;
import com.faralot.core.rest.model.vResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LocationPricesEditHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<Prices> mData = new ArrayList<>();
  private final String mLocationId;

  public LocationPricesEditHolder(Activity context, List<Prices> data, String lid) {
    mData = data;
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
    mLocationId = lid;
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
  public View getView(final int position, View view, ViewGroup parent) {
    View vi = view;
    ViewHolder holder;
    if (vi == null) {
      vi = mLayoutInflater.inflate(layout.adapter_location_prices_edit, parent, false);
      holder = new ViewHolder();
      holder.description = (TextView) vi.findViewById(id.description);
      holder.agegroup = (TextView) vi.findViewById(id.agegroup);
      holder.price = (TextView) vi.findViewById(id.price);
      holder.delete = (ImageView) vi.findViewById(id.delete);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    final Prices item = getItem(position);
    holder.description.setText(item.getDescription());
    holder.agegroup.setText(String.format(mContext.getString(R.string.agegroup_year), item.getAgegroup()
        .getFrom(), item.getAgegroup().getTo()));
    holder.price.setText(DecimalFormat.getCurrencyInstance(Locale.GERMANY).format(item.getPrice()));
    holder.delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        App.rest.location.deletePrices(mLocationId, position)
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
    TextView description;
    TextView agegroup;
    TextView price;
    ImageView delete;
  }
}
