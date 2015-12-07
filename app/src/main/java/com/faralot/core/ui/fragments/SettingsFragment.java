package com.faralot.core.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.faralot.core.App;
import com.faralot.core.R.string;
import com.faralot.core.R.xml;
import com.faralot.core.lib.LocalizationSystem;
import com.faralot.core.rest.model.User;
import com.faralot.core.rest.model.vResponse;

import io.paperdb.Paper;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SettingsFragment extends PreferenceFragment {

  public SettingsFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.settings));
    }
    getPreferenceManager().setSharedPreferencesName("settings");
    addPreferencesFromResource(xml.preferences);
    Preference locale = findPreference("localization");
    locale.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue.equals(LocalizationSystem.PALS)) {
          if (!App.palsActive) {
            Toast.makeText(getActivity(), getString(string.pals_not_found_please), Toast.LENGTH_LONG)
                .show();
          }
        }
        return true;
      }
    });
    Preference button = findPreference("refresh_token");
    button.setOnPreferenceClickListener(new OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        App.rest.user.updateToken().enqueue(new Callback<vResponse>() {
          @Override
          public void onResponse(Response<vResponse> response, Retrofit retrofit) {
            if (response.isSuccess()) {
              Paper.book().write(User.TOKEN, response.body().getValue());
              Toast.makeText(getActivity(), SettingsFragment.this
                  .getString(string.token_refresh_success), Toast.LENGTH_LONG)
                  .show();
            }
          }

          @Override
          public void onFailure(Throwable t) {
          }
        });
        return true;
      }
    });
  }
}
