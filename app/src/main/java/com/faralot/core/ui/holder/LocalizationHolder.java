package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.utils.Localization;

import java.util.ArrayList;
import java.util.List;

public class LocalizationHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<Localization> mData = new ArrayList<>();

  public LocalizationHolder(Activity context, List<Localization> data) {
    mData = data;
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public Localization getItem(int position) {
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
      vi = mLayoutInflater.inflate(layout.adapter_localization, parent, false);
      holder = new ViewHolder();
      holder.icon = (ImageView) vi.findViewById(id.icon);
      holder.title = (TextView) vi.findViewById(id.title);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    Localization item = getItem(position);
    holder.icon.setImageDrawable(mContext.getResources().getDrawable(item.getIcon()));
    holder.title.setText(item.getTitle());
    return vi;
  }

  static class ViewHolder {
    ImageView icon;
    TextView title;
  }
}
