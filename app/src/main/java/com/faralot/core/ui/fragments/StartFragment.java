package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.Config;
import com.faralot.core.rest.model.User;
import com.faralot.core.ui.activities.MainActivity;
import com.faralot.core.utils.Util;

import io.paperdb.Paper;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StartFragment extends Fragment {

  protected TextView message;

  public StartFragment() {}

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_start, container, false);
    initElements(view);
    generateContent();
    return view;
  }

  private void initElements(View view) {
    message = (TextView) view.findViewById(id.message);
  }

  private void generateContent() {
    App.palsActive = Util.isAppInstalled("com.bestog.pals", getActivity());
    App.rest.util.getConfig().enqueue(new Callback<Config>() {
      @Override
      public void onResponse(Response<Config> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          App.config = response.body();
          App.fields = App.config.getLocation().getFields();
          App.radius = App.config.getLocation().getMaxRadius();
          App.isReady = true;
          if (Paper.book().read(User.TOKEN) == null) {
            Util.changeFragment(LoginFragment.class.getSimpleName(), new LoginFragment(), id.login_frame_layout, StartFragment.this
                .getActivity(), Util.STACK_FULL);
          } else {
            startActivity(new Intent(getActivity(), MainActivity.class).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            getActivity().finish();
          }
        } else {
          message.setText(getString(string.app_cannot_start));
        }
      }

      @Override
      public void onFailure(Throwable t) {
        message.setText(string.app_cannot_start);
      }
    });
  }
}
