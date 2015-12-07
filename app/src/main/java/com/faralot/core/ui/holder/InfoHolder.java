package com.faralot.core.ui.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.utils.LibInfo;

import java.util.ArrayList;
import java.util.List;

public class InfoHolder extends BaseAdapter {
  private final Activity mContext;
  private final LayoutInflater mLayoutInflater;
  private List<LibInfo> mData = new ArrayList<>();

  public InfoHolder(Activity context, List<LibInfo> data) {
    mData = data;
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mData == null ? -1 : mData.size();
  }

  @Override
  public LibInfo getItem(int position) {
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
      vi = mLayoutInflater.inflate(layout.adapter_info, parent, false);
      holder = new ViewHolder();
      holder.title = (TextView) vi.findViewById(id.title);
      holder.description = (TextView) vi.findViewById(id.description);
      holder.license = (TextView) vi.findViewById(id.license);
      holder.link = (TextView) vi.findViewById(id.link);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    LibInfo item = getItem(position);
    holder.title.setText(String.format(mContext.getString(string.lib_title_version), item.getTitle(),
        item.getVersion()));
    holder.description.setText(item.getDescription());
    holder.license.setText(item.getLicense());
    holder.link.setText(item.getLink());
    return vi;
  }

  static class ViewHolder {
    TextView title;
    TextView description;
    TextView license;
    TextView link;
  }
}
