package com.faralot.core.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.faralot.core.App;
import com.faralot.core.R.styleable;

import java.util.ArrayList;
import java.util.List;

public class RatingBarView extends LinearLayout {

  private List<ImageView> mStars = new ArrayList<>();
  private boolean mClickable;
  private OnRatingListener onRatingListener;
  private Object bindObject;
  private float starImageSize;
  private int starCount;
  private Drawable starEmptyDrawable;
  private Drawable starFillDrawable;

  public final void setStarFillDrawable(Drawable starFillDrawable) {
    this.starFillDrawable = starFillDrawable;
  }

  public List<ImageView> getmStars() {
    return this.mStars;
  }

  public void setmStars(List<ImageView> mStars) {
    this.mStars = mStars;
  }

  public boolean ismClickable() {
    return this.mClickable;
  }

  public OnRatingListener getOnRatingListener() {
    return this.onRatingListener;
  }

  public Object getBindObject() {
    return this.bindObject;
  }

  public float getStarImageSize() {
    return this.starImageSize;
  }

  public int getStarCount() {
    return this.starCount;
  }

  public Drawable getStarEmptyDrawable() {
    return this.starEmptyDrawable;
  }

  public Drawable getStarFillDrawable() {
    return this.starFillDrawable;
  }

  public int getStartCount() {
    return this.startCount;
  }

  public void setStartCount(int startCount) {
    this.startCount = startCount;
  }

  public final void setStarEmptyDrawable(Drawable starEmptyDrawable) {
    this.starEmptyDrawable = starEmptyDrawable;
  }

  public final void setStarCount(int count) {
    this.starCount = count;
  }

  public final void setStarImageSize(float starImageSize) {
    this.starImageSize = starImageSize;
  }

  private int startCount;

  public final void setBindObject(Object bindObject) {
    this.bindObject = bindObject;
  }

  public final void setOnRatingListener(OnRatingListener onRatingListener) {
    this.onRatingListener = onRatingListener;
  }

  public final void setmClickable(boolean clickable) {
    mClickable = clickable;
  }

  public RatingBarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setOrientation(LinearLayout.HORIZONTAL);
    TypedArray a = context.obtainStyledAttributes(attrs, styleable.RatingBarView);
    this.starImageSize = a.getDimension(styleable.RatingBarView_starImageSize, 20.0F);
    this.starCount = a.getInteger(styleable.RatingBarView_starCount, App.config.getLocation()
        .getRate()
        .getTo());
    this.starEmptyDrawable = a.getDrawable(styleable.RatingBarView_starEmpty);
    this.starFillDrawable = a.getDrawable(styleable.RatingBarView_starFill);
    OnClickListener listener = new ImageOnClickListener();
    for (int i = 0; i < this.starCount; ++i) {
      ImageView imageView = this.getStarImageView(context);
      imageView.setOnClickListener(listener);
      this.addView(imageView);
    }
    a.recycle();
  }

  public class ImageOnClickListener implements OnClickListener {

    public ImageOnClickListener() {
    }

    @Override
    public void onClick(View v) {
      if (RatingBarView.this.mClickable) {
        RatingBarView.this.setStar(RatingBarView.this.indexOfChild(v) + 1);
        if (RatingBarView.this.onRatingListener != null) {
          RatingBarView.this.onRatingListener.onRating(RatingBarView.this.bindObject,
              RatingBarView.this.indexOfChild(v) + 1);
        }
      }
    }
  }

  private ImageView getStarImageView(Context context) {
    ImageView imageView = new ImageView(context);
    ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(Math.round(this.starImageSize), Math
        .round(this.starImageSize));
    imageView.setLayoutParams(para);
    imageView.setPadding(0, 0, 5, 0);
    imageView.setImageDrawable(this.starEmptyDrawable);
    imageView.setMaxWidth(10);
    imageView.setMaxHeight(10);
    return imageView;
  }

  public final void setStar(int starCount) {
    starCount = starCount > this.starCount ? this.starCount : starCount;
    starCount = starCount < 0 ? 0 : starCount;
    for (int i = 0; i < starCount; ++i) {
      ((ImageView) this.getChildAt(i)).setImageDrawable(this.starFillDrawable);
    }
    for (int i = this.starCount - 1; i >= starCount; --i) {
      ((ImageView) this.getChildAt(i)).setImageDrawable(this.starEmptyDrawable);
    }
  }

  public interface OnRatingListener {

    void onRating(Object bindObject, int ratingScore);
  }
}
