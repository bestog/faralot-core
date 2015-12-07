package com.faralot.core.lib;

import android.R.string;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.faralot.core.R.layout;

import java.util.List;

/**
 * MultiSpinner
 *
 * @author Sebastian Gottschlich
 * @version 1.0
 */
public class MultiSpinner extends Spinner
    implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

  private List<String> items;
  private boolean[] selected;
  private String defaultText;
  private MultiSpinnerListener listener;

  public MultiSpinner(Context context) {
    super(context);
  }

  public MultiSpinner(Context arg0, AttributeSet arg1) {
    super(arg0, arg1);
  }

  public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
    super(arg0, arg1, arg2);
  }

  @Override
  public void onClick(DialogInterface dialog, int which, boolean isChecked) {
    this.selected[which] = isChecked;
  }

  @Override
  public void onCancel(DialogInterface dialog) {
    StringBuilder spinnerBuffer = new StringBuilder();
    for (int i = 0; i < this.items.size(); i++) {
      if (this.selected[i]) {
        spinnerBuffer.append(this.items.get(i));
        spinnerBuffer.append(", ");
      }
    }
    String spinnerText = spinnerBuffer.toString();
    spinnerText = spinnerText.length() > 2 ? spinnerText.substring(0, spinnerText.length() - 2)
        : this.defaultText;
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), layout.adapter_multispinner,
        new String[] {
            spinnerText
        });
    this.setAdapter(adapter);
    if (this.selected.length > 0) {
      if (this.listener != null) {
        this.listener.onItemsSelected(this.selected);
      }
    }
  }

  @Override
  public boolean performClick() {
    Builder builder = new Builder(this.getContext());
    builder.setTitle(this.defaultText);
    builder.setMultiChoiceItems(this.items.toArray(new CharSequence[this.items.size()]), this.selected, this);
    builder.setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    builder.setOnCancelListener(this);
    builder.show();
    return true;
  }

  public void setItems(List<String> items, List<String> selects, String allText, int position, MultiSpinnerListener listener) {
    this.items = items;
    defaultText = allText;
    this.listener = listener;
    this.selected = new boolean[items.size()];
    for (int i = 0; i < this.selected.length; i++) {
      if (selects != null) {
        for (String item : selects) {
          if (items.get(i).equals(item)) {
            this.selected[i] = true;
          }
        }
      }
      if (!this.selected[i]) {
        this.selected[i] = false;
      }
    }

    StringBuilder spinnerBuffer = new StringBuilder();
    for (int i = 0; i < items.size(); i++) {
      if (this.selected[i]) {
        spinnerBuffer.append(items.get(i));
        spinnerBuffer.append(", ");
      }
    }
    String spinnerText = spinnerBuffer.toString();
    if (spinnerText.length() > 2) {
      spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
    } else {
      spinnerText = this.defaultText;
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), layout.adapter_multispinner,
        new String[] {
            spinnerText
        });
    this.setAdapter(adapter);
    if (position != -1) {
      this.selected[position] = true;
      listener.onItemsSelected(this.selected);
      this.onCancel(null);
    }
  }

  public interface MultiSpinnerListener {

    void onItemsSelected(boolean... selected);
  }
}