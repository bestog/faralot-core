package com.faralot.core.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.faralot.core.App;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.rest.model.User;
import com.faralot.core.ui.fragments.LoginFragment;
import com.faralot.core.ui.fragments.StartFragment;
import com.faralot.core.utils.Util;

import io.paperdb.Paper;

public class LoginActivity extends FragmentActivity {

  public LoginActivity() {
  }

  @Override
  protected final void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_login);
    if (App.isReady) {
      if (getIntent().getBooleanExtra("logout", false)) {
        Paper.book().destroy();
      }
      if (Paper.book().read(User.TOKEN) == null) {
        Util.changeFragment(LoginFragment.class.getSimpleName(), new LoginFragment(), id.login_frame_layout, this, null);
      } else {
        startActivity(new Intent(this, MainActivity.class).setFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
      }
    } else {
      Util.changeFragment(StartFragment.class.getSimpleName(), new StartFragment(), id.login_frame_layout, this, null);
    }
  }

  @Override
  public final void onBackPressed() {
    if (getFragmentManager().getBackStackEntryCount() == 1) {
      finish();
      super.onBackPressed();
    } else {
      getFragmentManager().popBackStack();
    }
  }
}
