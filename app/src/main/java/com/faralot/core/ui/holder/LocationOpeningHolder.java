package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.rest.model.location.Opening;
import com.faralot.core.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class LocationOpeningHolder extends BaseAdapter {
  private final LayoutInflater mLayoutInflater;
  private List<Opening> mData = new ArrayList<>();

  public LocationOpeningHolder(Activity context, List<Opening> data) {
    mData = data;
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
      vi = mLayoutInflater.inflate(layout.adapter_location_opening, parent, false);
      holder = new ViewHolder();
      holder.day = (TextView) vi.findViewById(id.day);
      holder.start = (TextView) vi.findViewById(id.start);
      holder.end = (TextView) vi.findViewById(id.end);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    Opening item = getItem(position);
    holder.day.setText(Util.getWeekday(item.getWeekday()));
    holder.start.setText(item.getBegin());
    holder.end.setText(item.getEnd());
    return vi;
  }

  static class ViewHolder {
    TextView day;
    TextView start;
    TextView end;
  }
}
