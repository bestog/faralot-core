package com.faralot.core.ui.fragments;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.User;
import com.faralot.core.rest.model.user.Name;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.ui.activities.LoginActivity;
import com.faralot.core.ui.activities.MainActivity;
import com.faralot.core.utils.Util;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import io.paperdb.Paper;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserEditFragment extends Fragment {

  protected EditText username;
  protected EditText firstname;
  protected EditText lastname;
  protected EditText email;
  protected ActionProcessButton submit;
  private User user;

  public UserEditFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_user_edit, container, false);
    initElements(view);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.edit));
    }
    App.rest.user.getMyProfile().enqueue(new Callback<User>() {
      @Override
      public void onResponse(Response<User> response, Retrofit retrofit) {
        user = response.body();
        username.setText(user.getName().getUser());
        firstname.setText(user.getName().getFirst());
        lastname.setText(user.getName().getLast());
        email.setText(user.getEmail());
      }

      @Override
      public void onFailure(Throwable t) {
        Util.getMessageDialog(t.getMessage(), getActivity());
      }
    });
    return view;
  }

  private void initElements(View view) {
    username = (EditText) view.findViewById(id.edit_profile_username);
    firstname = (EditText) view.findViewById(id.edit_profile_firstname);
    lastname = (EditText) view.findViewById(id.edit_profile_lastname);
    email = (EditText) view.findViewById(id.edit_profile_email);
    submit = (ActionProcessButton) view.findViewById(id.submit);
    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onSubmit();
      }
    });
    Button changePwd = (Button) view.findViewById(id.change_password);
    changePwd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onChangePwd();
      }
    });
    Button delete = (Button) view.findViewById(id.delete_account);
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onDelete();
      }
    });
  }

  protected final void onSubmit() {
    submit.setMode(Mode.ENDLESS);
    submit.setProgress(1);
    boolean error = false;
    if (username.length() == 0) {
      username.setError("Bitte ausfüllen");
      error = true;
    }
    if (firstname.length() == 0) {
      firstname.setError("Bitte ausfüllen");
      error = true;
    }
    if (lastname.length() == 0) {
      lastname.setError("Bitte ausfüllen");
      error = true;
    }
    if (email.length() == 0) {
      email.setError("Bitte ausfüllen");
      error = true;
    }
    if (!error) {
      com.faralot.core.rest.model.request.User request = new com.faralot.core.rest.model.request.User();
      request.setName(new Name(firstname.getText().toString(), lastname.getText()
          .toString(), username.getText().toString()));
      request.setEmail(email.getText().toString());
      App.rest.user.putProfile(request).enqueue(new Callback<vResponse>() {
        @Override
        public void onResponse(Response<vResponse> response, Retrofit retrofit) {
          submit.setProgress(100);
          Util.changeFragment(UserFragment.class.getSimpleName(), new UserFragment(), id.frame_container, UserEditFragment.this
              .getActivity(), Util.STACK_ONE);
        }

        @Override
        public void onFailure(Throwable t) {
          submit.setProgress(-1);
          Util.getMessageDialog(t.getMessage(), getActivity());
        }
      });
    }
  }

  protected void onChangePwd() {
    final DialogPlus dialog = DialogPlus.newDialog(getActivity())
        .setContentHolder(new ViewHolder(layout.dialog_user_change_pwd))
        .setGravity(Gravity.CENTER)
        .setExpanded(true)
        .setCancelable(true)
        .setFooter(null)
        .setPadding(10, 10, 10, 10)
        .create();
    Button dialogButton = (Button) dialog.findViewById(id.submit);
    final EditText first = (EditText) dialog.findViewById(id.first);
    final EditText second = (EditText) dialog.findViewById(id.second);
    dialogButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!first.getText().toString().isEmpty() && first.getText()
            .toString()
            .equals(second.getText().toString())) {
          com.faralot.core.rest.model.request.User request = new com.faralot.core.rest.model.request.User();
          request.setPassword(first.getText().toString());
          App.rest.user.putProfile(request).enqueue(new Callback<vResponse>() {
            @Override
            public void onResponse(Response<vResponse> response, Retrofit retrofit) {
              ((MainActivity) getActivity()).setDrawerLayout();
              dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
              Util.getMessageDialog(t.getMessage(), getActivity());
            }
          });
        } else {
          second.setError(getString(string.not_equal_password));
        }
      }
    });
    dialog.show();

  }

  protected void onDelete() {
    final Builder alertDialog = new Builder(getActivity())
        .setTitle(getString(string.delete_account))
        .setMessage(getString(string.delete_account_question));
    alertDialog
        .setPositiveButton(getString(string.yes), new OnClickListener() {
          @Override
          public void onClick(final DialogInterface dialog, int which) {
            alertDialog.setTitle(getString(string.account_delete_progress))
                .setMessage("");
            App.rest.user.deleteAccount().enqueue(new Callback<vResponse>() {
              @Override
              public void onResponse(Response<vResponse> response, Retrofit retrofit) {
                Paper.book().delete(User.TOKEN);
                getFragmentManager()
                    .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                dialog.cancel();
              }

              @Override
              public void onFailure(Throwable t) {
                dialog.dismiss();
                Util.getMessageDialog(getString(string.delete_account_no_success), UserEditFragment.this
                    .getActivity());
              }
            });
          }
        })
        .setNegativeButton(getString(string.back),
            new OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
              }
            });
    alertDialog.create().show();
  }
}
