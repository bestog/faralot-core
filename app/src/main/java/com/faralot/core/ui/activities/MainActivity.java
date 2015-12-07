package com.faralot.core.ui.activities;

import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.lib.LocalizationSystem;
import com.faralot.core.rest.model.User;
import com.faralot.core.ui.fragments.InfoFragment;
import com.faralot.core.ui.fragments.LocationListFragment;
import com.faralot.core.ui.fragments.SettingsFragment;
import com.faralot.core.ui.fragments.UserFavoritesFragment;
import com.faralot.core.ui.fragments.UserFragment;
import com.faralot.core.ui.fragments.UserVisitsFragment;
import com.faralot.core.utils.Util;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

  protected DrawerLayout drawerLayout;
  protected Toolbar toolbar;
  protected CircleImageView imageView;
  protected TextView username;
  protected TextView points;

  public NavigationView navigationView;

  public MainActivity() {
  }

  @Override
  public final void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_main);
    initElements();
    setSupportActionBar(toolbar);
    setDrawerLayout();
    App.localization = new LocalizationSystem(this);
    Util.changeFragment(LocationListFragment.class.getSimpleName(), new LocationListFragment(), R.id.frame_container, this, Util.STACK_FULL);
  }

  private void initElements() {
    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    navigationView = (NavigationView) findViewById(R.id.navigation_view);
    View header = navigationView.getHeaderView(0);
    imageView = (CircleImageView) header.findViewById(R.id.drawer_layout_picture);
    username = (TextView) header.findViewById(R.id.drawer_layout_username);
    points = (TextView) header.findViewById(R.id.drawer_layout_points);
  }

  public void setDrawerLayout() {

    App.rest.user.getMyProfile().enqueue(new Callback<User>() {
      @Override
      public void onResponse(Response<User> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          User user = response.body();
          App.user = user;
          String picture = user.getPicture().getUrl();
          if (picture != null && !picture.equals("")) {
            Picasso.with(getApplicationContext())
                .load(user.getPicture().getUrl())
                .into(imageView);
          }
          username.setText(App.user.getName().getUser());
          points.setText(getResources().getQuantityString(R.plurals.point_count, App.user.getPoints(), App.user
              .getPoints()));
        }
      }

      @Override
      public void onFailure(Throwable t) {
      }
    });
    final Activity activity = this;
    LinearLayout info = (LinearLayout) navigationView.findViewById(R.id.drawer_info);
    info.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Util.changeFragment(InfoFragment.class.getSimpleName(), new InfoFragment(), R.id.frame_container, activity, Util.STACK_FULL);
        drawerLayout.closeDrawers();
      }
    });
    if (App.config != null && !App.config.getLocation().getFields().contains("visits")) {
      navigationView.getMenu().findItem(R.id.drawer_visits).setVisible(false);
    }
    navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        if (id == R.id.drawer_location) {
          Util.changeFragment(LocationListFragment.class.getSimpleName(), new LocationListFragment(), R.id.frame_container, activity, Util.STACK_FULL);
        } else if (id == R.id.drawer_profile) {
          Util.changeFragment(UserFragment.class.getSimpleName(), new UserFragment(), R.id.frame_container, activity, Util.STACK_FULL);
        } else if (id == R.id.drawer_favorites) {
          Util.changeFragment(UserFavoritesFragment.class.getSimpleName(), new UserFavoritesFragment(), R.id.frame_container, activity, Util.STACK_FULL);
        } else if (id == R.id.drawer_visits) {
          Util.changeFragment(UserVisitsFragment.class.getSimpleName(), new UserVisitsFragment(), R.id.frame_container, activity, Util.STACK_FULL);
        } else if (id == R.id.drawer_settings) {
          Util.changeFragment(SettingsFragment.class.getSimpleName(), new SettingsFragment(), R.id.frame_container, activity, Util.STACK_FULL);
        } else if (id == R.id.drawer_logout) {
          Intent intent = new Intent(MainActivity.this, LoginActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
              .putExtra("logout", true);
          startActivity(intent);
        } else {
          Log.e("Error", "ID not found.");
        }
        drawerLayout.closeDrawers();
        return true;
      }
    });
    ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, string.drawer_open, string.drawer_close);
    drawerLayout.setDrawerListener(drawerToggle);
    drawerToggle.syncState();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == id.home) {
      drawerLayout.openDrawer(GravityCompat.START);
    }
    return super.onOptionsItemSelected(item);
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
