package com.faralot.core.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.utils.Util;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterFragment extends Fragment {

  protected EditText username;
  protected TextInputLayout usernameLayout;
  protected EditText email;
  protected TextInputLayout emailLayout;
  protected EditText password;
  protected TextInputLayout passwordLayout;
  protected EditText repeat;
  protected TextInputLayout repeatLayout;
  protected ActionProcessButton button;

  public RegisterFragment() {}

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_register, container, false);
    initElements(view);
    return view;
  }

  private void initElements(View view) {
    username = (EditText) view.findViewById(id.user);
    usernameLayout = (TextInputLayout) view.findViewById(id.layout_user);
    email = (EditText) view.findViewById(id.email);
    emailLayout = (TextInputLayout) view.findViewById(id.layout_email);
    password = (EditText) view.findViewById(id.pwd);
    passwordLayout = (TextInputLayout) view.findViewById(id.layout_pwd);
    repeat = (EditText) view.findViewById(id.pwd_repeat);
    repeatLayout = (TextInputLayout) view.findViewById(id.layout_repeat);
    button = (ActionProcessButton) view.findViewById(id.register);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onRegister();
      }
    });
  }

  public final void onRegister() {
    button.setMode(Mode.ENDLESS);
    button.setProgress(1);
    boolean error = false;
    if (username.getText().length() == 0) {
      username.setError(getString(string.not_empty_username));
      error = true;
    }
    if (!Util.isValidEmail(email.getText().toString())) {
      email.setError(getString(string.not_empty_email));
      error = true;
    }
    if (password.getText().length() == 0) {
      password.setError(getString(string.not_empty_password));
      error = true;
    }
    if (!password.getText().toString().equals(repeat.getText().toString())) {
      repeat.setError(getString(string.not_equal_password));
      error = true;
    }
    if (error) {
      button.setProgress(-1);
    } else {
      App.rest.user.postRegister(username.getText().toString(), password.getText().toString(), email
          .getText()
          .toString())
          .enqueue(new Callback<vResponse>() {
            @Override
            public void onResponse(Response<vResponse> response, Retrofit retrofit) {
              if (response.isSuccess()) {
                if (response.body().getValue().equals("success")) {
                  button.setProgress(100);
                  Toast.makeText(getActivity(), getString(string.register_success), Toast.LENGTH_LONG)
                      .show();
                  Util.changeFragment(LoginFragment.class.getSimpleName(), new LoginFragment(), id.login_frame_layout, RegisterFragment.this
                      .getActivity(), Util.STACK_ONE);
                } else {
                  button.setProgress(-1);
                  Util.getMessageDialog(getString(string.duplicate_user), RegisterFragment.this
                      .getActivity());
                }
              } else {
                button.setProgress(-1);
                Util.getMessageDialog(getString(string.register_error), RegisterFragment.this
                    .getActivity());
              }
            }

            @Override
            public void onFailure(Throwable t) {
              button.setProgress(-1);
              Util.getMessageDialog(t.getMessage(), getActivity());
            }
          });
    }
  }
}