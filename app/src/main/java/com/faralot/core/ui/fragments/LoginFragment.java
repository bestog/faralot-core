package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.User;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.ui.activities.MainActivity;
import com.faralot.core.utils.Util;

import io.paperdb.Paper;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginFragment extends Fragment {

  protected EditText name;
  protected EditText pwd;
  protected TextView error;
  protected ActionProcessButton button;
  protected Button register;

  public LoginFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_login, container, false);
    initElements(view);
    if (getActivity().getIntent().getBooleanExtra("logout", false)) {
      Paper.book().delete(User.TOKEN);
    } else {
      String token = Paper.book().read(User.TOKEN, "none");
      if (!token.equals("none")) {
        startActivity(new Intent(getActivity(), MainActivity.class));
      }
    }
    return view;
  }

  private void initElements(View view) {
    name = (EditText) view.findViewById(id.login_name);
    pwd = (EditText) view.findViewById(id.login_pwd);
    error = (TextView) view.findViewById(id.login_error);
    register = (Button) view.findViewById(id.login_toregister);
    register.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Util.changeFragment(RegisterFragment.class.getSimpleName(), new RegisterFragment(), id.login_frame_layout, LoginFragment.this
            .getActivity(), null);
      }
    });
    button = (ActionProcessButton) view.findViewById(id.login_button);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onSubmit();
      }
    });

  }

  public final void onSubmit() {
    error.setText("");
    button.setMode(Mode.ENDLESS);
    button.setProgress(1);
    App.rest.user.getLogin(name.getText().toString(), pwd.getText().toString())
        .enqueue(new Callback<vResponse>() {
          @Override
          public void onResponse(Response<vResponse> response, Retrofit retrofit) {
            if (response.isSuccess()) {
              button.setProgress(100);
              String token = response.body().getValue();
              if (!token.isEmpty()) {
                Paper.book().write(User.TOKEN, token);
                startActivity(new Intent(getActivity(), MainActivity.class));
              }
            } else {
              button.setProgress(-1);
              error.setText(getString(string.login_incorrect));
            }
          }

          @Override
          public void onFailure(Throwable t) {
            Util.getMessageDialog(t.getMessage(), getActivity());
            button.setProgress(-1);
          }
        });
  }
}
