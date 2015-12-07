package com.faralot.core.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.PullRefreshLayout.OnRefreshListener;
import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.iml.ActionProcessButton.Mode;
import com.faralot.core.App;
import com.faralot.core.R;
import com.faralot.core.R.id;
import com.faralot.core.R.layout;
import com.faralot.core.R.string;
import com.faralot.core.rest.model.User;
import com.faralot.core.rest.model.user.Name;
import com.faralot.core.rest.model.user.Visits;
import com.faralot.core.rest.model.vResponse;
import com.faralot.core.ui.activities.MainActivity;
import com.faralot.core.utils.Dimension;
import com.faralot.core.utils.Util;
import com.isseiaoki.simplecropview.CropImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserFragment extends Fragment {

  protected PullRefreshLayout frameLayout;
  protected CircleImageView picture;
  protected TextView username;
  protected TextView realname;
  protected TextView email;
  protected TextView register;
  protected TextView favoriteCount;
  protected TextView visitCount;
  protected TextView pointCount;
  private User user;

  public UserFragment() {
  }

  @Override
  public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(layout.fragment_user, container, false);
    initElements(view);
    setHasOptionsMenu(true);
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(getString(string.my_profile));
    }
    frameLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        generateContent();
      }
    });
    frameLayout.setRefreshing(true);
    generateContent();
    return view;
  }

  private void initElements(View view) {
    frameLayout = (PullRefreshLayout) view.findViewById(id.root);
    picture = (CircleImageView) view.findViewById(id.profile_picture);
    username = (TextView) view.findViewById(id.profile_username);
    realname = (TextView) view.findViewById(id.profile_name);
    email = (TextView) view.findViewById(id.profile_email);
    register = (TextView) view.findViewById(id.profile_register);
    favoriteCount = (TextView) view.findViewById(id.profile_favorite_count);
    visitCount = (TextView) view.findViewById(id.profile_visit_count);
    pointCount = (TextView) view.findViewById(id.profile_point_count);
    ImageView upload = (ImageView) view.findViewById(id.profile_upload);
    upload.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
      }
    });
    Button edit = (Button) view.findViewById(id.profile_edit);
    edit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Util.changeFragment(UserEditFragment.class.getSimpleName(), new UserEditFragment(), id.frame_container, UserFragment.this
            .getActivity(), null);
      }
    });
    CardView favorite = (CardView) view.findViewById(id.profile_favorite);
    favorite.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Util.changeFragment(UserFavoritesFragment.class.getSimpleName(), new UserFavoritesFragment(), id.frame_container, UserFragment.this
            .getActivity(), null);
      }
    });
    CardView visit = (CardView) view.findViewById(id.profile_visit);
    visit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Util.changeFragment(UserVisitsFragment.class.getSimpleName(), new UserVisitsFragment(), id.frame_container, UserFragment.this
            .getActivity(), null);
      }
    });
  }

  private void generateContent() {
    App.rest.user.getMyProfile().enqueue(new Callback<User>() {
      @Override
      public void onResponse(Response<User> response, Retrofit retrofit) {
        user = response.body();
        Name name = user.getName();
        if (!user.getPicture().getUrl().isEmpty()) {
          Picasso.with(getActivity()).load(user.getPicture().getUrl()).resize(200, 200)
              .centerCrop().into(picture);
        }
        username.setText(name.getUser());
        realname.setText(String.format(getString(string.realname), name
            .getFirst(), name.getLast()));
        email.setText(user.getEmail());
        register.setText(String.format(getString(string.registered_since), Util
            .formatDateTime(user
                .getRegistered(), "MM.yyyy")));
        favoriteCount.setText(String.valueOf(user.getFavorites()
            .size()));
        int count = 0;
        for (Visits visit : user.getVisits()) {
          count += visit.getTimes().size();
        }
        visitCount.setText(String.valueOf(count));
        pointCount.setText(String.valueOf(user.getPoints()));
        frameLayout.setRefreshing(false);
      }

      @Override
      public void onFailure(Throwable t) {
        t.printStackTrace();
        //@todo: better logging
      }
    });
  }

  @Override
  public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.menu_profile, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  /**
   * Bild wird ausgesucht und uebergeben
   */
  @Override
  public final void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
      String path = Util.getPathFromCamera(data, getActivity());
      if (path != null) {
        DialogPlus dialog = DialogPlus.newDialog(getActivity())
            .setContentHolder(new ViewHolder(layout.dialog_profile_picture))
            .setGravity(Gravity.CENTER)
            .setCancelable(true)
            .setContentWidth(LayoutParams.WRAP_CONTENT)
            .setContentHeight(LayoutParams.WRAP_CONTENT)
            .setFooter(null)
            .create();
        final CropImageView image = (CropImageView) dialog.findViewById(id.cropimage);
        image.setImageBitmap(Util.decodeScaledBitmapFromSdCard(path, App.config.getUser()
            .getPictureSize()));
        Util.setCrop(getResources(), image);
        Button submit = (Button) dialog.findViewById(id.submit);
        ImageButton rotateButton = (ImageButton) dialog.findViewById(id.rotate);
        rotateButton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            image.setImageBitmap(Util.rotateImage(image.getImageBitmap(), new Dimension(image.getWidth(), image
                .getHeight()), 90));
          }
        });
        submit.setOnClickListener(new UploadClickListener(dialog, image));
        dialog.show();
      } else {
        Util.getMessageDialog("Konnte Bild nicht laden", getActivity());
      }
    }
  }

  private class UploadClickListener implements OnClickListener {

    private final CropImageView image;
    private final DialogPlus dialog;

    public UploadClickListener(DialogPlus dp, CropImageView iv) {
      this.dialog = dp;
      this.image = iv;
    }

    @Override
    public void onClick(View v) {
      final ActionProcessButton submit = (ActionProcessButton) dialog.findViewById(id.submit);
      submit.setMode(Mode.ENDLESS);
      submit.setProgress(1);
      Bitmap out = Util.resizeImage(image.getCroppedBitmap(), App.config.getUser()
          .getPictureSize());
      final File resizedFile = new File(getActivity().getFilesDir(), "resize.jpg");
      try {
        OutputStream fOut = new BufferedOutputStream(new FileOutputStream(resizedFile));
        out.compress(CompressFormat.JPEG, App.PICTURE_QUALITY, fOut);
        fOut.flush();
        fOut.close();
        out.recycle();
      } catch (IOException e) {
        e.printStackTrace();
        // @todo better logging
      }
      RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(resizedFile.getPath()));
      App.rest.user.postPicture(file).enqueue(new Callback<vResponse>() {
        @Override
        public void onResponse(Response<vResponse> response, Retrofit retrofit) {
          submit.setProgress(100);
          dialog.dismiss();
          boolean result = resizedFile.delete();
          Log.d(LocationDetailsFragment.class.getSimpleName(), String.valueOf(result));
          ((MainActivity) getActivity()).setDrawerLayout();
          generateContent();
        }

        @Override
        public void onFailure(Throwable t) {
          submit.setProgress(-1);
          Util.getMessageDialog(t.getMessage(), getActivity());
        }
      });
    }
  }
}
